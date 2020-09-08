package com.example.examscanner.persistence.local.files_management;

import android.graphics.Bitmap;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface FilesManager {
    public Bitmap get(String id) throws FileNotFoundException;
    public void store(Bitmap bm, String path) throws IOException;
    void tearDown();
    String genId();
    public void delete(String bitmapPath);
}
