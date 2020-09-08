package com.example.examscanner.communication;

import android.Manifest;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import com.example.examscanner.authentication.AuthenticationHandlerFactory;
import com.example.examscanner.communication.entities_interfaces.ExamEntityInterface;
import com.example.examscanner.communication.entities_interfaces.QuestionEntityInterface;
import com.example.examscanner.communication.entities_interfaces.SemiScannedCaptureEntityInterface;
import com.example.examscanner.communication.entities_interfaces.VersionEntityInterface;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.log.ESLogeerFactory;
import com.example.examscanner.persistence.local.AppDatabase;
import com.example.examscanner.persistence.local.AppDatabaseFactory;
import com.example.examscanner.persistence.remote.FirebaseDatabaseFactory;
import com.example.examscanner.persistence.remote.RemoteDatabaseFacadeFactory;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.observers.TestObserver;

import static java.lang.Thread.sleep;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class CommunicationFacadeTest {
    private static final int QAD_NUM_OF_QUESTIONS = 50;
    private static final String DONT_KNOW_EXAMINEE_ID = "DONT_KNOW_EXAMINEE_ID";
    private final String DEBUG_TAG = "DebugExamScanner";
    CommunicationFacade oot;
    AppDatabase db;
    private String currentUserId;
    private static Bitmap DEFAULT_VERSION_BITMAP;

    @Rule
    public GrantPermissionRule write = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    @Rule
    public GrantPermissionRule rule = GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE);

    @Before
    public void setUp() throws Exception {
        try {
            AppDatabaseFactory.setTestMode();
            db = AppDatabaseFactory.getInstance();
            oot = new CommunicationFacadeFactory().create();
            FirebaseDatabaseFactory.setTestMode();
            TestObserver<FirebaseAuth> observer = new TestObserver<FirebaseAuth>(){
                @Override
                public void onNext(FirebaseAuth firebaseAuth) {
                    ESLogeerFactory.getInstance().log(DEBUG_TAG, "logn succcess");
                    currentUserId = firebaseAuth.getUid();
                }

                @Override
                public void onError(Throwable t) {
                    ESLogeerFactory.getInstance().log(DEBUG_TAG, "logn failed", t);
                    Assert.fail();
                }
            };
            AuthenticationHandlerFactory.getTest().authenticate().subscribe(observer);
            observer.awaitCount(1);
            observer.assertComplete();
            DEFAULT_VERSION_BITMAP = BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_in1();
        }catch (Exception e){
            ESLogeerFactory.getInstance().log(DEBUG_TAG, "failed setup", e);
            Assert.fail();
        }
    }

    @After
    public void tearDown() {
        try {
            AppDatabaseFactory.tearDownDb();
            RemoteDatabaseFacadeFactory.tearDown();
        }catch (Exception e){
            ESLogeerFactory.getInstance().log(DEBUG_TAG, "failed teardown", e);
        }
    }

    private ExamContext setUpExamContext() {
        long examCreationContext = oot.createNewCreateExamSession();
        return new ExamContext(
        oot.createExam("COMP", "walla.co.il", "2020", 1, 1, currentUserId,new String[0],examCreationContext,QAD_NUM_OF_QUESTIONS, 0,1)
        );
    }

    private VersionContext setUpVersionContext() {
        ExamContext context = setUpExamContext();
        final int versionNumber = 1;
        return new VersionContext(oot.addVersion(context.eId, versionNumber,DEFAULT_VERSION_BITMAP),  context.eId, versionNumber);
    }

    private VersionContext setUpVersionNonEmptyContext() {
        ExamContext context = setUpExamContext();
        final int versionNumber = 1;
        VersionContext ans = new VersionContext(oot.addVersion(context.eId, versionNumber,DEFAULT_VERSION_BITMAP),context.eId,versionNumber);
        oot.addQuestion(ans.vId, 1, 1,0,0,0,0);
        oot.addQuestion(ans.vId, 2, 400,0,0,0,0);
        oot.addQuestion(ans.vId, 3, 5,0,0,0,0);
        oot.addQuestion(ans.vId, 4, 1,0,0,0,0);
        oot.addQuestion(ans.vId, 5, 2,0,0,0,0);
        return ans;
    }

    private ExamineeSolutionContext setUpExamineeSolutionContextWithNonEmptyVersion() {
        VersionContext versionContext = setUpVersionNonEmptyContext();
        long scanExamSessionId= setUpScanExamSessionContext(versionContext.eId);
        return new ExamineeSolutionContext(
                0,
                oot.createExamineeSolution(scanExamSessionId, BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked(), DONT_KNOW_EXAMINEE_ID, versionContext.vId,BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked()),
                versionContext.vId
        );
    }

    private long setUpScanExamSessionContext(long eId) {
        return oot.createNewScanExamSession(eId);
    }

    @Test
    public void testCreateAndGetExamNotNull() {
        String[] graders = new String[]{};
        long id = oot.createExam("COMP", "walla.co.il", "2020", 1, 1,currentUserId, graders,-1,QAD_NUM_OF_QUESTIONS, 0,1);
        assertNotNull(oot.getExamById(id));
    }

    @Test
    public void testAddVersionNotNull() {
        long examId = setUpExamContext().eId;
        final int versionNumber = 1;
        long vId = oot.addVersion(examId, versionNumber, DEFAULT_VERSION_BITMAP);
        assertNotNull(oot.getVersionByExamIdAndNumber(examId, versionNumber));
    }

    @Test
    public void testAddVersionDataIsCorrect() {
        long examId = setUpExamContext().eId;
        final int versionNumber = 6;
        long vId = oot.addVersion(examId, versionNumber ,DEFAULT_VERSION_BITMAP);
        VersionEntityInterface vEI = oot.getVersionByExamIdAndNumber(examId, versionNumber);
        assertEquals(vEI.getExamId(), examId);
        assertEquals(vEI.getNumber(), versionNumber);
        assertArrayEquals(oot.getExamById(examId).getVersionsIds(), new long[]{vId});
    }

    @Test
    public void testSessionIdUpdates() {
        try {
            ExamContext e1 = setUpExamContext();
            ExamContext e2 = setUpExamContext();
            assertTrue(oot.createNewScanExamSession(e1.eId) != oot.createNewScanExamSession(e1.eId));
        }catch ( Throwable t){
            ESLogeerFactory.getInstance().log(DEBUG_TAG, "fail", t);
            Assert.fail();
        }
    }

    @Test
    public void testGetExamByScanExamSessionId() {
        ExamContext e = setUpExamContext();
        long scanExamSessionId = oot.createNewScanExamSession(e.eId);
        assertEquals(e.eId, oot.getExamIdByScanExamSession(scanExamSessionId));
    }

    @Test
    public void testCreateAndGetSemiScannedCaptureNotNull() {
        VersionContext context = setUpVersionContext();
        long scanExamSessionId = oot.createNewScanExamSession(context.eId);
        long sscId = oot.createSemiScannedCapture(0, 0, 0, 0, scanExamSessionId, BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked());
        assertNotNull(oot.getSemiScannedCapture(sscId));
    }

    @Test
    public void testCreateAndGetBySessionSemiScannedCaptureNotNull() {
        VersionContext context = setUpVersionContext();
        long scanExamSessionId = setUpScanExamSessionContext(context.eId);
        long sscId = oot.createSemiScannedCapture(0, 0, 0, 0, scanExamSessionId, BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked());
        boolean assertion = false;
        long[] sscs = oot.getSemiScannedCaptureBySession(scanExamSessionId);
        for (int i = 0; i < sscs.length; i++) {
            assertion |= sscs[i] == sscId;
        }
        assertTrue(assertion);
    }

    @Test
    public void testWeirdBug() {
        setUpVersionContext();
        VersionContext context2 = setUpVersionContext();
        long scanExamSessionId = setUpScanExamSessionContext(context2.eId);
        long sscId = oot.createSemiScannedCapture(0, 0, 0, 0, scanExamSessionId, BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked());
        boolean assertion = false;
        long[] sscs = oot.getSemiScannedCaptureBySession(scanExamSessionId);
        for (int i = 0; i < sscs.length; i++) {
            assertion |= sscs[i] == sscId;
        }
        assertTrue(assertion);
    }

    @Test
    public void testGetAndCreateExamineeSolutionsBySessionNotEmpty() {
        VersionContext vContext = setUpVersionContext();
        long scanExamSessionId = setUpScanExamSessionContext(vContext.eId);
        long esId = oot.createExamineeSolution(scanExamSessionId, BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked(), DONT_KNOW_EXAMINEE_ID, vContext.vId,BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked());
        assertTrue(oot.getExamineeSolutionsBySession(scanExamSessionId).length > 0);
    }

    @Test
    public void testGetAndCreateExamineeSolutionsBySessionUpdates() {
        VersionContext vContext = setUpVersionContext();
        long scanExamSessionId = setUpScanExamSessionContext(vContext.eId);
        long esId = oot.createExamineeSolution(scanExamSessionId, BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked(), DONT_KNOW_EXAMINEE_ID, vContext.vId,BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked());
        boolean assertion = false;
        long[] ess = oot.getExamineeSolutionsBySession(scanExamSessionId);
        for (int i = 0; i < ess.length; i++) {
            assertion |= ess[i] == esId;
        }
        assertTrue(assertion);
    }

    @Test
    public void testGetAndCreateExamineeAnswersByExamineeSolutionNotNull() {
        ExamineeSolutionContext esContext = setUpExamineeSolutionContextWithNonEmptyVersion();
        long[] qs = oot.getQuestionsByVersionId(esContext.vId);
        long eaId = oot.addExamineeAnswer(esContext.solutionId, qs[0], 1, 0, 0, 0, 0);
        assertTrue(oot.getExamineeAnswersIdsByExamineeSolutionId(esContext.solutionId).length > 0);
    }

    @Test
    public void testGetAndCreateExamineeAnswersByExamineeSolutionDataUpdates() {
        ExamineeSolutionContext esContext = setUpExamineeSolutionContextWithNonEmptyVersion();
        long[] qs = oot.getQuestionsByVersionId(esContext.vId);
        long eaId = oot.addExamineeAnswer(esContext.solutionId, qs[0], 1, 0, 0, 0, 0);
        long[] answers = oot.getExamineeAnswersIdsByExamineeSolutionId(esContext.solutionId);
        boolean assertion = false;
        for (int i = 0; i < answers.length; i++) {
            assertion |= answers[i] == eaId;
        }
        assertTrue(assertion);
    }
    @Test
    public void testSemiScannedCaptureEntityInterface() {
        VersionContext versionContext = setUpVersionContext();
        long scanExamSessionId = setUpScanExamSessionContext(versionContext.eId);
        final Bitmap testJpg1Marked = BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked();
        final int rightMostY = 1;
        final int rightMostX = 2;
        final int upperMostY = 3;
        final int leftMostX = 4;
        oot.createSemiScannedCapture(leftMostX, upperMostY, rightMostX, rightMostY,scanExamSessionId, testJpg1Marked);
        SemiScannedCaptureEntityInterface ei = oot.getSemiScannedCapture(
                oot.getSemiScannedCaptureBySession(scanExamSessionId)[0]
        );
        assertTrue(ei.getBitmap().sameAs(testJpg1Marked));
        assertEquals(ei.getSessionId(),scanExamSessionId);
        assertEquals(ei.getLeftMostX(),leftMostX);
        assertEquals(ei.getUpperMostY(),upperMostY);
        assertEquals(ei.getRightMostX(),rightMostX);
        assertEquals(ei.getBottomMosty(),rightMostY);
    }

    @Test
    public void testVersionEntityInterface() {
        ExamContext examContext = setUpExamContext();
        final int versionNumber = 1;
        oot.addVersion(examContext.eId, versionNumber, DEFAULT_VERSION_BITMAP);
        VersionEntityInterface ei = oot.getVersionByExamIdAndNumber(examContext.eId,versionNumber);
        assertEquals(ei.getNumber(),versionNumber);
        assertEquals(ei.getExamId(),examContext.eId);
        assertArrayEquals(ei.getQuestions(),new long[0]);
    }
    @Test
    public void testExamEntityInterface() {
        final String comp = "COMP";
        final String url = "walla.co.il";
        final String year = "2020";
        final int term = 1;
        final int semester = 1;
        final int sessionId = -1;
        long id = oot.createExam(comp, url, year, term, semester,currentUserId,new String[0], sessionId,QAD_NUM_OF_QUESTIONS, 0,1);
        ExamEntityInterface ei = oot.getExamById(id);
        assertEquals(ei.getCourseName(), comp);
        assertEquals(ei.getUrl(), url);
        assertEquals(ei.getTerm(), term);
        assertEquals(ei.getSemester(), semester);
        assertEquals(ei.getSessionId(), sessionId);
        assertArrayEquals(ei.getVersionsIds(), new long[0]);
    }
    @Test
    public void testQuestionEntityInterface() {
        try {
            VersionContext versionContext = setUpVersionContext();
            final int qNum = 3;
            final int correctAnswer = 4;
            final int leftX = 1;
            final int upY = 2;
            final int rightX = 3;
            final int bottomY = 4;
            oot.addQuestion(versionContext.vId, qNum, correctAnswer, leftX, upY, rightX, bottomY);
            QuestionEntityInterface ei = oot.getQuestionByExamIdVerNumAndQNum(versionContext.eId,versionContext.vNum,qNum);
            assertEquals(ei.getLeftX(),leftX);
            assertEquals(ei.getUpY(),upY);
            assertEquals(ei.getBottomY(),bottomY);
            assertEquals(ei.getRightX(),rightX);
            assertEquals(ei.getCorrectAnswer(),correctAnswer);
            assertEquals(ei.getVersionId(),versionContext.vId);
            sleep(3000);
        }catch (Throwable t){
            ESLogeerFactory.getInstance().log(DEBUG_TAG, "fail", t);
            Assert.fail();
        }
    }




    private class VersionContext {
        long vId;
        long eId;
        int vNum;

        public VersionContext(long vId, long eId, int vNum) {
            this.vId = vId;
            this.eId =eId;
            this.vNum = vNum;
        }

    }

    private class ExamContext {
        long eId;

        public ExamContext(long eId) {
            this.eId = eId;
        }
    }

    private class ExamineeSolutionContext {
        long vId;
        long solutionId;
        int esId;

        public ExamineeSolutionContext(int examineeId, long solutionId,long vId) {
            this.vId = vId;
            this.solutionId = solutionId;
            this.esId = examineeId;
        }
    }
}
