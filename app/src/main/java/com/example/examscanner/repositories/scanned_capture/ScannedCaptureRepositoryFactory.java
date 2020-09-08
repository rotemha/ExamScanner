package com.example.examscanner.repositories.scanned_capture;

import com.example.examscanner.repositories.Repository;

public class ScannedCaptureRepositoryFactory {
    private static Repository<ScannedCapture> testInstance = null;

    public static void tearDown() {
        ScannedCaptureRepository.tearDown();
        testInstance=null;
    }

    public Repository<ScannedCapture> create(){
        if(testInstance!=null){
            return testInstance;
        }
        return ScannedCaptureRepository.getInstance();
    }

    public static void ONLYFORTESTINGsetTestInstance(Repository<ScannedCapture> scannedCaptureRepository) {
        testInstance =scannedCaptureRepository;
    }
}
