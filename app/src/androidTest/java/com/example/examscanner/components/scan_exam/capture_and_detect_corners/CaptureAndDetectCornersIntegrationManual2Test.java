package com.example.examscanner.components.scan_exam.capture_and_detect_corners;


import android.view.View;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.examscanner.R;
import com.example.examscanner.StateFullTest;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.components.scan_exam.capture.camera.CameraManager;
import com.example.examscanner.components.scan_exam.capture.camera.CameraMangerFactory;
import com.example.examscanner.components.scan_exam.capture.camera.CameraOutputHander;
import com.example.examscanner.components.scan_exam.detect_corners.DCEmptyRepositoryFactory;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.persistence.local.entities.Exam;
import com.example.examscanner.persistence.local.entities.ExamCreationSession;
import com.example.examscanner.repositories.corner_detected_capture.CDCRepositoryFacrory;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.examscanner.Utils.sleepCameraPreviewSetupTime;
import static com.example.examscanner.Utils.sleepMovingFromCaptureToDetectCorners;
import static com.example.examscanner.Utils.sleepSingleCaptureProcessingTime;
import static org.hamcrest.Matchers.containsString;


@Ignore("Ment to be run manually")
@RunWith(AndroidJUnit4.class)
public class CaptureAndDetectCornersIntegrationManual2Test extends StateFullTest {

    private static final int QAD_NUM_OF_QUESTIONS = 50;
    private ImageProcessingFacade imageProcessor;
    private String test_course_name = "Test_Course_Name";
    @Before
    @Override
    public void setUp() {
        dbCallback = db ->{
            test_course_name = "TEST_course_name";
            long examCreationSessionId = db.getExamCreationSessionDao().insert(new ExamCreationSession());
            long eId = db.getExamDao().insert(new Exam(test_course_name,0,"TEST_year","TEST_url",0,examCreationSessionId, null,QAD_NUM_OF_QUESTIONS, null, new String[0], 0,-1,true));
        };
        super.setUp();
//        CameraMangerFactory.setStubInstance(new CameraManagerStub());
        CDCRepositoryFacrory.ONLYFORTESTINGsetTestInstance(DCEmptyRepositoryFactory.create());
    }


    @Test
    public void testDetectRealCorners() {
        CameraMangerFactory.setStubInstance(new CameraManager() {
            @Override
            public void setUp() {
            }

            @Override
            public View.OnClickListener createCaptureClickListener(CameraOutputHander handler) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handler.handleBitmap(BitmapsInstancesFactoryAndroidTest.getTestJpgBlackBack());
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
        navToCapture();
        sleepCameraPreviewSetupTime();
        sleepCameraPreviewSetupTime();
        onView(withId(R.id.capture_image_button)).perform(click());
        sleepSingleCaptureProcessingTime();
        onView(withId(R.id.button_move_to_detect_corners)).perform(click());
        sleepMovingFromCaptureToDetectCorners();
        onView(withText("1/2")).check(matches(isDisplayed()));
    }


    @Test
    public void testDetectSimpleImg() {
        CameraMangerFactory.setStubInstance(new CameraManager() {
            @Override
            public void setUp() {
            }

            @Override
            public View.OnClickListener createCaptureClickListener(CameraOutputHander handler) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handler.handleBitmap(BitmapsInstancesFactoryAndroidTest.getTestJpgReal2());
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
        navToCapture();
        sleepCameraPreviewSetupTime();
        sleepCameraPreviewSetupTime();
        onView(withId(R.id.capture_image_button)).perform(click());
        sleepSingleCaptureProcessingTime();
        onView(withId(R.id.button_move_to_detect_corners)).perform(click());
        sleepMovingFromCaptureToDetectCorners();
        onView(withText("1/2")).check(matches(isDisplayed()));
    }

    @Test
    public void testDetectDiagonalImg() {
        CameraMangerFactory.setStubInstance(new CameraManager() {
            @Override
            public void setUp() {
            }

            @Override
            public View.OnClickListener createCaptureClickListener(CameraOutputHander handler) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handler.handleBitmap(BitmapsInstancesFactoryAndroidTest.getTestJpgDiagonal1());
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
        navToCapture();
        sleepCameraPreviewSetupTime();
        sleepCameraPreviewSetupTime();
        onView(withId(R.id.capture_image_button)).perform(click());
        sleepSingleCaptureProcessingTime();
        onView(withId(R.id.button_move_to_detect_corners)).perform(click());
        sleepMovingFromCaptureToDetectCorners();
        onView(withText("1/2")).check(matches(isDisplayed()));
    }



    private void navToCapture() {
        onView(withText(containsString(test_course_name))).perform(click());
    }
}
