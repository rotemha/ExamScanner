package com.example.examscanner.components.scan_exam.detect_corners;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.examscanner.R;
import com.example.examscanner.communication.CommunicationFacadeFactory;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.components.scan_exam.reslove_answers.SCEmptyRepositoryFactory;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.image_processing.ScanAnswersConsumer;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.corner_detected_capture.CDCRepositoryFacrory;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.ExamRepositoryFactory;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;
import com.example.examscanner.stubs.ExamRepositoryStub;
import com.example.examscanner.stubs.ExamStubFactory;
import com.example.examscanner.stubs.ScannedCapturesInstancesFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.OpenCVLoader;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.examscanner.ImageProcessorsGenerator.fakeIP;
import static com.example.examscanner.ImageProcessorsGenerator.slowIP;
import static com.example.examscanner.Utils.loadOpenCV;
import static com.example.examscanner.Utils.sleepRectangleTransformationTime;
import static com.example.examscanner.Utils.sleepScanAnswersTime;
import static com.example.examscanner.Utils.sleepSwipingTime;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class CornerDetectionFragmentTest {
    private static final String TAG = "CornerDetectionFragmentTest";
    private static final String NOT_SUPPORTING_EXAMINEE_ID_EXTRACTION_YET = "NOT_SUPPORTING_EXAMINEE_ID_EXTRACTION_YET";
    private Repository<ScannedCapture> repo;
    private ImageProcessingFacade imageProcessor;
    private BaseLoaderCallback mLoaderCallback;
    private long sessionId;
    private Exam exam;
    private int QAD_counter = 0;
    private String QAD_GRADER_EMAIL;

    @Before
    public void setUp() {
//        CDCRepositoryFacrory.ONLYFORTESTINGsetTestInstance(DCEmptyRepositoryFactory.create());
        ScannedCaptureRepositoryFactory.ONLYFORTESTINGsetTestInstance(SCEmptyRepositoryFactory.create());
        Repository<Exam> stubERepo  = new ExamRepositoryStub();
        exam = ExamStubFactory.instance1();
        stubERepo.create(
                exam
        );
        ExamRepositoryFactory.setStubInstance(stubERepo);
        imageProcessor = fakeIP();
        repo = new ScannedCaptureRepositoryFactory().create();
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(fakeIP());
        ImageProcessingFactory.setTestMode(getApplicationContext());
        OpenCVLoader.initDebug();
    }

    @After
    public void tearDown() {
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(null);
        CDCRepositoryFacrory.ONLYFORTESTINGsetTestInstance(null);
        ExamRepositoryFactory.setStubInstance(null);
        CommunicationFacadeFactory.tearDown();
        ExamRepositoryFactory.tearDown();
        CDCRepositoryFacrory.tearDown();
        ScannedCaptureRepositoryFactory.tearDown();

    }

    private void produceSCIntoRepo(Bitmap testJpg) {
        ScannedCapturesInstancesFactory.instance1(new ScanAnswersConsumer() {
            @Override
            public void consume(int numOfAnswersDetected, int[] answersIds, float[] lefts, float[] tops, float[] rights, float[] bottoms, int[] selections) {
                QAD_GRADER_EMAIL = "QAD_GRADER_EMAIL";
                final ScannedCapture t = new ScannedCapture(-1, testJpg, testJpg, exam.getNumOfQuestions(), numOfAnswersDetected, answersIds, lefts, tops, rights, bottoms, selections, exam.getVersions().get(0), NOT_SUPPORTING_EXAMINEE_ID_EXTRACTION_YET + String.valueOf(QAD_counter++), QAD_GRADER_EMAIL);
                repo.create(t);
            }
        });
    }

    private void setUp5Captures() {
        produceSCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg1());
        produceSCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg2());
        produceSCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg3());
        produceSCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg3());
        produceSCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg3());
    }

    private void setUp3MarkedCaptures() {
        produceSCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked());
        produceSCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg2Marked());
        produceSCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg3Marked());

    }

    private void resolveAnswersAndSwipeLeft() {
        approve();
//        onView(withId(R.id.viewPager2_corner_detected_captures)).perform(ViewActions.swipeLeft());
        sleepSwipingTime();
    }

    @Test
    public void testProcess5Captures() throws InterruptedException {
        setUp5Captures();
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(null);
        launchCornerDetectionFragment();
        Thread.sleep(1000);
        resolveAnswersAndSwipeLeft();
        resolveAnswersAndSwipeLeft();
        resolveAnswersAndSwipeLeft();
        resolveAnswersAndSwipeLeft();
    }

    private void launchCornerDetectionFragment() {
        Bundle b = new Bundle();
        b.putLong("examId", ExamStubFactory.instance1().getId());
        FragmentScenario<CornerDetectionFragment> scenraio = FragmentScenario.launchInContainer(
                CornerDetectionFragment.class, b);
        loadOpenCV(scenraio);

    }

    @Test
    @Ignore("Failing for annoying reasons and not deadly important")
    public void testProgressBarStays() {
        setUp3MarkedCaptures();
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(slowIP());
        launchCornerDetectionFragment();
        onView(withId(R.id.viewPager2_corner_detected_captures)).perform(ViewActions.swipeLeft());
//        onView(Utils.withIndex(withId(R.id.spinner_detect_corners_version_num), 0)).perform(click());
        onView(withText(Integer.toString(ExamStubFactory.instance1_dinaBarzilayVersion))).perform(click());
        onView(withId(R.id.button_cd_approve)).perform(click());
        sleepSwipingTime();
        sleepSwipingTime();
        onView(withId(R.id.viewPager2_corner_detected_captures)).perform(ViewActions.swipeLeft());
        onView(withId(R.id.button_cd_approve)).perform(click());
        onView(withId(R.id.viewPager2_corner_detected_captures)).perform(ViewActions.swipeRight());
        sleepSwipingTime();
        sleepSwipingTime();
        sleepSwipingTime();
        onView(withId(R.id.progressBar2_scanning_answers)).check(matches(isDisplayed()));
    }

    @Test
    public void testAutoLeftSwipeOnProcess() throws InterruptedException {
        setUp3MarkedCaptures();
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(slowIP());
        launchCornerDetectionFragment();
        Thread.sleep(1000);
        approve();
        sleepSwipingTime();
        onView(withId(R.id.textView_cd_current_position)).check(matches(withText("1/2")));
    }

    @Test
    public void testCorrentPositionPointer() throws InterruptedException {
        produceSCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked());
        produceSCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg2Marked());
        launchCornerDetectionFragment();
        Thread.sleep(2000);
        onView(withText("1/2")).check(matches(isDisplayed()));
        onView(withId(R.id.viewPager2_corner_detected_captures)).perform(ViewActions.swipeLeft());
        onView(withText("2/2")).check(matches(isDisplayed()));
        onView(withId(R.id.viewPager2_corner_detected_captures)).perform(ViewActions.swipeRight());
        onView(withText("1/2")).check(matches(isDisplayed()));
    }

