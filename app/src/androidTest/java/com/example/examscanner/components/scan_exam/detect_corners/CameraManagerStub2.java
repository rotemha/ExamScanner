package com.example.examscanner.components.scan_exam.detect_corners;

import android.view.View;

import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.components.scan_exam.capture.camera.CameraManager;
import com.example.examscanner.components.scan_exam.capture.camera.CameraOutputHander;

class CameraManagerStub2 implements CameraManager {
    @Override
    public void setUp() {

    }

    @Override
    public View.OnClickListener createCaptureClickListener(CameraOutputHander handler) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.handleBitmap(BitmapsInstancesFactoryAndroidTest.getTestJpg1());
            }
        };

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }
}
