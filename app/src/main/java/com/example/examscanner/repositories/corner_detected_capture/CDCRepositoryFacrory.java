package com.example.examscanner.repositories.corner_detected_capture;

import com.example.examscanner.repositories.Repository;

public class CDCRepositoryFacrory {
    private static Repository<CornerDetectedCapture> testInstance;

    public static void tearDown() {
        CDCRepository.tearDown();
    }

    public Repository<CornerDetectedCapture> create(){
        if (testInstance!=null)return testInstance;
        else return CDCRepository.getInstance();
    }

    public static void ONLYFORTESTINGsetTestInstance(Repository<CornerDetectedCapture> theTestInstance){
        testInstance =theTestInstance;
    }
}
