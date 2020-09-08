package com.example.examscanner.components.create_exam.view_model.integration_with_remote_db;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.examscanner.authentication.AuthenticationHandlerFactory;
import com.example.examscanner.authentication.state.StateFactory;
import com.example.examscanner.authentication.state.StateHolder;
import com.example.examscanner.communication.CommunicationFacadeFactory;
import com.example.examscanner.components.create_exam.CreateExamModelView;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.persistence.local.AppDatabase;
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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.observers.TestObserver;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static com.example.examscanner.components.create_exam.CreateExamFragmentAbstractTestStateFull.BOB_ID;
import static com.example.examscanner.components.scan_exam.AbstractComponentInstrumentedTest.USINIG_REAL_DB;
import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class CreateExamUpdatesGraderTest {
    private CreateExamModelView out;
    private Repository<Exam> examRepository;
    private Repository<Grader> graderRepository;
    private ImageProcessingFacade imageProcessor;
    private Exam theExpectedExam;
    private RemoteDatabaseFacade remoteDatabaseFacade;
    private AppDatabase esDB;
    private AppDatabase bobDB;
    private String DEBUG_TAG;

    @Before
    public void setUp() throws Exception {
        USINIG_REAL_DB = false;
        if(!USINIG_REAL_DB)FirebaseDatabaseFactory.setTestMode();
        esDB = Room.inMemoryDatabaseBuilder(getApplicationContext(), AppDatabase.class).build();
        AppDatabaseFactory.setTestInstance(esDB);
        FilesManagerFactory.setTestMode(getApplicationContext());
        if(!USINIG_REAL_DB)RemoteFilesManagerFactory.setTestMode();
        TestObserver to = new TestObserver(){
            @Override
            public void onNext(Object value) {
                StateFactory.get().login(StateHolder.getDefaultHolder(), value);
            }
        };
        AuthenticationHandlerFactory.getTest().authenticate().subscribe(to);
        RemoteDatabaseFacadeFactory.tearDown();
        remoteDatabaseFacade = RemoteDatabaseFacadeFactory.get();
        remoteDatabaseFacade.addGraderIfAbsent("bobexamscanner80@gmail.com","QR6JunUJDvaZr1kSOWEq3iiCToQ2");
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
        out.holdVersionBitmap(BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_in1());
        out.holdVersionNumber(3);
        out.holdGraderIdentifier("bobexamscanner80@gmail.com");
        out.addGrader();
        out.holdNumOfQuestions("50");
        out.addVersion();
        out.holdExamUrl("/durlurl");
        out.create("CreateExamUpdatesGraderTest_courseName","A","Fall","2020");
        List<Exam> exams = examRepository.get((e)->true);
        assert 1 == exams.size();
        theExpectedExam = exams.get(0);
        theExpectedExam.quziEagerLoad();
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        theExpectedExam.dontResoveFutures();
//        tearDown();
        AppDatabaseFactory.tearDownDb();
        ExamRepositoryFactory.tearDown();
        CommunicationFacadeFactory.tearDown();
        GraderRepoFactory.tearDown();
        AuthenticationHandlerFactory.getTest().authenticate("bobexamscanner80@gmail.com", "Ycombinator").subscribe(to);
        imageProcessor = new ImageProcessorStub();
        bobDB = Room.inMemoryDatabaseBuilder(getApplicationContext(), AppDatabase.class).build();
        AppDatabaseFactory.setTestInstance(bobDB);
        imageProcessor = new ImageProcessorStub();
        examRepository = new ExamRepositoryFactory().create();
        sleep(1000);
    }

    @After
    public void tearDown() throws Exception {
        esDB.clearAllTables();
        bobDB.clearAllTables();
        RemoteDatabaseFacadeFactory.tearDown();
        ExamRepositoryFactory.tearDown();
        FilesManagerFactory.tearDown();
        CommunicationFacadeFactory.tearDown();
        GraderRepoFactory.tearDown();
    }

    @Test
    public void createExamUpdatesGraderTest() throws InterruptedException {
        final List<Exam> exams = examRepository.get(e -> true);
        assertEquals(1,exams.size());
        Exam theActualExam = exams.get(0);
        TestObserver testObserver = new TestObserver(){
            @Override
            public void onComplete() {
                super.onComplete();
                assertTrue(theActualExam.isDownloaded());
                assertTrue(theActualExam.getVersions().size()>0);
                assertEquals(theExpectedExam, theActualExam);
            }

            @Override
            public void onError(Throwable t) {
                DEBUG_TAG = "DebugExamScanner";
                Log.d(DEBUG_TAG,"failed",t);
                Assert.fail();
            }
        };
        Completable comp =theActualExam.observeDownload();
        comp.subscribe(testObserver);
        testObserver.awaitCount(1);
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
        List<Exam> finalExams = exams;
        TestObserver testObserver = new TestObserver(){
            @Override
            public void onComplete() {
                Bitmap expected = theExpectedExam.getVersions().get(0).getPerfectImage();
                Bitmap actual = finalExams.get(0).getVersions().get(0).getPerfectImage();
                assertTrue(expected.sameAs(actual));
                super.onComplete();
            }

            @Override
            public void onError(Throwable t) {
                DEBUG_TAG = "DebugExamScanner";
                Log.d(DEBUG_TAG,"failed",t);
                Assert.fail();
            }
        };
        Completable comp =exams.get(0).observeDownload();
        comp.subscribe(testObserver);
        testObserver.awaitCount(1);



    }
}
