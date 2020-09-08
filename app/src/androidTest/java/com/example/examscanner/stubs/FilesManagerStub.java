package com.example.examscanner.stubs;

import android.graphics.Bitmap;

import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.persistence.local.files_management.FilesManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FilesManagerStub implements FilesManager {
    Map<String, Bitmap> map = new HashMap<>();
    @Override
    public Bitmap get(String id) throws FileNotFoundException {
        return map.get(id);
    }

    @Override
    public void store(Bitmap bm, String path) throws IOException {
        map.put(path,bm);
    }

    @Override
    public void tearDown() {
        map.clear();
    }

    @Override
    public String genId() {
        return null;
    }

    @Override
    public void delete(String bitmapPath) {
        map.remove(bitmapPath);
    }
}
