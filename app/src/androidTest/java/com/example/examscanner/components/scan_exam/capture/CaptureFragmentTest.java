package com.example.examscanner.components.scan_exam.capture;


import android.Manifest;
import android.os.Bundle;
import android.os.RemoteException;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;

import com.example.examscanner.R;
import com.example.examscanner.Utils;
import com.example.examscanner.components.scan_exam.capture.camera.CameraMangerFactory;
import com.example.examscanner.components.scan_exam.detect_corners.DCEmptyRepositoryFactory;
import com.example.examscanner.components.scan_exam.reslove_answers.SCEmptyRepositoryFactory;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.persistence.local.AppDatabaseFactory;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.corner_detected_capture.CDCRepositoryFacrory;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.ExamRepositoryFactory;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepository;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;
import com.example.examscanner.stubs.ExamRepositoryStub;
import com.example.examscanner.stubs.ExamStubFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.example.examscanner.ImageProcessorsGenerator.fakeIP;
import static com.example.examscanner.ImageProcessorsGenerator.nullIP;
import static com.example.examscanner.Utils.sleepCameraPreviewSetupTime;
import static com.example.examscanner.Utils.sleepSingleCaptureProcessingTime;
import static com.example.examscanner.Utils.sleepSingleCaptureTakingTime;
import static com.example.examscanner.components.scan_exam.capture.CaptureUtils.assertUserSeeProgress;

@RunWith(AndroidJUnit4.class)
public class CaptureFragmentTest{
    private FragmentScenario<CaptureFragment> scenario;
    private UiDevice device;
    private Bundle b;

    @Rule
    public GrantPermissionRule write = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    @Rule
    public GrantPermissionRule rule = GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE);

    @Rule
    public GrantPermissionRule camera = GrantPermissionRule.grant(Manifest.permission.CAMERA);

    @Before
    public void setUp() {
//        CDCRepositoryFacrory.ONLYFORTESTINGsetTestInstance(DCEmptyRepositoryFactory.create());
//        AppDatabaseFactory.setTestMode();
        ScannedCaptureRepositoryFactory.ONLYFORTESTINGsetTestInstance(SCEmptyRepositoryFactory.create());
        CameraMangerFactory.setStubInstance(new CameraManagerStub());
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(fakeIP());
        b = new Bundle();
        Repository<Exam> stubERepo  = new ExamRepositoryStub();
        Exam e = ExamStubFactory.instance1();
        stubERepo.create(
                e
        );
        ExamRepositoryFactory.setStubInstance(stubERepo);
        b.putLong("examId", e.getId());
        b.putLong("sessionId", 0);
    }

    @After
    public void tearDown() throws Exception {
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(null);
        ScannedCaptureRepositoryFactory.ONLYFORTESTINGsetTestInstance(null);
        ExamRepositoryFactory.setStubInstance(null);
        CameraMangerFactory.setStubInstance(null);
        ExamRepositoryFactory.tearDown();
//        AppDatabaseFactory.tearDownDb();

    }

    private void captureASolution() {
        onView(withId(R.id.spinner_capture_version_num)).perform(click());
        onView(withText(Integer.toString(ExamStubFactory.instance1_dinaBarzilayVersion))).perform(click());
        onView(withId(R.id.editText_capture_examineeId)).perform(replaceText("123456"));
        onView(withId(R.id.capture_image_button)).perform(click());
    }

    private void theNumberOfUnprocessedCapturesUpdates() {
        captureASolution();
        sleepSingleCaptureTakingTime();
        try {
            assertUserSeeProgress(0,1);
        }catch (NoMatchingViewException e){
            assertUserSeeProgress(1,1);
        }
    }

    @Test
    public void testTheNumberOfUnprocessedCapturesUpdates() {
        scenario =FragmentScenario.launchInContainer(CaptureFragment.class, b);
        sleepCameraPreviewSetupTime();
        theNumberOfUnprocessedCapturesUpdates();
    }
    @Test
    public void testTheNumberOfUnprocessedCapturesUpdatesRealCamera() {
        scenario =FragmentScenario.launchInContainer(CaptureFragment.class, b);
        sleepCameraPreviewSetupTime();
        CameraMangerFactory.setStubInstance(null);
        theNumberOfUnprocessedCapturesUpdates();
    }

    @Test
    @Ignore("Maybe shpuld pass")
    public void testTheNumberOfUnprocessedCapturesUpdatesRealIP() {
        ImageProcessingFactory.setTestMode(getInstrumentation().getContext());
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(null);
        scenario =FragmentScenario.launchInContainer(CaptureFragment.class, b);
        sleepCameraPreviewSetupTime();
        sleepCameraPreviewSetupTime();
        sleepCameraPreviewSetupTime();
        theNumberOfUnprocessedCapturesUpdates();
    }

    @Test
    public void testTheNumberOfProcessedAndUnprocessedCapturesUpdates() {
        scenario =FragmentScenario.launchInContainer(CaptureFragment.class, b);
        sleepCameraPreviewSetupTime();
        sleepCameraPreviewSetupTime();
        captureASolution();
        sleepSingleCaptureProcessingTime();
        assertUserSeeProgress(1,1);
    }

    @Test
    public void testTheNumberOfProcessedAndUnprocessedCapturesUpdatesRealCamera() {
        scenario =FragmentScenario.launchInContainer(CaptureFragment.class, b);
        sleepCameraPreviewSetupTime();
        CameraMangerFactory.setStubInstance(null);
        sleepCameraPreviewSetupTime();
        captureASolution();
        sleepSingleCaptureProcessingTime();
        assertUserSeeProgress(1,1);
    }

    @Test
    @Ignore("TODO - fix")
    public void testDataSurvivesRotation() {
        device = UiDevice.getInstance(getInstrumentation());
        sleepCameraPreviewSetupTime();
        captureASolution();
        sleepSingleCaptureProcessingTime();
        try {
            device.setOrientationLeft();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            device.setOrientationNatural();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        assertUserSeeProgress(1, 1);
    }


}