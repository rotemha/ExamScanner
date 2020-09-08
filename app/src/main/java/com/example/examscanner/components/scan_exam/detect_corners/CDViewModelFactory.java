package com.example.examscanner.components.scan_exam.detect_corners;


import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.repositories.corner_detected_capture.CDCRepositoryFacrory;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.ExamRepositoryFactory;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;
import com.example.examscanner.stubs.BitmapInstancesFactory;

public class CDViewModelFactory implements ViewModelProvider.Factory {
    FragmentActivity activity;
    private Exam exam;

    public CDViewModelFactory(FragmentActivity activity, long examId) {
        this.activity = activity;
        this.exam = new ExamRepositoryFactory().create().get(examId);
    }

    public CDViewModelFactory() {}

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new CornerDetectionViewModel(
                new ImageProcessingFactory().create(),
                new ScannedCaptureRepositoryFactory().create(),
                exam
        );
    }

//    private class InitialFactory implements ViewModelProvider.Factory{
//        @NonNull
//        @Override
//        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
//            return (T)new CornerDetectionViewModel(
//                    new ImageProcessingFactory().create(),
//                    new ResolveAnswersViewModelFactory(activity)
//                            .create(ResolveAnswersViewModel.class)
//            );
//        }
//    }
}
