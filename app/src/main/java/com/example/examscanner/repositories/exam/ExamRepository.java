package com.example.examscanner.repositories.exam;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.examscanner.communication.CommunicationFacade;
import com.example.examscanner.communication.CommunicationFacadeFactory;
import com.example.examscanner.communication.entities_interfaces.ExamEntityInterface;

import com.example.examscanner.repositories.Converter;
import com.example.examscanner.repositories.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


public class ExamRepository implements Repository<Exam> {
    private CommunicationFacade comFacade;
    private Converter<ExamEntityInterface, Exam> converter;

    private static ExamRepository instance;
    private static final String TAG = "ExamRepository";
    private int currAvailableId = 0;

    public static ExamRepository getInstance() {
        if (instance == null) {
            final CommunicationFacade comFacade = new CommunicationFacadeFactory().create();
            instance = new ExamRepository(comFacade, new ExamConverter(comFacade));
            return instance;
        } else {
            return instance;
        }
    }
    static void tearDown(){
        instance=null;
    }

    public ExamRepository(CommunicationFacade comFacade, Converter<ExamEntityInterface,Exam> converter) {
        this.comFacade = comFacade;
        this.converter =converter;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public Exam get(long id) {
        return converter.convert(comFacade.getExamById(id));
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<Exam> get(Predicate<Exam> criteria) {
        List<Exam> ans = new ArrayList<>();
        for (ExamEntityInterface ei : comFacade.getExams()) {
            Exam e = converter.convert(ei);
            if (criteria.test(converter.convert(ei)))
                ans.add(e);
        }
        return ans;
    }

    @Override
    public void create(Exam exam) {
        long id= comFacade.createExam(
                exam.getCourseName(),
                exam.getURL(),
                exam.getYear(),
                exam.getTerm(),
                exam.getSemester(),
                exam.getManagerId(),
                exam.getGradersIdentifiers(),
                exam.getSessionId(),
                exam.getNumOfQuestions(),
                exam.getUploaded(),
                exam.getVersions().size()
        );
        exam.setId(id);
        exam.setUploadCompletable(()->comFacade.observeExamUpladed(id));
        insertVersions(exam);
    }

    private void insertVersions(Exam exam) {
        for (Version v: exam.getVersions()) {
            long versionId = comFacade.insertVersionReplaceOnConflict(exam.getId(),v.getNum(), v.getPerfectImage());
            v.setId(versionId);
//            insertQuestions(v);
        }
        for (Version v: exam.getVersions()) {
            insertQuestions(v);
        }
    }

    private void insertQuestions(Version v) {
        for (Question q:v.getQuestions()) {
            long qId = comFacade.insertQuestionReplaceOnConflict(
                    v.getId(), q.getNum(),q.getAns(), q.getLeft(),q.getRight(),q.getUp(),q.getBottom()
            );
            q.setId(qId);
        }
    }

    public void updateUploaded(Exam exam) {
        exam.setUploaded(1);
        comFacade.updateUploaded(exam.getId());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void update(Exam exam) {
        updateVersions(exam.getVersions());
        comFacade.updateExam(
                exam.getId(),
                exam.getCourseName(),
                exam.getSemester(),
                exam.getTerm(),
                exam.getVersions().stream().mapToLong(Version::getId).toArray(),
                exam.getSessionId(),
                exam.getYear());
    }

    private void updateVersions(List<Version> versions) {
        for (Version v:versions) {
            long maybeNewId = comFacade.insertVersionReplaceOnConflict(v.getExam().getId(),v.getNum(), v.getPerfectImage());
            v.setId(maybeNewId);
            //TODO update questions
        }
    }

    @Override
    public void delete(int id) {
        comFacade.deleteExam((long)id);
    }

    @Override
    public void removeFromCache(long id) {
        throw new RuntimeException("not implemented");
    }


//    @Override
//    public int genId() {
//        int ans = currAvailableId;
//        currAvailableId++;
//        return ans;
//    }
}