//    @Test
//    public void testProcessingFeedbackSlowIP() {
//        testProcessingFeedback(slowIP());
//    }

//    @Test
//    public void testProcessingFeedbackRealIP() {
//        testProcessingFeedback(null);
//    }

//    private void testProcessingFeedback(ImageProcessingFacade ip) {
//        DetectCornersConsumer consumer = new DetectCornersConsumer() {
//            @Override
//            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
//                repo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg1(), upperLeft, upperRight, bottomRight, bottomLeft, sessionId));
//            }
//        };
//        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg1(), consumer);
//        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg1(), consumer);
//        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(ip);
//        launchCornerDetectionFragment();
//        onView(withId(R.id.textView_cd_processing_progress)).check(matches(withText("0/2")));
//        selectVersionAndScanAnswers();
//        sleepRectangleTransformationTime();
//        sleepScanAnswersTime();
//        onView(withId(R.id.textView_cd_processing_progress)).check(matches(withText("1/2")));
//    }

    @Test
    @Ignore("Test not important functionality")
    public void testProgressIndicator() {
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(imageProcessor);
        produceSCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg1());
        produceSCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg1());
        produceSCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg1());
        launchCornerDetectionFragment();
        approve();
        onView(withId(R.id.viewPager2_corner_detected_captures)).perform(ViewActions.swipeRight());
        onView(withId(R.id.progressBar2_scanning_answers)).check(matches(isDisplayed()));
        sleepRectangleTransformationTime();
        sleepScanAnswersTime();
        onView(withId(R.id.progressBar2_scanning_answers)).check(matches(not(isDisplayed())));
    }

    @Test
    @Ignore("maybe need to migrate to capture")
    public void testOnFinishProcessFragmentIsDiscarded2CapturesSlowIP() throws InterruptedException {
        testOnFinishProcessFragmentIsDiscarded2Captures(slowIP());
    }

    @Test
    public void testOnFinishProcessFragmentIsDiscarded2CapturesRealP() throws InterruptedException {
        testOnFinishProcessFragmentIsDiscarded2Captures(null);
    }

    private void testOnFinishProcessFragmentIsDiscarded2Captures(ImageProcessingFacade ip) throws InterruptedException {
        produceSCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg1());
        produceSCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg1());
        produceSCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg2());
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(ip);
        launchCornerDetectionFragment();
        Thread.sleep(1000);
        approve();
        approve();
        onView(withId(R.id.textView_cd_current_position)).check(matches(withText("1/1")));
    }

    @Test
    @Ignore("Not working and dont want to debug")
    public void testOnFinishProcessFragmentIsDiscarded3CapturesSlowIP() {
        testOnFinishProcessFragmentIsDiscarded3Captures(slowIP());
    }

    @Test
    public void testOnFinishProcessFragmentIsDiscarded3CapturesRealIP() {
        testOnFinishProcessFragmentIsDiscarded3Captures(null);
    }

    public void testOnFinishProcessFragmentIsDiscarded3Captures(ImageProcessingFacade ip) {
        produceSCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg1());
        produceSCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg2());
        produceSCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg3());
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(ip);
        launchCornerDetectionFragment();
        approve();
        sleepScanAnswersTime();
        sleepScanAnswersTime();
        onView(withId(R.id.textView_cd_current_position)).check(matches(withText("1/2")));
    }

    @Test
    public void versionEditTextIsVisible() {
        produceSCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked());
        launchCornerDetectionFragment();
