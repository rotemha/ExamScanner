package com.example.examscanner.components.scan_exam.detect_corners_and_resolve_ans.ip_integration;

import android.graphics.PointF;

import com.example.examscanner.R;
import com.example.examscanner.Utils;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.components.scan_exam.detect_corners.DCEmptyRepositoryFactory;
import com.example.examscanner.components.scan_exam.detect_corners.RemoteFilesManagerStub;
import com.example.examscanner.components.scan_exam.detect_corners_and_resolve_ans.DetectCornersAndResolveAnswersAbstractTest;
import com.example.examscanner.components.scan_exam.reslove_answers.SCEmptyRepositoryFactory;
import com.example.examscanner.image_processing.DetectCornersConsumer;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.persistence.remote.RemoteDatabaseFacadeFactory;
import com.example.examscanner.persistence.remote.files_management.RemoteFilesManagerFactory;
import com.example.examscanner.repositories.corner_detected_capture.CDCRepositoryFacrory;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCapture;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.ExamRepositoryFactory;
import com.example.examscanner.repositories.exam.ExamineeIdsSocket;
import com.example.examscanner.repositories.exam.Version;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;
import com.example.examscanner.repositories.session.SESessionProviderFactory;
import com.example.examscanner.stubs.RemoteDatabaseStubInstance;
import com.example.examscanner.use_case_contexts_creators.CornerDetectionContext1Setuper;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;


public class DetectCornersAndResolveAnswersTest extends DetectCornersAndResolveAnswersAbstractTest {

    private static final int QAD_NUM_OF_QUESTIONS = 50;

    @Before
    @Override
    public void setUp() {
//        RemoteDatabaseFacadeFactory.setStubInstance(new RemoteDatabaseStubInstance());
        super.setUp();
    }

    @Override
    protected CornerDetectionContext1Setuper createContext() {
        return new CornerDetectionContext1Setuper(){
            @Override
            public void setup() {
                e = new Exam(null,-1,Exam.theEmptyFutureVersionsList(),new ArrayList<>(),"theTestExamCourseName",0,0,0,"2020",QAD_NUM_OF_QUESTIONS, 0, ExamineeIdsSocket.getEmpty(),true, Exam.DownloadCompletable.getEmpty(),Exam.DownloadCompletable.getEmpty());
                examRepository = new ExamRepositoryFactory().create();
                dinaBarzilayVersion = 496351;
                e.addVersion(new Version(-1, dinaBarzilayVersion,Version.toFuture(e),Version.theEmptyFutureQuestionsList(), BitmapsInstancesFactoryAndroidTest.getTestJpg1()));
                theDevilVersion = 666;
                e.addVersion(new Version(-1, theDevilVersion,Version.toFuture(e),Version.theEmptyFutureQuestionsList(), BitmapsInstancesFactoryAndroidTest.getTestJpg1()));
                examRepository.create(e);
                CDCRepositoryFacrory.ONLYFORTESTINGsetTestInstance(DCEmptyRepositoryFactory.create());
                scRepo = SCEmptyRepositoryFactory.create();
                ScannedCaptureRepositoryFactory.ONLYFORTESTINGsetTestInstance(scRepo);
                imageProcessor = new ImageProcessingFactory().create();
                cdcRepo = new CDCRepositoryFacrory().create();
                scanExamSession = new SESessionProviderFactory().create().provide(e.getId());
                imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestAuthPic1Marked(), new DetectCornersConsumer() {
                    @Override
                    public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                        cdcRepo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestAuthPic1Marked(), upperLeft, upperRight,bottomRight,bottomLeft, scanExamSession));
                    }
                });
                imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestAuthPic1Marked(), new DetectCornersConsumer() {
                    @Override
                    public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                        cdcRepo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestAuthPic1Marked(), upperLeft, upperRight,bottomRight,bottomLeft, scanExamSession));
                    }
                });
            }
        };
    }

    protected void navFromHomeToDetecteCornersUnderTestExam() {
        onView(withText(containsString(usecaseContext.getTheExam().getCourseName()))).perform(click());
        Utils.sleepAlertPoppingTime();
        onView(withText(R.string.home_dialog_yes)).perform().perform(click());
        onView(withId(R.id.button_move_to_detect_corners)).perform(click());
    }


//    @Test
//    @Ignore("Need to fix. Not seem terribly usefull")
//    public void testNotCrashProcessedCornerDetectedCapturesConsistentBetweenFragmentsBackToCornerDetectionPositionRealIP() {
//        testNotCrashProcessedCornerDetectedCapturesConsistentBetweenFragmentsBackToCornerDetectionPosition(null);
//    }

}