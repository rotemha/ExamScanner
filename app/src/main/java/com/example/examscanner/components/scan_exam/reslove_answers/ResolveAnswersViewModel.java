package com.example.examscanner.components.scan_exam.reslove_answers;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.scanned_capture.ConflictedAnswer;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;

import java.util.ArrayList;
import java.util.List;

public class ResolveAnswersViewModel extends ViewModel {
//    private MutableLiveData<Integer> mNumOfIdentified;
//    private MutableLiveData<Integer> mNumOfUnidentified;
    private MutableLiveData<Integer> mNumOfAnswersScannedAnswers;
    private List<MutableLiveData<ScannedCapture>> scannedCaptures;
    private ImageProcessingFacade imageProcessor;
    private Repository<ScannedCapture> scRepo;

    public ResolveAnswersViewModel(ImageProcessingFacade imageProcessor, Repository<ScannedCapture> scRepo) {
        this.imageProcessor=imageProcessor;
        this.scRepo = scRepo;
//        mNumOfIdentified = new MutableLiveData<>(0);
//        mNumOfUnidentified = new MutableLiveData<>(0);
        scannedCaptures = new ArrayList<>();
        for (ScannedCapture sc: scRepo.get(sc->true)) {
            scannedCaptures.add(new MutableLiveData<>(sc));
        }
        mNumOfAnswersScannedAnswers = new MutableLiveData<>(scannedCaptures.size());
    }


    public List<MutableLiveData<ScannedCapture>> getScannedCaptures() {
        return scannedCaptures;
    }

    public MutableLiveData<ScannedCapture> getScannedCapture(int captureId) {
        return scannedCaptures.get(captureId);
    }

    public List<MutableLiveData<ConflictedAnswer>> getConflictedAnswers(int scanId) {
        List<MutableLiveData<ConflictedAnswer>> ans = new ArrayList<>();
        for (ConflictedAnswer ca:scannedCaptures.get(scanId).getValue().getConflictedAnswers()) {
            ans.add(new MutableLiveData<>(ca));
        }
        return ans;
    }

    public void commitResolutions(int scanId) {
        ScannedCapture scannedCapture = getScannedCapture(scanId).getValue();
        scannedCapture.commitResolutions();
        getScannedCapture(scanId).setValue(scannedCapture);
        scRepo.create(scannedCapture);
    }
}
