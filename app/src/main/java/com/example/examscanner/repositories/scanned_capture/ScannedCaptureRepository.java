package com.example.examscanner.repositories.scanned_capture;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.examscanner.communication.CommunicationException;
import com.example.examscanner.communication.CommunicationFacade;
import com.example.examscanner.communication.CommunicationFacadeFactory;
import com.example.examscanner.communication.entities_interfaces.ExamineeSolutionsEntityInterface;
import com.example.examscanner.communication.entities_interfaces.QuestionEntityInterface;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.RepositoryException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class ScannedCaptureRepository implements Repository<ScannedCapture> {

    private static ScannedCaptureRepository instance;
    private boolean dirty;
    private int currAvailableId = 0;
    private ScannedCaptureConverter converter;
    private CommunicationFacade comFacade;
    private List<ScannedCapture> cache;
    private String TAG;


    public static ScannedCaptureRepository getInstance(){
        if (instance==null){
            final CommunicationFacade communicationFacade = new CommunicationFacadeFactory().create();
            instance = new ScannedCaptureRepository(
                    new ScannedCaptureConverter(communicationFacade),
                    communicationFacade
            );
            return instance;
        }else{
            return instance;
        }
    }

    static void tearDown() {
        instance=null;
    }

    public ScannedCaptureRepository(ScannedCaptureConverter converter, CommunicationFacade comFacade) {
        this.converter = converter;
        this.comFacade = comFacade;
        cache = new ArrayList<>();
        this.dirty = true;
        cache = get(x->true);
        this.dirty = false;
        converter.onApprove(()-> this.dirty=true);
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public ScannedCapture get(long id) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<ScannedCapture> get(Predicate<ScannedCapture> criteria) {
        List<ScannedCapture> ans = new ArrayList<>();
        if(!dirty){
            for (ScannedCapture sc : cache){
                if(sc.isValid() && criteria.test(sc)){
                    ans.add(sc);
                }
            }
            return ans;
        }else{
            cache.clear();
        }
        for (ExamineeSolutionsEntityInterface ei: comFacade.getExamineeSoultions()) {
            final ScannedCapture convert = converter.convert(ei);
            if(convert.isValid() && criteria.test(convert)){
                cache.add(convert);
                ans.add(convert);
            }
        }
        dirty=false;
        return ans;
    }

    private ScannedCapture inCache(long id) {
        for (ScannedCapture scannedCapture : cache) {
            if(id== scannedCapture.getId()){
                return scannedCapture;
            }
        }
        return null;
    }

    @Override
    public void create(ScannedCapture scannedCapture) {
        dirty=true;
        try {
            final long id = comFacade.createExamineeSolution(
                    -1,
                    scannedCapture.getBm(),
                    scannedCapture.getExamineeId(),
                    scannedCapture.getVersion().getId(),
                    scannedCapture.getOrigBitmap()
            );

            for (Answer a:scannedCapture.getAnswers()) {
                comFacade.addExamineeAnswer(
                        id,
                        scannedCapture.getVersion().getQuestionByNumber(a.getAnsNum()).getId(),
                        a.getSelection(),
                        (int)(a.getLeft()*scannedCapture.getBm().getWidth()),
                        (int)(a.getUp()*scannedCapture.getBm().getHeight()),
                        (int)(a.getRight()*scannedCapture.getBm().getWidth()),
                        (int)(a.getBottom()*scannedCapture.getBm().getHeight())
                );
            }
            comFacade.addExamineeGrade(id, scannedCapture.calcGrade());
            comFacade.addGraderToSolution(id, scannedCapture.getGraderEmail());
            scannedCapture.setApprovalCallback(g->comFacade.approveSolutionAndStats(scannedCapture.getId(), g));
            scannedCapture.setId((int)id);
            scannedCapture.setValid();
            comFacade.validateSolution((long)id);
        }catch (CommunicationException e){
            try {
                delete(scannedCapture.getId());
            }catch (Exception someE){
                Log.d(TAG,"scanned capture transction failed and failed to delete. not to bad",someE);
            }
            throw new RepositoryException(e);
        }
    }

    @Override
    public void update(ScannedCapture scannedCapture) {
        float grade = scannedCapture.calcGrade();
        for (Answer a:scannedCapture.getAnswers()) {
            comFacade.updateExamineeAnswer(
                    scannedCapture.getId(),
                    scannedCapture.getVersion().getQuestionByNumber(a.getAnsNum()).getId(),
                    a.getSelection(),
                    (int)(a.getLeft()*scannedCapture.getBm().getWidth()),
                    (int)(a.getUp()*scannedCapture.getBm().getHeight()),
                    (int)(a.getRight()*scannedCapture.getBm().getWidth()),
                    (int)(a.getBottom()*scannedCapture.getBm().getHeight())
            );
        }
        comFacade.updateExamineeGrade((long)scannedCapture.getId(), grade);
    }

    @Override
    public void delete(int id) {
        dirty=true;
        comFacade.deleteExamineeSolution(id);
    }

    @Override
    public void removeFromCache(long id) {
        comFacade.removeExamineeSolutionFromCache(id);
    }

//    @Override
//    public int genId() {
//        int ans = currAvailableId;
//        currAvailableId++;
//        return ans;
//    }
}
