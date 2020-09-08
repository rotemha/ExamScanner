package com.example.examscanner.components.create_exam.view_model;

import android.graphics.Bitmap;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.examscanner.authentication.AuthenticationHandlerFactory;
import com.example.examscanner.authentication.state.StateFactory;
import com.example.examscanner.authentication.state.StateHolder;
import com.example.examscanner.communication.CommunicationFacadeFactory;
import com.example.examscanner.components.create_exam.CreateExamModelView;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.image_processing.ImageProcessor;
import com.example.examscanner.image_processing.ScanAnswersConsumer;
import com.example.examscanner.persistence.local.AppDatabaseFactory;
import com.example.examscanner.persistence.remote.RemoteDatabaseFacadeFactory;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.ExamRepositoryFactory;
import com.example.examscanner.repositories.exam.Version;
import com.example.examscanner.repositories.grader.GraderRepoFactory;
import com.example.examscanner.stubs.ImageProcessorStub;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.android.OpenCVLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public abstract class CreateExamModelViewAbstractTest {

    private CreateExamModelView out;
    private Repository<Exam> examRepository;
    private ImageProcessingFacade realIP;

    @Before
    public void setUp() throws Exception {
        OpenCVLoader.initDebug();
        AppDatabaseFactory.setTestMode();
        TestObserver to = new TestObserver(){
            @Override
            public void onNext(Object value) {
                StateFactory.get().login(StateHolder.getDefaultHolder(), value);
            }
        };
        AuthenticationHandlerFactory.getTest().authenticate().subscribe(to);
        to.awaitCount(1);
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(new ImageProcessor(getApplicationContext()));
        realIP = new ImageProcessingFactory().create();
        out  = new CreateExamModelView(
                new ExamRepositoryFactory().create(),
                new GraderRepoFactory().create(),
                realIP,
                StateFactory.get(),
                0
        );
        examRepository = new ExamRepositoryFactory().create();
    }

    @After
    public void tearDown() throws Exception {
        AppDatabaseFactory.tearDownDb();
        RemoteDatabaseFacadeFactory.tearDown();
        ExamRepositoryFactory.tearDown();
        CommunicationFacadeFactory.tearDown();
        GraderRepoFactory.tearDown();
    }

    @Test
    public void testAddVersion() {
//        final int[] expectedNumOfAnswers = new int[1];
//        realIP.scanAnswers(null, new ScanAnswersConsumer() {
//            @Override
//            public void consume(int numOfAnswersDetected, int[] answersIds, float[] lefts, float[] tops, float[] rights, float[] bottoms, int[] selections) {
//                expectedNumOfAnswers[0] = numOfAnswersDetected;
//                for (int i = 0; i <selections.length ; i++) {
//                    if (selections[i]==-1)
//                        expectedNumOfAnswers[0]--;
//                }
//            }
//        });
        final int expectedNumOfQuestions = 50;
        out.holdVersionBitmap(BitmapsInstancesFactoryAndroidTest.getTestJpg1());
        out.holdVersionNumber(3);
        out.holdNumOfQuestions(String.valueOf(expectedNumOfQuestions));
        out.addVersion();
        out.holdGraderIdentifier("bobexamscanner80@gmail.com");
        out.addGrader();
        out.holdExamUrl("/d/asdasd");
        out.create("testAddVersion()_courseName","A","Fall","2020");
        List<Exam> exams = examRepository.get((e)->true);
        assertEquals(exams.size(),1);
        Exam theExam = exams.get(0);
        assertTrue(theExam.getVersions().size()==1);
        final Version version = theExam.getVersions().get(0);
        assertEquals(expectedNumOfQuestions,version.getQuestions().size());
    }
    @Test
    public void testAddVersion2RealIP(){
        out.holdNumOfQuestions("50");
        int numOfQuestions = out.getExam().getNumOfQuestions();
        Bitmap bm = BitmapsInstancesFactoryAndroidTest.getExam50Qs();
//        realIP.scanAnswers(bm, numOfQuestions, new ScanAnswersConsumer() {
//            @Override
//            public void consume(int numOfAnswersDetected, int[] answersIds, float[] lefts, float[] tops, float[] rights, float[] bottoms, int[] selections) {
//                expectedNumOfAnswers[0] = numOfAnswersDetected;
//                for (int i = 0; i <selections.length ; i++) {
//                    if (selections[i]==-1)
//                        expectedNumOfAnswers[0]--;
//                }
//            }
//        });
        out.holdVersionBitmap(BitmapsInstancesFactoryAndroidTest.getExam50Qs());
        out.holdVersionNumber(3);
        out.addVersion();
        out.holdExamUrl("/d/urlurk");
        out.create("testAddVersion()_courseName","A","Fall","2020");
        List<Exam> exams = examRepository.get((e)->true);
        assertEquals(exams.size(),1);
        Exam theExam = exams.get(0);
        assertTrue(theExam.getVersions().size()==1);
        final Version version = theExam.getVersions().get(0);
        ArrayList<Integer> realAnswers = new ArrayList<Integer>(
                Arrays.asList(4, 3, 4, 3, 5, 1 ,5, 2, 1, 3, 4, 1, 5, 2, 4, 1, 5, 5, 1, 2, 1 ,2, 1, 4, 3,
                        4, 3, 4, 3, 2, 2, 4, 2, 3 ,2 ,2 ,1 ,1, 2 ,1 ,1, 4, 1 ,3, 4 ,5, 4 ,3 ,5 ,2));
        assertEquals(50 ,version.getQuestions().size());
        for(int i =0 ; i < version.getQuestions().size(); i++){
            assertTrue(realAnswers.get(i) == version.getQuestionByNumber(i+1).getAns());
        }
    }
}