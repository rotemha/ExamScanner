package com.example.examscanner.components.scan_exam.reslove_answers;

import android.graphics.PointF;

import com.example.examscanner.R;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.StateFullTest;
import com.example.examscanner.Utils;
import com.example.examscanner.components.scan_exam.capture.CameraManagerStub;
import com.example.examscanner.components.scan_exam.capture.camera.CameraMangerFactory;
import com.example.examscanner.components.scan_exam.detect_corners.RemoteFilesManagerStub;
import com.example.examscanner.image_processing.DetectCornersConsumer;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.persistence.local.entities.Exam;
import com.example.examscanner.persistence.local.entities.ExamCreationSession;
import com.example.examscanner.persistence.local.files_management.FilesManagerFactory;
import com.example.examscanner.persistence.remote.files_management.RemoteFilesManagerFactory;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.corner_detected_capture.CDCRepositoryFacrory;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCapture;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;
import com.example.examscanner.repositories.session.SESessionProviderFactory;
import com.example.examscanner.stubs.FilesManagerStub;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.examscanner.ImageProcessorsGenerator.nullIP;
import static org.hamcrest.Matchers.containsString;

@Ignore("NOT USING")
public class ResolveAnswersANdResolveConflictsTest extends StateFullTest {
    private static final int QAD_NUM_OF_QUESTIONS = 50;
    private final int dinaBarzilayVersionNumber = 496351;
    private ImageProcessingFacade imageProcessor;
    private Repository<ScannedCapture> repo;
    private Repository<CornerDetectedCapture> cdcRepo;
    private String theTestExamCourseName = "TEST_courseName";
    private long examId;
    private long scanExamSession;
    private int theDevilVersionNumber = 666;

    @Before
    @Override
    public void setUp() {
        FilesManagerFactory.setStubInstance(new FilesManagerStub());
        dbCallback = db ->{
            long creationId = db.getExamCreationSessionDao().insert(new ExamCreationSession());
            examId = db.getExamDao().insert(new Exam(theTestExamCourseName,0,"2020","url",0,creationId, null,QAD_NUM_OF_QUESTIONS, null, new String[0], 0,2, true));
            db.getVersionDao().insert(new com.example.examscanner.persistence.local.entities.Version(dinaBarzilayVersionNumber, examId, null, false, true, true));
            db.getVersionDao().insert(new com.example.examscanner.persistence.local.entities.Version(theDevilVersionNumber, examId, null, false, true, true));
        };
        super.setUp();
        RemoteFilesManagerFactory.setStubInstabce(new RemoteFilesManagerStub());
        scanExamSession =new SESessionProviderFactory().create().provide(examId);
        cdcRepo = new CDCRepositoryFacrory().create();
        ScannedCaptureRepositoryFactory.ONLYFORTESTINGsetTestInstance(SCEmptyRepositoryFactory.create());
        imageProcessor = nullIP();
        repo = new ScannedCaptureRepositoryFactory().create();
        repo.create(ScannedCapturesInstancesFactory.instance1(repo));
        repo.create(ScannedCapturesInstancesFactory.instance1(repo));
        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg1(), new DetectCornersConsumer() {
            @Override
            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                cdcRepo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked(), upperLeft, upperRight, bottomRight, bottomLeft, scanExamSession));
            }
        });
        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg2(), new DetectCornersConsumer() {
            @Override
            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                cdcRepo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg2Marked(), upperLeft, upperRight, bottomRight, bottomLeft, scanExamSession));
            }
        });
        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg3(), new DetectCornersConsumer() {
            @Override
            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                cdcRepo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg3Marked(), upperLeft, upperRight, bottomRight, bottomLeft, scanExamSession));
            }
        });
        CameraMangerFactory.setStubInstance(new CameraManagerStub());
        navFromHomeToDetecteCornersUnderTestExam();
    }
    private void navFromHomeToDetecteCornersUnderTestExam() {
        onView(withText(containsString(theTestExamCourseName))).perform(click());
        Utils.sleepAlertPoppingTime();
        onView(withText(R.string.home_dialog_yes)).perform().perform(click());
        onView(withId(R.id.button_move_to_detect_corners)).perform(click());
//        onView(withId(R.id.spinner_detect_corners_version_num)).perform(click());
        Utils.sleepSwipingTime();
        onView(withText(Integer.toString(dinaBarzilayVersionNumber))).perform(click());
        onView(withId(R.id.button_cd_approve)).perform(click());
        Utils.sleepScanAnswersTime();
//        onView(withId(R.id.button_cd_nav_to_resolve_answers)).perform(click());
    }

    @Test
    public void testConflictAndCheckedAMountUpdatesUponResolution() {
        onView(withText("Resolve")).perform(click());
        resolveAndSwipe("4");
        Utils.sleepSwipingTime();
        resolveAndSwipe("5");
        Utils.sleepSwipingTime();
        resolveAndSwipe("No Answer");
        Utils.sleepSwipingTime();
        resolveAndSwipe("No Answer");
        Utils.sleepSwipingTime();
        onView(withContentDescription("Navigate up")).perform(click());
        onView(withText("Checked: 38")).check(matches(isDisplayed()));
        onView(withText("Conflicted: 0")).check(matches(isDisplayed()));
        onView(withText("Missing: 15")).check(matches(isDisplayed()));
    }

    private void resolveAndSwipe(String s) {
        onView(withText(s)).perform(click());

    }
}
