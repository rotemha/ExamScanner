package com.example.examscanner.communication.entities_interfaces;

import android.graphics.Bitmap;

public interface SemiScannedCaptureEntityInterface {
    public int getLeftMostX();
    public int getUpperMostY();
    public int getRightMostX();
    public int getBottomMosty();
    public long getSessionId();
    public Bitmap getBitmap();
    long getId();
}
