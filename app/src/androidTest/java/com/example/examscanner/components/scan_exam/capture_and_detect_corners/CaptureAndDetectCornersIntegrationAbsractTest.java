package com.example.examscanner.components.scan_exam.capture_and_detect_corners;


import android.Manifest;

import androidx.test.rule.GrantPermissionRule;

import com.example.examscanner.R;
import com.example.examscanner.StateFullTest;
import com.example.examscanner.authentication.AuthenticationHandlerFactory;
import com.example.examscanner.authentication.state.State;
import com.example.examscanner.authentication.state.StateFactory;
import com.example.examscanner.components.scan_exam.capture.CameraManagerStub;
import com.example.examscanner.components.scan_exam.capture.camera.CameraMangerFactory;
import com.example.examscanner.components.scan_exam.detect_corners.RemoteFilesManagerStub;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.persistence.remote.RemoteDatabaseFacadeFactory;
import com.example.examscanner.persistence.remote.files_management.RemoteFilesManagerFactory;
import com.example.examscanner.repositories.corner_detected_capture.CDCRepositoryFacrory;
import com.example.examscanner.stubs.RemoteDatabaseStubInstance;
import com.example.examscanner.use_case_contexts_creators.CornerDetectionContext2Setuper;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import io.reactivex.observers.TestObserver;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.examscanner.ImageProcessorsGenerator.quickIP;
import static com.example.examscanner.Utils.sleepCameraPreviewSetupTime;
import static com.example.examscanner.Utils.sleepMovingFromCaptureToDetectCorners;
import static com.example.examscanner.Utils.sleepSingleCaptureProcessingTime;
import static com.example.examscanner.Utils.sleepSwipingTime;
import static org.hamcrest.Matchers.containsString;


public abstract class CaptureAndDetectCornersIntegrationAbsractTest extends StateFullTest {
    protected CornerDetectionContext2Setuper context;
    @Rule
    public GrantPermissionRule write = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    @Rule
    public GrantPermissionRule rule = GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE);

    @Rule
    public GrantPermissionRule camera = GrantPermissionRule.grant(Manifest.permission.CAMERA);

//    private static final int QAD_NUM_OF_QUESTIONS = 50;
//    private String test_course_name;

    @Before
    @Override
    public void setUp() {
//        dbCallback = db ->{
//            test_course_name = "TEST_course_name";
//            long examCreationSessionId = db.getExamCreationSessionDao().insert(new ExamCreationSession());
//            long eId = db.getExamDao().insert(new Exam(test_course_name,0,"TEST_year","TEST_url",0,examCreationSessionId, null,QAD_NUM_OF_QUESTIONS));
//        };
        if(!USINIG_REAL_DB)RemoteDatabaseFacadeFactory.setStubInstance(new RemoteDatabaseStubInstance());
        RemoteFilesManagerFactory.setStubInstabce(new RemoteFilesManagerStub());
        CameraMangerFactory.setStubInstance(new CameraManagerStub());
        setupCallback = () -> {
            context = getContext();
            context.setup();
        };
        super.setUp();
    }

    @Override
    @After
    public void tearDown()  {
        super.tearDown();
    }

    @NotNull
    protected State getState() {
        State[] ss = new State[1];
        TestObserver<FirebaseAuth> observer = new TestObserver<FirebaseAuth>(){
            @Override
            public void onNext(FirebaseAuth firebaseAuth) {
                StateFactory.get().login(s->ss[0]=s, firebaseAuth);
            }

            @Override
            public void onError(Throwable t) {
                super.onError(t);
            }

            @Override
            public void onSuccess(FirebaseAuth value) {
                super.onSuccess(value);
            }
        };
        AuthenticationHandlerFactory.getTest().authenticate().subscribe(observer);
        observer.awaitCount(1);
        assert ss[0]!=null;
        return ss[0];
    }

    @NotNull
    protected abstract CornerDetectionContext2Setuper getContext();

    public void testWhenTheGraderStartCornerDetectionHeSeesHowManyCapturesThereAreANdWhereIsHe() {
        navToCapture();
        resumeYourLastSession();
        sleepCameraPreviewSetupTime();
        sleepCameraPreviewSetupTime();
        int numOfCaptures = 2;
        for (int i = 0; i < numOfCaptures; i++) {
            captureASolution();
            sleepSingleCaptureProcessingTime();
        }
        onView(withId(R.id.button_move_to_detect_corners)).perform(click());
        sleepMovingFromCaptureToDetectCorners();
        onView(withText("1/2")).check(matches(isDisplayed()));
    }

    protected void resumeYourLastSession() {
//        onView(withText(R.string.home_dialog_yes)).perform().perform(click());
    }


