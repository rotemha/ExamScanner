package com.example.examscanner.components.scan_exam.capture;


import android.graphics.Bitmap;

import androidx.camera.core.ImageCapture;

import com.example.examscanner.repositories.exam.Version;


public class Capture {
    private ImageCapture.OutputFileResults captureResults;
    private Bitmap bitmap;
    private String examineeId;
    private Version version;

    public Capture(ImageCapture.OutputFileResults captureResults) {
        this.captureResults = captureResults;
    }

    public Capture(Bitmap bitmap, String examineeId, Version version) {
        this.bitmap = bitmap;
        this.examineeId = examineeId;
        this.version = version;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap align) {
        bitmap = align;
    }

    public Version getVersion() {
        return version;
    }

    public String getExamineeId() {
        return examineeId;
    }
}
