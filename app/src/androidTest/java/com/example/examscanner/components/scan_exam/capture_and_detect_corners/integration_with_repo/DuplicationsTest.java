package com.example.examscanner.components.scan_exam.capture_and_detect_corners.integration_with_repo;

import com.example.examscanner.R;
import com.example.examscanner.components.scan_exam.capture_and_detect_corners.CaptureAndDetectCornersIntegrationAbsractTest;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;
import com.example.examscanner.use_case_contexts_creators.CornerDetectionContext2Setuper;
import com.example.examscanner.use_case_contexts_creators.CornerDetectionContext4Setuper;

import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.examscanner.Utils.sleepCameraPreviewSetupTime;
import static com.example.examscanner.Utils.sleepSingleCaptureProcessingTime;
import static com.example.examscanner.Utils.sleepSwipingTime;

public class DuplicationsTest extends CaptureAndDetectCornersIntegrationAbsractTest {

    @Override
    @After
    public void tearDown() {
        CornerDetectionContext2Setuper.stubExamRepo=true;
        super.tearDown();
    }

    @NotNull
    @Override
    protected CornerDetectionContext2Setuper getContext() {
        CornerDetectionContext4Setuper.stubExamRepo=false;
        final CornerDetectionContext4Setuper theCOntext = new CornerDetectionContext4Setuper(getState());
        ScannedCaptureRepositoryFactory.ONLYFORTESTINGsetTestInstance(null);
        theCOntext.setSCRepo(new ScannedCaptureRepositoryFactory().create());
        return theCOntext;
    }

    @Test
    public void testCaptureDuplicationsAlert() {
        sleepSwipingTime();
        sleepSwipingTime();
        sleepSwipingTime();
        navToCapture();
        resumeYourLastSession();
        sleepCameraPreviewSetupTime();
        onView(withId(R.id.editText_capture_examineeId)).perform(replaceText(context.getSomeExamineeId()));
        onView(withText(R.string.examinee_id_duplication)).check(matches(isDisplayed()));
    }
}
