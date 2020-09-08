package com.example.examscanner.communication.entities_interfaces;

import android.graphics.Bitmap;

import java.io.FileNotFoundException;

public interface VersionEntityInterface {
    public long getId();
    public long getExamId();
    public long[] getQuestions();
    public int getNumber();
    public Bitmap getBitmap() ;
}
