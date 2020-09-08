package com.example.examscanner.components.create_exam;

import android.graphics.Bitmap;
import android.os.Build;
import android.text.Editable;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.examscanner.authentication.state.State;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ScanAnswersConsumer;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.ExamInCreation;
import com.example.examscanner.repositories.exam.Question;
import com.example.examscanner.repositories.grader.Grader;
import com.example.examscanner.repositories.scanned_capture.ResolvedAnswer;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;
import com.example.examscanner.repositories.exam.Version;

import java.util.ArrayList;
import java.util.List;

public class CreateExamModelView extends ViewModel {
    private MutableLiveData<Integer> addedVersions;
    private Repository<Grader> gRepo;
    private ImageProcessingFacade imageProcessor;
    private State state;
    private ExamInCreation examCreated;
    private Repository<Exam> eRepo;
    private Bitmap currentVersionBitmap;
    private String currentGraderUsername;
    private Integer currentVersionNumber;
    private List<Grader> graders;


    public CreateExamModelView(Repository<Exam> eRepo, Repository<Grader> gRepo, ImageProcessingFacade imageProcessor, State state, long sessionId) {
        this.eRepo = eRepo;
        this.gRepo = gRepo;
        this.imageProcessor = imageProcessor;
        this.state = state;
        examCreated = new ExamInCreation(sessionId);
        addedVersions = new MutableLiveData<>(0);
        graders = new ArrayList<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void create(String courseName, String term, String semester, String year){
        eRepo.create(
                examCreated.commit(
                        state.getId(),
                        courseName,
                        Term.createByViewValue(term).getValue(),
                        Semester.createByViewValue(semester).getValue(),
                        graders,
                        year
                )
        );
    }

    public MutableLiveData<Integer> getAddedVersions() {
        return addedVersions;
    }



    public void holdVersionBitmap(Bitmap bitmap){
        currentVersionBitmap = bitmap;

    }
    public void addVersion() {
        imageProcessor.scanAnswers(currentVersionBitmap, examCreated.getNumOfQuestions(), new ScanAnswersConsumer() {
            @Override
            public void consume(int numOfAnswersDetected, int[] answersIds, float[] lefts, float[] tops, float[] rights, float[] bottoms, int[] selections) {
                ScannedCapture scannedCapture = new ScannedCapture(-1,null,numOfAnswersDetected,numOfAnswersDetected,answersIds,lefts,tops,rights,bottoms,selections);
                if(versionScanningWentWell(scannedCapture))
                    throw new VersionScanningDidntGoWell();
                Version v = new Version(-1,currentVersionNumber,examCreated, Version.theEmptyFutureQuestionsList());
                examCreated.addVersion(v);
                for (ResolvedAnswer ans: scannedCapture.getResolvedAnswers()) {
                    v.addQuestion(
                            new Question(
                                    -1,
                                    v,ans.getAnsNum(),
                                    ans.getSelection(),
                                    (int)(ans.getUpperLeft().x*currentVersionBitmap.getWidth()),
                                    (int)(ans.getUpperLeft().y*currentVersionBitmap.getHeight()),
                                    (int)(ans.getBottomRight().x*currentVersionBitmap.getWidth()),
                                    (int)(ans.getBottomRight().y*currentVersionBitmap.getHeight())
                            )
                    );
                }
            }
        });
    }

    private boolean versionScanningWentWell(ScannedCapture scannedCapture) {
        return false;
    }

    public Exam getExam() {
        return examCreated;
    }

    public void holdVersionNumber(Integer verNum) {
        this.currentVersionNumber = verNum;
    }

    public void incNumOfVersions() {
        addedVersions.setValue(addedVersions.getValue()+1);
    }

    public Bitmap getCurrentVersionBitmap() {
        return currentVersionBitmap;
    }

    public Integer getCurrentVersionNumber() {
        return currentVersionNumber;
    }

    public void holdGraderUsername(String graderUsername) {
        this.currentGraderUsername = graderUsername;
    }
    public void holdNumOfQuestions(String text) {
        examCreated.setNumOfQuestions(Integer.parseInt(text));
    }

    public void addGrader() {
        final List<Grader> gradersWithCurrentUsername = gRepo.get(grader -> grader.getUserName().equals(currentGraderUsername));
        if(
                gradersWithCurrentUsername
                .size() ==0
        ) throw new NoSuchGraderException();
        graders.add(gradersWithCurrentUsername.get(0));
    }

    public String getCurrentGrader() {
        return currentGraderUsername;
    }
}
