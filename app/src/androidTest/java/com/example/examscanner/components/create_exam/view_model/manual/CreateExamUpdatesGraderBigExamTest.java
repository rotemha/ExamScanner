package com.example.examscanner.components.create_exam.view_model.manual;

import android.graphics.Bitmap;

import androidx.core.util.Pair;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.examscanner.authentication.AuthenticationHandlerFactory;
import com.example.examscanner.authentication.state.StateFactory;
import com.example.examscanner.authentication.state.StateHolder;
import com.example.examscanner.communication.CommunicationFacadeFactory;
import com.example.examscanner.components.create_exam.CreateExamModelView;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.persistence.local.AppDatabaseFactory;
import com.example.examscanner.persistence.local.files_management.FilesManagerFactory;
import com.example.examscanner.persistence.remote.FirebaseDatabaseFactory;
import com.example.examscanner.persistence.remote.RemoteDatabaseFacade;
import com.example.examscanner.persistence.remote.RemoteDatabaseFacadeFactory;
import com.example.examscanner.persistence.remote.files_management.RemoteFilesManagerFactory;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.ExamRepositoryFactory;
import com.example.examscanner.repositories.grader.Grader;
import com.example.examscanner.repositories.grader.GraderRepoFactory;
import com.example.examscanner.stubs.ImageProcessorStub;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.observers.TestObserver;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static com.example.examscanner.components.create_exam.CreateExamFragmentAbstractTestStateFull.BOB_ID;
import static com.example.examscanner.components.scan_exam.AbstractComponentInstrumentedTest.USINIG_REAL_DB;
import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class CreateExamUpdatesGraderBigExamTest {
    private CreateExamModelView out;
    private Repository<Exam> examRepository;
    private Repository<Grader> graderRepository;
    private ImageProcessingFacade imageProcessor;
    private Exam theExpectedExam;
    private RemoteDatabaseFacade remoteDatabaseFacade;
    private final TestObserver to = new TestObserver(){
        @Override
        public void onNext(Object value) {
            StateFactory.get().login(StateHolder.getDefaultHolder(), value);
        }
    };

    private interface BitmapLambda{
        Bitmap get();
    }

    @Before
    public void setUp() throws Exception {
        USINIG_REAL_DB = true;
        if(!USINIG_REAL_DB)FirebaseDatabaseFactory.setTestMode();
        AppDatabaseFactory.setTestMode();
        FilesManagerFactory.setTestMode(getApplicationContext());
//        RemoteFilesManagerFactory.setTestMode();
        AuthenticationHandlerFactory.getTest().authenticate().subscribe(to);
        remoteDatabaseFacade = RemoteDatabaseFacadeFactory.get();
        remoteDatabaseFacade.addGraderIfAbsent("rotemhas@post.bgu.ac.il","5etmuMvCN3aY9aUXWa8rOX0CZXj1");
        remoteDatabaseFacade.addGraderIfAbsent("rotemb271@gmail.com","n5BDz7ckJpT45JP772xnAVhQtWn2");
        sleep(2000);
        to.awaitCount(1);
        ImageProcessingFactory.setTestMode(getApplicationContext());
        imageProcessor = new ImageProcessingFactory().create();
        out  = new CreateExamModelView(
                new ExamRepositoryFactory().create(),
                new GraderRepoFactory().create(),
                imageProcessor,
                StateFactory.get(),
                0
        );
        examRepository = new ExamRepositoryFactory().create();
        graderRepository = new GraderRepoFactory().create();
        graderRepository.create(new Grader("bobexamscanner80@gmail.com", BOB_ID));
        out.holdGraderIdentifier("rotemhas@post.bgu.ac.il");
        out.addGrader();
        out.holdGraderIdentifier("rotemb271@gmail.com");
        out.addGrader();
        out.holdNumOfQuestions("80");
        List<Pair<Integer, BitmapLambda>> versions = new ArrayList<>(
                Arrays.asList(
                        new Pair<>(1,()->BitmapsInstancesFactoryAndroidTest.exmp0_ver1()),
                        new Pair<>(2,()->BitmapsInstancesFactoryAndroidTest.exmp0_ver2()),
                        new Pair<>(3,()->BitmapsInstancesFactoryAndroidTest.exmp0_ver3()),
                        new Pair<>(4,()->BitmapsInstancesFactoryAndroidTest.exmp0_ver4()),
                        new Pair<>(5,()->BitmapsInstancesFactoryAndroidTest.exmp0_ver5()),
                        new Pair<>(6,()->BitmapsInstancesFactoryAndroidTest.exmp0_ver6()),
                        new Pair<>(7,()->BitmapsInstancesFactoryAndroidTest.exmp0_ver7()),
                        new Pair<>(8,()->BitmapsInstancesFactoryAndroidTest.exmp0_ver8())
                )
        );
        for (Pair<Integer, BitmapLambda> pair:versions) {
            out.holdVersionNumber(pair.first);
            out.holdVersionBitmap(pair.second.get());
            out.addVersion();
        }
        out.holdExamUrl("https://docs.google.com/spreadsheets/d/16qSAOL4RxBzbfvtIkuB8NHWlGWcRCoBLP6vp9Vyk970/edit?usp=sharing");
        out.create("CreateExamUpdatesGraderTest_courseName","A","Fall","2020");
        List<Exam> exams = examRepository.get((e)->true);
        assert 1 == exams.size();
        theExpectedExam = exams.get(0);
    }

    @After
    public void tearDown() throws Exception {
        AppDatabaseFactory.tearDownDb();
        RemoteDatabaseFacadeFactory.tearDown();
        ExamRepositoryFactory.tearDown();
        FilesManagerFactory.tearDown();
        CommunicationFacadeFactory.tearDown();
        GraderRepoFactory.tearDown();
    }

    @Test
    public void createExamUpdatesGraderTestBreakPoint() {
        int x =1;
    }
    @Test
    public void createExamUpdatesGraderTestLookOnBob() {
        theExpectedExam.quziEagerLoad();
//        theExpectedExam.dontResoveFutures();
//        tearDown();
        AppDatabaseFactory.tearDownDb();
        ExamRepositoryFactory.tearDown();
        CommunicationFacadeFactory.tearDown();
        GraderRepoFactory.tearDown();
        AuthenticationHandlerFactory.getTest().authenticate("bobexamscanner80@gmail.com", "Ycombinator").subscribe(to);
        imageProcessor = new ImageProcessorStub();
        final List<Exam> exams = examRepository.get(e -> true);
        assertEquals(1,exams.size());
        Exam theActualExam = exams.get(0);
        assertTrue(theActualExam.getVersions().size()>0);
        assertEquals(theExpectedExam, theActualExam);
    }

    @Test
    public void createExamUpdatesGraderGetsBitmap() {
        List<Exam> exams = null;
        try {
            exams = examRepository.get(e -> true);
        }catch (Exception e){
            throw  e;
//            e.printStackTrace();
//            fail("Probably no such file exception");
        }
        Bitmap expected = theExpectedExam.getVersions().get(0).getPerfectImage();
        Bitmap actual = exams.get(0).getVersions().get(0).getPerfectImage();

        assertTrue(expected.sameAs(actual));
    }
}
