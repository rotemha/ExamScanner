package com.example.examscanner.components.admin;

import android.Manifest;
import android.util.Log;

import androidx.room.Room;
import androidx.test.rule.GrantPermissionRule;

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
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import io.reactivex.observers.TestObserver;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static com.example.examscanner.components.create_exam.CreateExamFragmentAbstractTestStateFull.BOB_ID;
import static com.example.examscanner.components.scan_exam.AbstractComponentInstrumentedTest.USINIG_REAL_DB;
import static java.lang.Thread.sleep;
import static org.junit.Assert.*;

public class AdminViewModelTest {
    private final String DEBUG_TAG = "DebugExamScanner";
    private AdminViewModel out;
    private CreateExamModelView createExamModelView;
    private Repository<Exam> examRepository;
    private Repository<Grader> graderRepository;
    private ImageProcessingFacade imageProcessor;
    private RemoteDatabaseFacade remoteDatabaseFacade;
    private Exam theExpectedExam;
    private AppDatabase aliceDB;
    private AppDatabase bobDB;
    private static boolean WITH_REAL_REMOTE_DB =false;


    @Rule
    public GrantPermissionRule write = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    @Rule
    public GrantPermissionRule rule = GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE);


    @Before
    public void setUp() throws Exception {
        try {
            if(!AdminViewModelTest.WITH_REAL_REMOTE_DB)FirebaseDatabaseFactory.setTestMode();
            aliceDB = Room.inMemoryDatabaseBuilder(getApplicationContext(), AppDatabase.class).build();
            AppDatabaseFactory.setTestInstance(aliceDB);
            FilesManagerFactory.setTestMode(getApplicationContext());
            if(!AdminViewModelTest.WITH_REAL_REMOTE_DB)RemoteFilesManagerFactory.setTestMode();
            RemoteDatabaseFacadeFactory.tearDown();
            remoteDatabaseFacade = RemoteDatabaseFacadeFactory.get();
            remoteDatabaseFacade.addGraderIfAbsent("bobexamscanner80@gmail.com","QR6JunUJDvaZr1kSOWEq3iiCToQ2");
            remoteDatabaseFacade.addGraderIfAbsent("aliceexamscanner80@gmail.com","vtXTnHcehjdRmBvg38r5S0K1R8p1");
            sleep(2000);
            TestObserver to = new TestObserver(){
                @Override
                public void onNext(Object value) {
                    Log.d(DEBUG_TAG, "alice logged in");
                    StateFactory.get().login(StateFactory.getStateHolder(), value);
                }
            };
            AuthenticationHandlerFactory.getAliceTest().authenticate().subscribe(to);
            to.awaitCount(1);
            ImageProcessingFactory.setTestMode(getApplicationContext());
            imageProcessor = new ImageProcessingFactory().create();
            createExamModelView  = new CreateExamModelView(
                    new ExamRepositoryFactory().create(),
                    new GraderRepoFactory().create(),
                    imageProcessor,
                    StateFactory.get(),
                    0
            );
            examRepository = new ExamRepositoryFactory().create();
            graderRepository = new GraderRepoFactory().create();
            graderRepository.create(new Grader("bobexamscanner80@gmail.com", BOB_ID));
            createExamModelView.holdVersionBitmap(BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_in1());
            createExamModelView.holdVersionNumber(3);
            createExamModelView.holdGraderIdentifier("bobexamscanner80@gmail.com");
            createExamModelView.addGrader();
            createExamModelView.holdNumOfQuestions("50");
            createExamModelView.addVersion();
            createExamModelView.holdExamUrl("/durlurl");
            createExamModelView.create("CreateExamUpdatesGraderTest_courseName","A","Fall","2020");
            sleep(7*1000);
            List<Exam> exams = examRepository.get((e)->true);
            assert 1 == exams.size();
            theExpectedExam = exams.get(0);
            theExpectedExam.quziEagerLoad();
//        theExpectedExam.dontResoveFutures();
//        tearDown();
//        AppDatabaseFactory.tearDownDb();
            ExamRepositoryFactory.tearDown();
            CommunicationFacadeFactory.tearDown();
            GraderRepoFactory.tearDown();
            StateFactory.get().logout(StateFactory.getStateHolder());
            sleep(2000);
            to = new TestObserver(){
                @Override
                public void onNext(Object value) {
                    Log.d(DEBUG_TAG, "bob logged in");
                    StateFactory.get().login(StateFactory.getStateHolder(), value);
                }
            };
            AuthenticationHandlerFactory.getBobTest().authenticate().subscribe(to);
            bobDB = Room.inMemoryDatabaseBuilder(getApplicationContext(), AppDatabase.class).build();
            AppDatabaseFactory.setTestInstance(bobDB);
            imageProcessor = new ImageProcessorStub();
            examRepository = new ExamRepositoryFactory().create();
            sleep(1000);
            final List<Exam> exams2 = examRepository.get(e -> true);
            assert 1==exams2.size();
            StateFactory.get().logout(StateHolder.getDefaultHolder());
//        AppDatabaseFactory.tearDownDb();
            ExamRepositoryFactory.tearDown();
            CommunicationFacadeFactory.tearDown();
            GraderRepoFactory.tearDown();
            AppDatabaseFactory.setTestInstance(aliceDB);
//        TestObserver to = new TestObserver(){
//            @Override
//            public void onNext(Object value) {
//                StateFactory.get().login(StateHolder.getDefaultHolder(), value);
//            }
//        };
            to = new TestObserver(){
                @Override
                public void onNext(Object value) {
                    Log.d(DEBUG_TAG, "alice logged in again");
                    StateFactory.get().login(StateFactory.getStateHolder(), value);
                }
            };
            AuthenticationHandlerFactory.getAliceTest().authenticate().subscribe(to);
            examRepository = new ExamRepositoryFactory().create();
            out = new AdminViewModel(
                    examRepository,
                    examRepository.get(x->true).get(0)
            );
            out.delete();
            ExamRepositoryFactory.tearDown();
            CommunicationFacadeFactory.tearDown();
            GraderRepoFactory.tearDown();
        }catch (Throwable e){
            tearDown();
        }
    }
    @After
    public void tearDown() throws Exception {
        AppDatabaseFactory.tearDownDb();
        RemoteDatabaseFacadeFactory.tearDown();
        ExamRepositoryFactory.tearDown();
        FilesManagerFactory.tearDown();
        CommunicationFacadeFactory.tearDown();
        GraderRepoFactory.tearDown();
        StateFactory.tearDown();
        USINIG_REAL_DB =false;
    }

    @Test
    public void testAfterDeletionExamDisappear() {
        TestObserver to = new TestObserver(){
            @Override
            public void onNext(Object value) {
                Log.d(DEBUG_TAG, "bob logged in again");
                StateFactory.get().login(StateHolder.getDefaultHolder(), value);
            }
        };
        AuthenticationHandlerFactory.getBobTest().authenticate().subscribe(to);
        AppDatabaseFactory.setTestInstance(bobDB);
        imageProcessor = new ImageProcessorStub();
        examRepository = new ExamRepositoryFactory().create();
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final List<Exam> exams = examRepository.get(e -> true);
        assertEquals(0, exams.size());
    }
}