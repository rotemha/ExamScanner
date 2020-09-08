package com.example.examscanner.components.scan_exam.capture;

import android.view.View;

import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.components.scan_exam.capture.camera.CameraManager;
import com.example.examscanner.components.scan_exam.capture.camera.CameraOutputHander;

public class CameraManagerStub implements CameraManager {
    @Override
    public void setUp() {

    }

    @Override
    public View.OnClickListener createCaptureClickListener(CameraOutputHander handler) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.handleBitmap(BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_1());
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
