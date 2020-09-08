package com.example.examscanner;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.examscanner.authentication.UIAuthenticationHandlerFactory;
import com.example.examscanner.authentication.state.StateFactory;
import com.example.examscanner.repositories.grader.GraderRepoFactory;
import com.example.examscanner.repositories.grader.GraderRepository;

public class MainActivityViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new MainActivityViewModel(
                StateFactory.get(),
                UIAuthenticationHandlerFactory.get(),
                GraderRepoFactory.create()
        );
    }
}
