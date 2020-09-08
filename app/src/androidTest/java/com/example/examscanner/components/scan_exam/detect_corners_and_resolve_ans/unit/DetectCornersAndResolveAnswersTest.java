package com.example.examscanner.components.scan_exam.detect_corners_and_resolve_ans.unit;

import com.example.examscanner.R;
import com.example.examscanner.Utils;
import com.example.examscanner.components.scan_exam.detect_corners_and_resolve_ans.DetectCornersAndResolveAnswersAbstractTest;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.persistence.remote.RemoteDatabaseFacadeFactory;
import com.example.examscanner.stubs.RemoteDatabaseStubInstance;
import com.example.examscanner.use_case_contexts_creators.CornerDetectionContext1Setuper;

import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.examscanner.ImageProcessorsGenerator.fakeIP;
import static com.example.examscanner.ImageProcessorsGenerator.slowIP;
import static org.hamcrest.Matchers.containsString;


public class DetectCornersAndResolveAnswersTest extends DetectCornersAndResolveAnswersAbstractTest {

    @Override
    @Before
    public void setUp() {
        RemoteDatabaseFacadeFactory.setStubInstance(new RemoteDatabaseStubInstance());
        super.setUp();
    }

    @Override
    protected CornerDetectionContext1Setuper createContext() {
        return new CornerDetectionContext1Setuper();
    }

    @Override
    protected void navFromHomeToDetecteCornersUnderTestExam() {
        onView(withText(containsString(usecaseContext.getTheExam().getCourseName()))).perform(click());
        Utils.sleepAlertPoppingTime();
        onView(withText(R.string.home_dialog_yes)).perform().perform(click());
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(fakeIP());
        onView(withId(R.id.button_move_to_detect_corners)).perform(click());
    }

//    @Test
//    public void testNotCrashProcessedCornerDetectedCapturesConsistentBetweenFragmentsBackToCornerDetectionPositionSlowIP() {
//        testNotCrashProcessedCornerDetectedCapturesConsistentBetweenFragmentsBackToCornerDetectionPosition(slowIP());
//    }

}