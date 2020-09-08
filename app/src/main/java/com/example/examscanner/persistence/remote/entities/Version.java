package com.example.examscanner.persistence.remote.entities;

import android.graphics.Bitmap;

public class Version {
    public static String metaExamId = "examId";
    public static String metaVersionNumber = "versionNumber";
    public static String metaBitmapPath = "bitmapPath";
    public String examId;
    public int versionNumber;
    private String id;
    public String bitmapPath;

    public Version() {
    }

    public Version(String examId, int versionNumber, String bmPath) {
        this.examId = examId;
        this.versionNumber = versionNumber;
        this.bitmapPath = bmPath;
    }

    public String _getId() {
        return id;
    }
    public void  _getId(String theId) {
        id= theId;
    }
}