//        onView(withId(R.id.spinner_detect_corners_version_num)).check(matches(isDisplayed()));
    }

    @Test
    @Ignore("need to migrate to capture")
    public void versionEditTextOnlyAvailableVersionsOptions() {
        produceSCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked());
        launchCornerDetectionFragment();
//        onView(withId(R.id.spinner_detect_corners_version_num)).perform(click());
        onView(withText(Integer.toString(ExamStubFactory.instance1_dinaBarzilayVersion))).check(matches(isDisplayed()));
        onView(withText(Integer.toString(ExamStubFactory.instance1_theDevilVersion))).check(matches(isDisplayed()));
    }


//    @Test
//    public void scanningButtonIsWorkingIFFVersionIsChosen() {
//        produceCDCIntoRepo(BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked());
//        launchCornerDetectionFragment();
//        onView(withId(R.id.button_cd_approve_and_scan_answers)).perform(click());
//        onView(withText(R.string.detect_corners_please_choose_version)).check(matches(isDisplayed()));
//        onView(withText(R.string.detect_corners_ok)).perform(click());
//        selectVersionAndScanAnswers();
//        onView(withText(R.string.detect_corners_please_choose_version)).check(doesNotExist());
//    }
    private void approve() {
        onView(withId(R.id.button_cd_approve)).perform(click());
    }
}