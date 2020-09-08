package com.example.examscanner.components.scan_exam.capture_and_detect_corners.integration_with_repo;

import com.example.examscanner.components.scan_exam.capture_and_detect_corners.CaptureAndDetectCornersIntegrationAbsractTest;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;
import com.example.examscanner.stubs.FaultyScannedCaptureRepo;
import com.example.examscanner.use_case_contexts_creators.CornerDetectionContext2Setuper;

import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Test;

public class CaptureAndDetectCornersIntegrationRepoFaultTest extends CaptureAndDetectCornersIntegrationAbsractTest {
    @NotNull
    @Override
    protected CornerDetectionContext2Setuper getContext() {
        CornerDetectionContext2Setuper.stubExamRepo=false;
        CornerDetectionContext2Setuper context2Setuper = new CornerDetectionContext2Setuper(getState());
        ScannedCaptureRepositoryFactory.ONLYFORTESTINGsetTestInstance(new FaultyScannedCaptureRepo());
        context2Setuper.setSCRepo(new ScannedCaptureRepositoryFactory().create());
        return context2Setuper;
    }

    @Override
    @After
    public void tearDown() {
        CornerDetectionContext2Setuper.stubExamRepo=true;
        super.tearDown();
    }

    @Test
    public void test3Caps() {
        super.test3Caps();
    }

    @Test
    public void test1FailCap() {
        super.test1FailCap();
    }


}
