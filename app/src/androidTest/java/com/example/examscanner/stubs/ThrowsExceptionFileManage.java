package com.example.examscanner.stubs;

import android.graphics.Bitmap;

import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.persistence.local.files_management.FilesManager;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ThrowsExceptionFileManage implements FilesManager {
    private boolean throwing;

    public ThrowsExceptionFileManage() {
        this.throwing = true;
    }

    @Override
    public Bitmap get(String id) throws FileNotFoundException {
        if(throwing){
            throw new FileNotFoundException("Stub throw excetion");
        }else{
            return BitmapsInstancesFactoryAndroidTest.getTestJpg1();
        }
    }

    @Override
    public void store(Bitmap bm, String path) throws IOException {
        if(throwing){
            throw new IOException("Stub throw excetion");
        }

    }

    @Override
    public void tearDown() {

    }

    @Override
    public String genId() {
        return null;
    }

    @Override
    public void delete(String bitmapPath) {
        throw new RuntimeException("Stub throw excetion");
    }

    public void stopThrowing() {
        throwing = false;
    }
}
