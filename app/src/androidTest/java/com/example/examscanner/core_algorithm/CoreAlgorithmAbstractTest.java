package com.example.examscanner.core_algorithm;

import android.Manifest;
import android.util.Log;

import androidx.test.rule.GrantPermissionRule;

import com.example.examscanner.authentication.AuthenticationHandlerFactory;
import com.example.examscanner.authentication.state.State;
import com.example.examscanner.authentication.state.StateFactory;
import com.example.examscanner.components.scan_exam.AbstractComponentInstrumentedTest;
import com.example.examscanner.components.scan_exam.capture.CaptureViewModel;
import com.example.examscanner.components.scan_exam.detect_corners.RemoteFilesManagerStub;
import com.example.examscanner.persistence.local.files_management.FilesManagerFactory;
import com.example.examscanner.persistence.remote.FirebaseDatabaseFactory;
import com.example.examscanner.persistence.remote.RemoteDatabaseFacadeFactory;
import com.example.examscanner.persistence.remote.files_management.RemoteFilesManagerFactory;
import com.example.examscanner.repositories.scanned_capture.Answer;
import com.example.examscanner.repositories.scanned_capture.ResolvedAnswer;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;
import com.example.examscanner.stubs.FilesManagerStub;
import com.example.examscanner.stubs.RemoteDatabaseStubInstance;
import com.example.examscanner.use_case_contexts_creators.CornerDetectionContext2Setuper;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public abstract class CoreAlgorithmAbstractTest extends AbstractComponentInstrumentedTest {

    private final String DEBUG_TAG = "DebugExamScanner";
    @Rule
    public GrantPermissionRule write = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    @Rule
    public GrantPermissionRule rule = GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE);
    public CornerDetectionContext2Setuper useCaseContext;
    public CaptureViewModel cvm;

    @Before
    public void setUp() {
        try {
            RemoteDatabaseFacadeFactory.setStubInstance(new RemoteDatabaseStubInstance());
            RemoteFilesManagerFactory.setStubInstabce(new RemoteFilesManagerStub());
            FilesManagerFactory.setStubInstance(new FilesManagerStub());
            if(!USINIG_REAL_DB)
                FirebaseDatabaseFactory.setTestMode();

            super.setUp();
            useCaseContext = getUseCaseContext();
            useCaseContext.setup();
            cvm = new CaptureViewModel(
                    useCaseContext.getSCRepo(),
                    useCaseContext.getImageProcessor(),
//                useCaseContext.getCDCRepo(),
                    -1,
                    useCaseContext.getTheExam(),
                    useCaseContext.getState()
            );
        }catch (Exception e ){
            Log.d(DEBUG_TAG, "failed setting up", e);
            Assert.fail();
        }
    }

    @NotNull
    protected CornerDetectionContext2Setuper getUseCaseContext() {
        return new CornerDetectionContext2Setuper(getState());
    }

    @NotNull
    protected State getState() {
        State[] ss = new State[1];
        TestObserver<FirebaseAuth> observer = new TestObserver<FirebaseAuth>(){
            @Override
            public void onNext(FirebaseAuth firebaseAuth) {
                Log.d(DEBUG_TAG, "sign in success");
                StateFactory.get().login(s->ss[0]=s, firebaseAuth);
            }

            @Override
            public void onError(Throwable t) {
                Log.d(DEBUG_TAG, "sign in failed", t);
                super.onError(t);
                Assert.fail();
            }
        };
        AuthenticationHandlerFactory.getTest().authenticate().subscribe(observer);
        observer.awaitCount(1);
        return ss[0];
    }

    @Override
    public void tearDown(){
        useCaseContext.tearDown();
        StateFactory.tearDown();
        super.tearDown();
    }

    //    @Test
    public void scanAnswersByPositions() {

        cvm.consumeCapture(useCaseContext.getCapture());
        cvm.processCapture();
//        useCaseContext.getCDC().setBitmap(Bitmap.createScaledBitmap(useCaseContext.getCDC().getBitmap(), useCaseContext.getOrigVersionImage().getWidth(), useCaseContext.getOrigVersionImage().getHeight(), false));
//        out.scanAnswers(useCaseContext.getCDC());
        ArrayList<Integer> realAnswers = new ArrayList<Integer>(
                Arrays.asList(5, 4, 3, 3, 3, 3, 5, 2, 4, 1, 1, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 1, 2, 3,
                        4, 5, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 1, 2, 3, 4));
        List<ScannedCapture> sclst = useCaseContext.getSCRepo().get(x -> true);
        assert sclst.size() == 1;
        ScannedCapture sc = sclst.get(0);
        assertEquals(50, sc.getAnswers().size());
        int numOfCorrextDetections = 0;
        int resolved = 0;
        int correctResolved = 0;
        int wrongResolved = 0;

        for (int i = 0; i < useCaseContext.getTheExam().getNumOfQuestions(); i++) {
            Answer ans = sc.getAnswerByNum(i + 1);
            if (ans.isResolved()) {
                resolved++;
                ResolvedAnswer rAns = (ResolvedAnswer) ans;
                if (realAnswers.get(i) == rAns.getSelection()) {
                    correctResolved++;
                } else {
                    wrongResolved++;
                    System.out.println(String.format("Q#%d:\t EXP:%d, ACT:%d\n", i + 1, realAnswers.get(i), rAns.getSelection()));
                }
            }
        }
        System.out.println(
                String.format(
                        "RESOLVED#%d:\t CORRECTLY_RESOLVED:%d, WRONG_RESOLVED:%d\n",
                        resolved, correctResolved, wrongResolved
                )
        );
        assertEquals(50, resolved);
        assertEquals(50, correctResolved);
        assertEquals(0, wrongResolved);
    }


    public void scanAnswersByPositions_Uncertain() {

        cvm.consumeCapture(useCaseContext.getCapture());
        cvm.processCapture();
        List<Integer> answers = new ArrayList<>(Arrays.asList(5, 4, 3, 5, 3, 3, 5, 2, 4, 1, 1, 1, 2, 3, 4, 5, 1, 2, 3, -1, 5, 1, 2, 3, 4,
                                            5, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 1, 2, 3, 4));
        List<Integer> uncertainAnswers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 7, 8, 9, 13, 14, 15, 16, 18, 20, 21, 22, 25, 27, 28, 29, 30, 32, 33, 38));
        List<ScannedCapture> sclst = useCaseContext.getSCRepo().get(x -> true);
        assert sclst.size() == 1;
        ScannedCapture sc = sclst.get(0);
        assertEquals(50, sc.getAnswers().size());


        for (int i = 0; i < useCaseContext.getTheExam().getNumOfQuestions(); i++) {
            Answer ans = sc.getAnswerByNum(i + 1);
            if (ans.isResolved()) {
                ResolvedAnswer rAns = (ResolvedAnswer) ans;
                assertEquals("The algorithm was mistaken resolving question " + (i+1),
                        (int)answers.get(i), rAns.getSelection());
            }
            else{
                assertTrue("The algorithm didn't resolve question " + (i+1),
                            uncertainAnswers.contains(i+1));
            }
        }
    }
}
