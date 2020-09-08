package com.example.examscanner.components.integrations;

import android.util.Log;

import com.example.examscanner.authentication.AuthenticationHandlerFactory;
import com.example.examscanner.authentication.state.State;
import com.example.examscanner.authentication.state.StateFactory;
import com.example.examscanner.authentication.state.StateHolder;
import com.example.examscanner.components.scan_exam.AbstractComponentInstrumentedTest;
import com.example.examscanner.persistence.remote.RemoteDatabaseFacade;
import com.example.examscanner.persistence.remote.RemoteDatabaseFacadeFactory;
import com.example.examscanner.persistence.remote.entities.ExamineeSolution;
import com.example.examscanner.repositories.scanned_capture.Answer;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepository;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;
import com.example.examscanner.use_case_contexts_creators.CornerDetectionContext3Setuper;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.TestObserver;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ScannedCaptureInserted_ExamineeSolutionIsStored extends AbstractComponentInstrumentedTest {
    private final String DEBUG_TAG = "DebugExamScanner";
    private CornerDetectionContext3Setuper usecaseContext;
    private RemoteDatabaseFacade out;


    @Override
    @Before
    public void setUp() {
        super.setUp();
        ScannedCaptureRepositoryFactory.tearDown();
        usecaseContext = new CornerDetectionContext3Setuper(
                getState()
        );
        usecaseContext.setSCRepo(new ScannedCaptureRepositoryFactory().create());
        RemoteDatabaseFacadeFactory.tearDown();
        usecaseContext.setup();
        out = RemoteDatabaseFacadeFactory.get();
    }

    @Override
    @After
    public void tearDown() {
        super.tearDown();
    }

    private State getState() {
        State[] ss = new State[1];
        TestObserver<FirebaseAuth> observer = new TestObserver<FirebaseAuth>(){
            @Override
            public void onNext(FirebaseAuth firebaseAuth) {
                Log.d(DEBUG_TAG,"login success");
                StateFactory.get().login(s->ss[0]=s, firebaseAuth);
            }

            @Override
            public void onError(Throwable t) {
                Log.d(DEBUG_TAG,"login fail",t);
                super.onError(t);
                Assert.fail();
            }

            @Override
            public void onSuccess(FirebaseAuth value) {
                super.onSuccess(value);
            }
        };
        AuthenticationHandlerFactory.getTest().authenticate().subscribe(observer);
        observer.awaitCount(1);
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(ss[0]==null){
            Assert.fail();
        }
        return ss[0];
    }

    @Test
    public void theSolutionIsStoredInTheDB(){
        List<ExamineeSolution> es = new ArrayList<>();
        out.getExamineeSolutions().blockingSubscribe(_es->es.addAll(_es));
        ExamineeSolution actual = null;
        final ScannedCapture expected = usecaseContext.getSc();
        for (ExamineeSolution currEs:es) {
            if(currEs.isValid && currEs.examineeId.equals(expected.getExamineeId())){
                actual = currEs;
            }
        }
        assertNotNull(actual);
        assertTrue(actual.answers.size() == expected.getAnswers().size());
        for (Answer a : expected.getAnswers()) {
            Integer actualAns = actual.answers.get(a.getAnsNum());
            Integer expectedAns = new Integer(a.getSelection());
            assertEquals(expectedAns, actualAns );
        }

    }
}
