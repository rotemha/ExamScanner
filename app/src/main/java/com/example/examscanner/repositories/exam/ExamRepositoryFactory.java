package com.example.examscanner.repositories.exam;

import com.example.examscanner.repositories.Repository;

public class ExamRepositoryFactory {
    private static Repository<Exam> testInstance;
    public Repository<Exam> create(){
        if(testInstance!=null) return testInstance;
        return ExamRepository.getInstance();
    }

    public static void setStubInstance(Repository<Exam> examRepository) {
        testInstance = examRepository;
    }
    public static void tearDown(){
        ExamRepository.tearDown();
        testInstance = null;
    }
}