//    public void testTheAppStateStaysUpdatedWhenNavigatingForthAndBackBetweenCornerDetAndCapture() {
//        navToCapture();
//        resumeYourLastSession();
//        sleepCameraPreviewSetupTime();
//        int numOfCaptures = 2;
//        for (int i = 0; i < numOfCaptures; i++) {
//            captureASolution();
//            sleepSingleCaptureProcessingTime();
//        }
//        onView(withId(R.id.button_move_to_detect_corners)).perform(click());
//        sleepMovingFromCaptureToDetectCorners();
//        onView(withContentDescription("Navigate up")).perform(click());
//        sleepCameraPreviewSetupTime();
//        onView(withText("2/2")).check(matches(isDisplayed()));
//    }


//    public void testTheAppStateStaysUpdatedWhenNavigatingForthAndBacAndForthkBetweenCornerDetAndCapture() {
//        navToCapture();
//        resumeYourLastSession();
//        sleepCameraPreviewSetupTime();
//        int numOfCaptures = 2;
//        for (int i = 0; i < numOfCaptures; i++) {
//            captureASolution();
//            sleepSingleCaptureProcessingTime();
//        }
//        onView(withId(R.id.button_move_to_detect_corners)).perform(click());
//        sleepMovingFromCaptureToDetectCorners();
//        onView(withContentDescription("Navigate up")).perform(click());
//        sleepCameraPreviewSetupTime();
//        onView(withId(R.id.button_move_to_detect_corners)).perform(click());
//        sleepMovingFromCaptureToDetectCorners();
//        onView(withText("1/2")).check(matches(isDisplayed()));
//    }


    @Test
    @Ignore("Crashes the program. TODO - FIXXXXX!!!!!")
    public void testProcessedCornerDetectedCapturesNotLeaking() {
        navToCapture();
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(quickIP());
        sleepCameraPreviewSetupTime();
        captureASolution();
        sleepSingleCaptureProcessingTime();
        onView(withId(R.id.button_move_to_detect_corners)).perform(click());
        sleepMovingFromCaptureToDetectCorners();
        onView(withId(R.id.button_cd_approve)).perform(click());
        sleepSwipingTime();
        sleepSwipingTime();
        onView(withContentDescription("Navigate up")).perform(click());
        captureASolution();
        sleepSwipingTime();
        sleepSwipingTime();
        onView(withId(R.id.button_move_to_detect_corners)).perform(click());
        sleepMovingFromCaptureToDetectCorners();
        onView(withId(R.id.button_cd_approve)).perform(click());
        sleepSwipingTime();
        sleepSwipingTime();
    }


    public void testWhenTheGraderStartCornerDetectionHeSeesHowManyCapturesThereAreANdWhereIsHeNoRepoStub() {
        CDCRepositoryFacrory.ONLYFORTESTINGsetTestInstance(null);
        testWhenTheGraderStartCornerDetectionHeSeesHowManyCapturesThereAreANdWhereIsHe();
    }

    @Test
    @Ignore("Relies on camera")
    public void testWhenTheGraderStartCornerDetectionHeSeesHowManyCapturesThereAreANdWhereIsHeNoRepoStubNoCamStub() {
        CDCRepositoryFacrory.ONLYFORTESTINGsetTestInstance(null);
        CameraMangerFactory.setStubInstance(null);
        testWhenTheGraderStartCornerDetectionHeSeesHowManyCapturesThereAreANdWhereIsHe();
    }

    protected void navToCapture() {
//        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
        sleepSwipingTime();
        sleepSwipingTime();
        sleepSwipingTime();
        onView(withText(containsString(context.getTheExam().getCourseName()))).perform(click());
//        onView(withText(R.string.start_scan_exam)).perform(click());

    }

    protected void captureASolution() {
        captureASolution(context.getSomeExamineeId());
    }

    protected void captureASolution(String examineeId) {
        onView(withId(R.id.spinner_capture_version_num)).perform(click());
        onView(withText(Integer.toString(context.getSomeVersion()))).perform(click());
        onView(withId(R.id.editText_capture_examineeId)).perform(replaceText(examineeId));
        onView(withId(R.id.capture_image_button)).perform(click());
    }

    protected void testRetake() {
        navToCapture();
        resumeYourLastSession();
        sleepCameraPreviewSetupTime();
        int numOfCaptures = 3;
        for (int i = 0; i < numOfCaptures; i++) {
            captureASolution();
            sleepSingleCaptureProcessingTime();
            sleepSingleCaptureProcessingTime();
        }
        onView(withId(R.id.button_move_to_detect_corners)).perform(click());
        sleepCameraPreviewSetupTime();
        onView(withId(R.id.button_cd_retake)).perform(click());
        sleepMovingFromCaptureToDetectCorners();
        sleepMovingFromCaptureToDetectCorners();
        onView(withId(R.id.for_testing_fragment_capture_root)).check(matches(isDisplayed()));
        int numOfCaptures2 = 2;
        for (int i = 0; i < numOfCaptures2; i++) {
            captureASolution();
            sleepSingleCaptureProcessingTime();
            sleepSingleCaptureProcessingTime();
        }
        sleepCameraPreviewSetupTime();
        onView(withId(R.id.button_move_to_detect_corners)).perform(click());
        sleepMovingFromCaptureToDetectCorners();
        onView(withText(String.format("%d/%d", 1, numOfCaptures + numOfCaptures2 - 1))).check(matches(isDisplayed()));
    }

    protected void testFinishBatch() {
        navToCapture();
        resumeYourLastSession();
        sleepCameraPreviewSetupTime();
        int numOfCaptures = 2;
        for (int i = 0; i < numOfCaptures; i++) {
            captureASolution();
            sleepSingleCaptureProcessingTime();
        }
        onView(withId(R.id.button_move_to_detect_corners)).perform(click());
        for (int i = 0; i < numOfCaptures; i++) {
            sleepSwipingTime();
            sleepSwipingTime();
            onView(withId(R.id.button_cd_approve)).perform(click());
            sleepSwipingTime();
        }
        onView(withText(R.string.done_batch)).check(matches(isDisplayed()));
    }

    protected void testFinishBatchAndToHome() {
        navToCapture();
        resumeYourLastSession();
        sleepCameraPreviewSetupTime();
        int numOfCaptures = 1;
        for (int i = 0; i < numOfCaptures; i++) {
            captureASolution();
            sleepSingleCaptureProcessingTime();
        }
        onView(withId(R.id.button_move_to_detect_corners)).perform(click());
        for (int i = 0; i < numOfCaptures; i++) {
            sleepSwipingTime();
            sleepSwipingTime();
            onView(withId(R.id.button_cd_approve)).perform(click());
            sleepSwipingTime();
        }
        sleepSwipingTime();
        onView(withText(R.string.stage_back_to_home)).perform(click());
        onView(withId(R.id.exams_recycler_view)).check(matches(isDisplayed()));
    }

    protected void testFinishBatchAndToCapture() {
        navToCapture();
        resumeYourLastSession();
        sleepCameraPreviewSetupTime();
        int numOfCaptures = 1;
        for (int i = 0; i < numOfCaptures; i++) {
            captureASolution();
            sleepSingleCaptureProcessingTime();
        }
        onView(withId(R.id.button_move_to_detect_corners)).perform(click());
        for (int i = 0; i < numOfCaptures; i++) {
            sleepSwipingTime();
            sleepSwipingTime();
            onView(withId(R.id.button_cd_approve)).perform(click());
            sleepSwipingTime();
        }
        sleepSwipingTime();
        onView(withText(R.string.stage_start_another_batch)).perform(click());
        onView(withId(R.id.for_testing_fragment_capture_root)).check(matches(isDisplayed()));
    }


    protected void testTake5() {
        navToCapture();
        resumeYourLastSession();
        sleepCameraPreviewSetupTime();
        int numOfCaptures = 5;
        for (int i = 0; i < numOfCaptures; i++) {
            captureASolution();
            sleepSingleCaptureProcessingTime();
        }
    }

    protected void test3Caps() {
        navToCapture();
        resumeYourLastSession();
        sleepCameraPreviewSetupTime();
        int numOfCaptures = 3;
        for (int i = 0; i < numOfCaptures; i++) {
            captureASolution();
            sleepSingleCaptureProcessingTime();
            sleepSingleCaptureProcessingTime();
        }
        onView(withId(R.id.button_move_to_detect_corners)).perform(click());
        sleepCameraPreviewSetupTime();
        onView(withText(String.format("%d/%d", 1, 3))).check(matches(isDisplayed()));
    }

    protected void test1FailCap(){
        navToCapture();
        resumeYourLastSession();
        sleepCameraPreviewSetupTime();
        int numOfCaptures = 1;
        for (int i = 0; i < numOfCaptures; i++) {
            captureASolution();
            sleepSingleCaptureProcessingTime();
            sleepSingleCaptureProcessingTime();
        }
        onView(withId(R.id.button_move_to_detect_corners)).perform(click());
        sleepCameraPreviewSetupTime();
        onView(withText(String.format("%d/%d", 1, numOfCaptures))).check(matches(isDisplayed()));
    }

    protected void testQuickRetake() {
        navToCapture();
        resumeYourLastSession();
        sleepCameraPreviewSetupTime();
        int numOfCaptures = 1;
        for (int i = 0; i < numOfCaptures; i++) {
            captureASolution();
            sleepSingleCaptureProcessingTime();
        }
        onView(withId(R.id.button_move_to_detect_corners)).perform(click());
        sleepCameraPreviewSetupTime();
        onView(withId(R.id.button_cd_retake)).perform(click());
        sleepMovingFromCaptureToDetectCorners();
        sleepMovingFromCaptureToDetectCorners();
        onView(withId(R.id.for_testing_fragment_capture_root)).check(matches(isDisplayed()));
        int numOfCaptures2 = 2;
        for (int i = 0; i < numOfCaptures2; i++) {
            captureASolution();
            sleepSingleCaptureProcessingTime();
        }
        sleepCameraPreviewSetupTime();
        onView(withId(R.id.button_move_to_detect_corners)).perform(click());
        sleepMovingFromCaptureToDetectCorners();
        onView(withText(String.format("%d/%d", 1, numOfCaptures + numOfCaptures2 - 1))).check(matches(isDisplayed()));
    }
}
