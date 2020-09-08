package com.example.examscanner.components.admin;

import androidx.lifecycle.ViewModel;

import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.exam.Exam;

public class AdminViewModel extends ViewModel {

    private Repository<Exam> examRepository;
    private Exam exam;

    public AdminViewModel(Repository<Exam> examRepository, Exam exam) {
        this.examRepository = examRepository;
        this.exam = exam;
    }

    public void delete() {
        examRepository.delete((int)exam.getId());
    }
}
