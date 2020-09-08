package com.example.examscanner;

import android.content.Context;
import android.view.View;

import com.example.examscanner.components.scan_exam.capture.camera.CameraManager;
import com.example.examscanner.components.scan_exam.capture.camera.CameraMangerFactory;
import com.example.examscanner.components.scan_exam.capture.camera.CameraOutputHander;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.image_processing.ImageProcessor;
import com.example.examscanner.stubs.BitmapInstancesFactory;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static com.example.examscanner.stubs.ImageProcessorStubFactory.alignmentSubIp;

class StubsSetupManager {

    private static BitmapInstancesFactory bitmapInstancesFactory;

    public static void setup(Context c) {
        bitmapInstancesFactory = new BitmapInstancesFactory(c);
        CameraMangerFactory.setStubInstance(new CameraManager() {
            @Override
            public void setUp() {

            }

            @Override
            public View.OnClickListener createCaptureClickListener(CameraOutputHander handler) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handler.handleBitmap(bitmapInstancesFactory.get_1());
                    }
                };
            }

            @Override
            public void onPause() {

            }

            @Override
            public void onDestroy() {

            }
        });

        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(alignmentSubIp(bitmapInstancesFactory.getComp191_V1_ins_27(), new ImageProcessor(c)));
    }
}
