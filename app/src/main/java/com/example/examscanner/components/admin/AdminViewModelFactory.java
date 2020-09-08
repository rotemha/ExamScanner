package com.example.examscanner.components.admin;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.ExamRepositoryFactory;

public class AdminViewModelFactory implements ViewModelProvider.Factory {
    private long examId;

    public AdminViewModelFactory(long examId) {
        this.examId = examId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        Repository<Exam> repo = new ExamRepositoryFactory().create();
        return (T) new AdminViewModel(
                repo,
                repo.get(examId)
        );
    }
}
