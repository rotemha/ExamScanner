package com.example.examscanner.components.scan_exam.capture.camera;

import android.graphics.Bitmap;
import android.view.View;

public interface CameraOutputHander {
    public View.OnClickListener handleBitmap(Bitmap bm);
}
