package com.example.examscanner.persistence.local.files_management;

import android.graphics.Bitmap;

import java.util.HashMap;

class StubFilesManager implements FilesManager {
    private static long counter = 0;
    private HashMap<Long, Bitmap> map = new HashMap<Long, Bitmap>();

    @Override
    public Bitmap get(String id) {
        return map.get(id);
    }

    @Override
    public void store(Bitmap bm, String path) {
        long id = counter++;
        map.put(id, bm);
    }

    @Override
    public void tearDown() {

    }

    @Override
    public String genId() {
        return "GENERATED_"+String.valueOf(counter++);
    }

    @Override
    public void delete(String bitmapPath) {

    }
}
