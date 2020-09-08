package com.example.examscanner.repositories.grader;

import com.example.examscanner.repositories.Repository;

public class GraderRepoFactory {
    public static void tearDown() {
        GraderRepository.tearDown();
    }

    public static void setStubInstance(Repository<Grader> graderRepository) {
        GraderRepository.setInstance(graderRepository);
    }



    public static Repository<Grader> create(){
        return GraderRepository.getInstance();
    }
}
