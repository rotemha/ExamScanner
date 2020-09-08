package com.example.examscanner.components.scan_exam.detect_corners.integration_with_remote_db;

import android.Manifest;

import androidx.test.rule.GrantPermissionRule;

import com.example.examscanner.R;
import com.example.examscanner.StateFullTest;
import com.example.examscanner.Utils;
import com.example.examscanner.authentication.AuthenticationHandlerFactory;
import com.example.examscanner.authentication.state.State;
import com.example.examscanner.authentication.state.StateFactory;
import com.example.examscanner.authentication.state.StateHolder;
import com.example.examscanner.components.scan_exam.AbstractComponentInstrumentedTest;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.components.scan_exam.capture.CameraManagerStub;
import com.example.examscanner.components.scan_exam.capture.camera.CameraMangerFactory;
import com.example.examscanner.components.scan_exam.detect_corners.RemoteFilesManagerStub;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.persistence.remote.files_management.RemoteFilesManagerFactory;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;
import com.example.examscanner.use_case_contexts_creators.CornerDetectionContext2Setuper;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.reactivex.observers.TestObserver;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.examscanner.ImageProcessorsGenerator.halfFakeIP;
import static org.hamcrest.Matchers.containsString;



public class ResolveConflictsTest extends StateFullTest {
    private static final int QAD_NUM_OF_QUESTIONS = 50;
    private ImageProcessingFacade imageProcessor;
    private Repository<ScannedCapture> repo;
//    private Repository<CornerDetectedCapture> cdcRepo;
    private long examId;
    private long scanExamSession;
    private int theDevilVersionNumber = 666;
    private com.example.examscanner.repositories.exam.Exam exam;
    private CornerDetectionContext2Setuper context;
    private String currentUserId;

    @Rule
    public GrantPermissionRule write = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    @Rule
    public GrantPermissionRule rule = GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE);

    @Rule
    public GrantPermissionRule camera = GrantPermissionRule.grant(Manifest.permission.CAMERA);

    @Before
    @Override
    public void setUp() {
        AbstractComponentInstrumentedTest.USINIG_REAL_DB = true;
//        AppDatabaseFactory.setTestMode();
        RemoteFilesManagerFactory.setStubInstabce(new RemoteFilesManagerStub());
        CameraMangerFactory.setStubInstance(new CameraManagerStub());
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(halfFakeIP(getApplicationContext()));
        CornerDetectionContext2Setuper.setIOStub(new ImageProcessingFactory().create());
        setupCallback = () -> {
            context = getContext();
            context.setup();
        };
        super.setUp();

//        repo = new ScannedCaptureRepositoryFactory().create();
//        repo.create(ScannedCapturesInstancesFactory.instance1(repo));
//        repo.create(ScannedCapturesInstancesFactory.instance1(repo));
//        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg1(), new DetectCornersConsumer() {
//            @Override
//            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
//                cdcRepo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked(), upperLeft, upperRight, bottomRight, bottomLeft, scanExamSession));
//            }
//        });
//        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg2(), new DetectCornersConsumer() {
//            @Override
//            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
//                cdcRepo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg2Marked(), upperLeft, upperRight, bottomRight, bottomLeft, scanExamSession));
//            }
//        });
//        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg3(), new DetectCornersConsumer() {
//            @Override
//            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
//                cdcRepo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg3Marked(), upperLeft, upperRight, bottomRight, bottomLeft, scanExamSession));
//            }
//        });
        navFromHomeToDetecteCornersUnderTestExam();
    }

    @Override
    @After
    public void tearDown(){
        context.tearDown();
        super.tearDown();
        AbstractComponentInstrumentedTest.USINIG_REAL_DB = false;
        CornerDetectionContext2Setuper.stubExamRepo=true;
    }

    @NotNull
    private CornerDetectionContext2Setuper getContext() {
        State[] ss = new State[1];
        StateHolder sHolder = new StateHolder() {
            @Override
            public void setState(State s) {
                ss[0] = s;
            }
        };
//        FirebaseDatabaseFactory.setTestMode();
        TestObserver<FirebaseAuth> observer = new TestObserver<FirebaseAuth>(){
            @Override
            public void onNext(FirebaseAuth firebaseAuth) {
                StateFactory.get().login(sHolder, firebaseAuth);
            }
        };
        AuthenticationHandlerFactory.getTest().authenticate().subscribe(observer);
        observer.awaitCount(1);
        final CornerDetectionContext2Setuper context2Setuper = new CornerDetectionContext2Setuper(ss[0], BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_1());
        context2Setuper.setSCRepo(new ScannedCaptureRepositoryFactory().create());
        CornerDetectionContext2Setuper.stubExamRepo=false;
        return context2Setuper;
    }
    private void navFromHomeToDetecteCornersUnderTestExam() {
        Utils.sleepSwipingTime();
        Utils.sleepSwipingTime();
        Utils.sleepSwipingTime();
        Utils.sleepSwipingTime();
        onView(withText(containsString(context.getTheExam().getCourseName()))).perform(click());
        Utils.sleepAlertPoppingTime();
        captureASolution();
        Utils.sleepScanAnswersTime();
        Utils.sleepScanAnswersTime();
//        onView(withId(R.id.spinner_detect_corners_version_num)).perform(click());
//        onView(withId(R.id.button_cd_nav_to_resolve_answers)).perform(click());
    }
    private void captureASolution() {
        onView(withId(R.id.spinner_capture_version_num)).perform(click());
        onView(withText(Integer.toString(context.getSomeVersion()))).perform(click());
        onView(withId(R.id.editText_capture_examineeId)).perform(replaceText(context.getSomeExamineeId()));
        onView(withId(R.id.capture_image_button)).perform(click());
    }

    @Test
    public void testConflictAndCheckedAMountUpdatesUponResolution() {
        onView(withId(R.id.button_move_to_detect_corners)).perform(click());
        Utils.sleepScanAnswersTime();
        Utils.sleepScanAnswersTime();
        onView(withText("Resolve")).perform(click());
        resolveAndSwipe("4");
        Utils.sleepSwipingTime();
        resolveAndSwipe("5");
        Utils.sleepSwipingTime();
    }

    @Test
    public void testConflictAndCheckedAMountUpdatesUponResolution2() {
        captureASolution();
        Utils.sleepScanAnswersTime();
        Utils.sleepScanAnswersTime();
        onView(withId(R.id.button_move_to_detect_corners)).perform(click());
        onView(withText("Resolve")).perform(click());
        resolveAndSwipe("4");
        Utils.sleepSwipingTime();
        resolveAndSwipe("5");
        Utils.sleepSwipingTime();
    }

    private void resolveAndSwipe(String s) {
        onView(withText(s)).perform(click());

    }
}
