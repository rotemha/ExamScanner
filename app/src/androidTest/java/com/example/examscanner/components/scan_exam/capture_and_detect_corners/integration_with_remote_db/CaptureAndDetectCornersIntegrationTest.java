package com.example.examscanner.components.scan_exam.capture_and_detect_corners.integration_with_remote_db;

import com.example.examscanner.components.scan_exam.capture_and_detect_corners.CaptureAndDetectCornersIntegrationAbsractTest;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;
import com.example.examscanner.use_case_contexts_creators.CornerDetectionContext2Setuper;

import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CaptureAndDetectCornersIntegrationTest extends CaptureAndDetectCornersIntegrationAbsractTest {
    @Override
    @Before
    public void setUp() {
        USINIG_REAL_DB =true;
        CornerDetectionContext2Setuper.stubExamRepo= false;
        super.setUp();
    }

    @Override
    @After
    public void tearDown(){
        super.tearDown();
        USINIG_REAL_DB =false;
        CornerDetectionContext2Setuper.stubExamRepo= true;
    }

    @NotNull
    @Override
    protected CornerDetectionContext2Setuper getContext() {
        CornerDetectionContext2Setuper context2Setuper = new CornerDetectionContext2Setuper(getState());
        ScannedCaptureRepositoryFactory.ONLYFORTESTINGsetTestInstance(null);
        context2Setuper.setSCRepo(new ScannedCaptureRepositoryFactory().create());
        return context2Setuper;
    }

    @Test
    public void testRetake() {
        super.testRetake();
    }

    @Test
    public void testFinishBatch() {
        super.testFinishBatch();
    }

    @Test
    public void testFinishBatchAndToHome() {
        super.testFinishBatchAndToHome();
    }

    @Test
    public void testFinishBatchAndToCapture() {
        super.testFinishBatchAndToCapture();
    }


}
