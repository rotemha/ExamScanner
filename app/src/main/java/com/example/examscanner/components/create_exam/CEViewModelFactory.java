package com.example.examscanner.components.create_exam;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.examscanner.authentication.state.StateFactory;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.repositories.exam.ExamRepositoryFactory;
import com.example.examscanner.repositories.grader.GraderRepoFactory;
import com.example.examscanner.repositories.session.CESessionProviderFactory;
import com.example.examscanner.stubs.BitmapInstancesFactory;

public class CEViewModelFactory implements ViewModelProvider.Factory {
    private FragmentActivity activity;

    public CEViewModelFactory(FragmentActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new CreateExamModelView(
                new ExamRepositoryFactory().create(),
                new GraderRepoFactory().create(),
                new ImageProcessingFactory().create(),
                StateFactory.get(),
                new CESessionProviderFactory().create().provide()
        );
    }
}
