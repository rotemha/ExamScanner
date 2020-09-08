package com.example.examscanner.components.scan_exam.capture_and_detect_corners.integration_with_repo;

import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.components.scan_exam.capture_and_detect_corners.CaptureAndDetectCornersIntegrationAbsractTest;
import com.example.examscanner.image_processing.ImageProcessor;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;
import com.example.examscanner.use_case_contexts_creators.CornerDetectionContext2Setuper;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static com.example.examscanner.ImageProcessorsGenerator.alignmentSubIp;

public class CaptureAndDetectStressTest extends CaptureAndDetectCornersIntegrationAbsractTest {
    @NotNull
    @Override
    protected CornerDetectionContext2Setuper getContext() {
        CornerDetectionContext2Setuper context2Setuper = new CornerDetectionContext2Setuper(
                getState(),
                alignmentSubIp(BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_27(), new ImageProcessor(getApplicationContext()))
        );
        ScannedCaptureRepositoryFactory.ONLYFORTESTINGsetTestInstance(null);
        context2Setuper.setSCRepo(new ScannedCaptureRepositoryFactory().create());
        return context2Setuper;
    }

    @Test
    public void testTake5() {
        super.testTake5();
    }
    @Test
    public void testQuickRetake() {
        super.testQuickRetake();
    }
}
