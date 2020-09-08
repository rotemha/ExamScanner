package com.example.examscanner.components.scan_exam.detect_corners_and_resolve_ans;

import android.view.View;

import androidx.test.espresso.PerformException;

import com.example.examscanner.R;
import com.example.examscanner.StateFullTest;
import com.example.examscanner.Utils;
import com.example.examscanner.components.scan_exam.detect_corners.RemoteFilesManagerStub;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.persistence.remote.files_management.RemoteFilesManagerFactory;
import com.example.examscanner.use_case_contexts_creators.CornerDetectionContext1Setuper;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Ignore;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


@Ignore("Not using")
public abstract class DetectCornersAndResolveAnswersAbstractTest extends StateFullTest {
    protected CornerDetectionContext1Setuper usecaseContext;
    protected abstract CornerDetectionContext1Setuper createContext();
    protected abstract void navFromHomeToDetecteCornersUnderTestExam();

    @Override
    @Before
    public void setUp() {
        setupCallback = ()->{
            usecaseContext = createContext();
            usecaseContext.setup();
        };
        RemoteFilesManagerFactory.setStubInstabce(new RemoteFilesManagerStub());
        super.setUp();
        navFromHomeToDetecteCornersUnderTestExam();
    }
    private void selectVersionAndScanAnswers() {
        clickOnTheVersionSpinner();
        onView(withText(Integer.toString(usecaseContext.getDinaBarzilayVersion()))).perform(click());
        onView(withId(R.id.button_cd_approve)).perform(click());
    }

    @Override
    public void tearDown(){
        usecaseContext.tearDown();
        super.tearDown();
    }

    static Matcher<View> withTag(final Object tag) {
        return new TypeSafeMatcher<View>() {

            @Override
            public void describeTo(final Description description) {
                description.appendText("has tag equals to: " + tag);
            }

            @Override
            protected boolean matchesSafely(final View view) {
                Object viewTag = view.getTag();
                if (viewTag == null) {
                    return tag == null;
                }

                return viewTag.equals(tag);
            }
        };
    }

    private void clickOnTheVersionSpinner() {
        try {
//            onView(Utils.withIndex(withId(R.id.spinner_detect_corners_version_num), 0)).perform(click());
        } catch (PerformException e){
//            onView(Utils.withIndex(withId(R.id.spinner_detect_corners_version_num), 1)).perform(click());
        }
    }

//    public void testNotCrashProcessedCornerDetectedCapturesConsistentBetweenFragmentsBackToCornerDetectionPosition(ImageProcessingFacade ip) {
//        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(ip);
//        Utils.sleepCDFragmentSetupTime();
//        onView(withText(R.string.detect_corners_the_empty_version_choice)).perform(click());
//        onView(withText(Integer.toString(usecaseContext.getDinaBarzilayVersion()))).perform(click());
//        onView(withId(R.id.button_cd_approve)).perform(click());
//        Utils.sleepSwipingTime();
////        clickOnTheVersionSpinner();
////        onView(withText(Integer.toString(usecaseContext.getTheDevilVersion()))).perform(click());
////        onView(withId(R.id.button_cd_approve_and_scan_answers)).perform(click());
////        Utils.sleepScanAnswersTime();
//        onView(withId(R.id.button_cd_nav_to_resolve_answers)).perform(click());
//        onView(withContentDescription("Navigate up")).perform(click());
//        Utils.sleepScanAnswersTime();
//        onView(withId(R.id.textView_cd_current_position)).check(matches(withText("1/1")));
//    }


//    @Ignore
//    public void testProcessedCornerDetectedCapturesConsistentBetweenFragmentsBackToCornerDetectionPosition() {
//        Utils.sleepCDFragmentSetupTime();
//        selectVersionAndScanAnswers();
//        onView(withId(R.id.viewPager2_corner_detected_captures)).perform(swipeLeft());
//        selectVersionAndScanAnswers();
//        onView(withId(R.id.button_cd_nav_to_resolve_answers)).perform(click());
//        onView(withContentDescription("Navigate up")).perform(click());
//        Utils.sleepScanAnswersTime();
//        Utils.sleepScanAnswersTime();
//        Utils.sleepScanAnswersTime();
//        onView(withId(R.id.textView_cd_current_position)).check(matches(withText("1/1")));
//    }


//    public void testProcessedCornerDetectedCapturesConsistentBetweenFragments() {
//        selectVersionAndScanAnswers();
//        Utils.sleepCDFragmentSetupTime();
//        selectVersionAndScanAnswers();
//        Utils.sleepScanAnswersTime();
//        Utils.sleepScanAnswersTime();
//        onView(withId(R.id.button_cd_nav_to_resolve_answers)).perform(click());
//        onView(withId(R.id.textView_ra_progress_feedback)).check(matches(withText("1/2")));
//    }


//    public void testProcessedCornerDetectedCapturesConsistentBetweenFragmentsBackToCornerDetectionProgress() {
//        Utils.sleepCDFragmentSetupTime();
//        selectVersionAndScanAnswers();
//        onView(withId(R.id.viewPager2_corner_detected_captures)).perform(swipeLeft());
//        Utils.sleepSwipingTime();
//        selectVersionAndScanAnswers();
//        Utils.sleepSwipingTime();
//        Utils.sleepScanAnswersTime();
//        Utils.sleepScanAnswersTime();
//        onView(withId(R.id.button_cd_nav_to_resolve_answers)).perform(click());
//        Utils.sleepSwipingTime();
//        onView(withContentDescription("Navigate up")).perform(click());
//        onView(withId(R.id.textView_cd_processing_progress)).check(matches(withText("2/3")));
//    }
}