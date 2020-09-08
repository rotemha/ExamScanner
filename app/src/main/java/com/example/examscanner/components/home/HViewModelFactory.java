package com.example.examscanner.components.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.examscanner.authentication.state.StateFactory;
import com.example.examscanner.repositories.exam.ExamRepositoryFactory;
import com.example.examscanner.repositories.session.SESessionRepoFactory;

public class HViewModelFactory implements ViewModelProvider.Factory {
    FragmentActivity activity;
    public HViewModelFactory(FragmentActivity activity) {
        this.activity=activity;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new HomeViewModel(
                new ExamRepositoryFactory().create(),
                new SESessionRepoFactory().create(),
                StateFactory.get()
        );
    }
}
