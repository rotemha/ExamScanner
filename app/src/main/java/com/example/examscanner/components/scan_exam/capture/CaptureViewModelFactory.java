package com.example.examscanner.components.scan_exam.capture;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.examscanner.authentication.state.StateFactory;
import com.example.examscanner.communication.CommunicationFacadeFactory;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.repositories.corner_detected_capture.CDCRepositoryFacrory;
import com.example.examscanner.repositories.exam.ExamRepositoryFactory;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;
import com.example.examscanner.stubs.BitmapInstancesFactory;

public class CaptureViewModelFactory implements ViewModelProvider.Factory {
    FragmentActivity activity;
    private long examId;
    private long sessionId;

    public CaptureViewModelFactory(FragmentActivity activity , long sessionId, long examId) {
        this.activity=activity;
        this.examId = examId;
//        if(sessionId == -1){
        if(true){//sessions disabled
//            this.sessionId = new CommunicationFacadeFactory().create().createNewScanExamSession(examId);
            this.sessionId = -1;
        }else{
            this.sessionId = sessionId;
        }

    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CaptureViewModel(
                new ScannedCaptureRepositoryFactory().create(),
                new ImageProcessingFactory().create(),
                sessionId,
                new ExamRepositoryFactory().create().get(examId),
                StateFactory.get()
        );
    }
}
