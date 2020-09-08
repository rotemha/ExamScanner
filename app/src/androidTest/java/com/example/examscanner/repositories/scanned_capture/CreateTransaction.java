package com.example.examscanner.repositories.scanned_capture;

import android.Manifest;

import androidx.test.rule.GrantPermissionRule;

import com.example.examscanner.authentication.AuthenticationHandlerFactory;
import com.example.examscanner.authentication.state.State;
import com.example.examscanner.authentication.state.StateFactory;
import com.example.examscanner.communication.CommunicationFacadeFactory;
import com.example.examscanner.communication.ContextProvider;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.image_processing.ScanAnswersConsumer;
import com.example.examscanner.persistence.local.AppDatabaseFactory;
import com.example.examscanner.persistence.local.files_management.FilesManagerFactory;
import com.example.examscanner.persistence.remote.RemoteDatabaseFacadeFactory;
import com.example.examscanner.persistence.remote.files_management.RemoteFilesManagerFactory;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.RepositoryException;
import com.example.examscanner.repositories.corner_detected_capture.CDCRepositoryFacrory;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.ExamInstancesGenerator;
import com.example.examscanner.repositories.exam.ExamRepositoryFactory;
import com.example.examscanner.repositories.grader.GraderRepoFactory;
import com.example.examscanner.stubs.FilesManagerStub;
import com.example.examscanner.stubs.RemoteDatabaseStubInstance;
import com.example.examscanner.stubs.ThrowsExceptionFileManage;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.reactivex.observers.TestObserver;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class CreateTransaction {
    private static final String QAD_NOT_SUPPORTING_EXAMINEE_IDS_ETRACTIONS = "123/456";
    Repository<Exam> eRepo;
    Repository<ScannedCapture> out;
    ImageProcessingFacade ip;
    private Exam theExam;

    @Rule
    public GrantPermissionRule write = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    @Rule
    public GrantPermissionRule rule = GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE);
    private String QAD_GRADER_EMAIL;


    @Before
    public void setUp() throws Exception {
        ContextProvider.set(getInstrumentation().getContext());
        AppDatabaseFactory.setTestMode();
        RemoteFilesManagerFactory.setTestMode();
        RemoteDatabaseFacadeFactory.setStubInstance(new RemoteDatabaseStubInstance());
        getState();
        eRepo = new ExamRepositoryFactory().create();
        theExam = ExamInstancesGenerator.getInstance1();
        eRepo.create(theExam);
        ImageProcessingFactory.setTestMode(getApplicationContext());
        ip = new ImageProcessingFactory().create();
    }

    @After
    public void tearDown() throws Exception {
        AppDatabaseFactory.tearDownDb();
        RemoteDatabaseFacadeFactory.tearDown();
        CommunicationFacadeFactory.tearDown();
        RemoteFilesManagerFactory.tearDown();
        ExamRepositoryFactory.tearDown();
        CDCRepositoryFacrory.tearDown();
        ScannedCaptureRepositoryFactory.tearDown();
        GraderRepoFactory.tearDown();
    }

    @NotNull
    private State getState() {
        State[] ss = new State[1];
        TestObserver<FirebaseAuth> observer = new TestObserver<FirebaseAuth>(){
            @Override
            public void onNext(FirebaseAuth firebaseAuth) {
                StateFactory.get().login(s->ss[0]=s, firebaseAuth);
            }
        };
        AuthenticationHandlerFactory.getTest().authenticate().subscribe(observer);
        observer.awaitCount(1);
        return ss[0];
    }

    @Test
    public void testExceptionIsthrownWhenErrorOccurs() {
        CommunicationFacadeFactory.tearDown();
        FilesManagerFactory.setStubInstance(new ThrowsExceptionFileManage());
        out = new ScannedCaptureRepositoryFactory().create();
        ip.scanAnswers(BitmapsInstancesFactoryAndroidTest.getTestJpg1(), 53, new ScanAnswersConsumer() {
            @Override
            public void consume(int numOfAnswersDetected, int[] answersIds, float[] lefts, float[] tops, float[] rights, float[] bottoms, int[] selections) {
                try {
                    QAD_GRADER_EMAIL = "QAD_GRADER_EMAIL";
                    out.create(
                            new ScannedCapture(-1,null, null, numOfAnswersDetected,numOfAnswersDetected,answersIds,lefts,tops,rights,bottoms,selections,theExam.getVersions().get(0), QAD_NOT_SUPPORTING_EXAMINEE_IDS_ETRACTIONS, QAD_GRADER_EMAIL)
                    );
                } catch (RepositoryException e) {
                    return;
                }
                fail();
            }
        });
    }

    @Test
    public void testScannedCaptrueIsInvalidWhenErrorOccrus() {
        CommunicationFacadeFactory.tearDown();
        final ThrowsExceptionFileManage fm = new ThrowsExceptionFileManage();
        FilesManagerFactory.setStubInstance(fm);
        out = new ScannedCaptureRepositoryFactory().create();
        ip.scanAnswers(BitmapsInstancesFactoryAndroidTest.getTestJpg1(), 53, new ScanAnswersConsumer() {
            @Override
            public void consume(int numOfAnswersDetected, int[] answersIds, float[] lefts, float[] tops, float[] rights, float[] bottoms, int[] selections) {
                try {
                    out.create(
                            new ScannedCapture(-1,null, null, numOfAnswersDetected,numOfAnswersDetected,answersIds,lefts,tops,rights,bottoms,selections,theExam.getVersions().get(0), QAD_NOT_SUPPORTING_EXAMINEE_IDS_ETRACTIONS, QAD_GRADER_EMAIL)
                    );
                } catch (RepositoryException e) {
                    fm.stopThrowing();
                    final int size = out.get(x -> true).size();
                    if(size >0){
                        fail(String.format("There are %d scanned captures", size));
                    }
                    return;
                }
                   fail();
            }
        });
    }

    @Test
    public void testScannedCaptrueIsValidInHappyScipt() {
        CommunicationFacadeFactory.tearDown();
        FilesManagerFactory.setStubInstance(new FilesManagerStub());
        out = new ScannedCaptureRepositoryFactory().create();
        ip.scanAnswers(BitmapsInstancesFactoryAndroidTest.getTestJpg1(), 53, new ScanAnswersConsumer() {
            @Override
            public void consume(int numOfAnswersDetected, int[] answersIds, float[] lefts, float[] tops, float[] rights, float[] bottoms, int[] selections) {
                try {
                    int[] ansDet ={1};
                    int[] sel ={1};
                    out.create(
                            new ScannedCapture(-1,BitmapsInstancesFactoryAndroidTest.getTestJpg1(), BitmapsInstancesFactoryAndroidTest.getTestJpg1(), 1,1,ansDet,new float[1],new float[1],new float[1],new float[1],sel,theExam.getVersions().get(0), QAD_NOT_SUPPORTING_EXAMINEE_IDS_ETRACTIONS, QAD_GRADER_EMAIL)
                    );
                } catch (RepositoryException e) {
                    fail(e.getMessage());
                }
            }
        });
        assertTrue(out.get(x->true).size()==1);
    }
}
