package com.example.examscanner.components.scan_exam.capture.camera;

import android.view.View;

public interface CameraManager {
    public void setUp();
    public View.OnClickListener createCaptureClickListener(CameraOutputHander handler);
    public void onPause();
    public void onDestroy();
}
