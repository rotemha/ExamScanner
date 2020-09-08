package com.example.examscanner.components.scan_exam.reslove_answers;

import com.example.examscanner.authentication.AuthenticationHandlerFactory;
import com.example.examscanner.authentication.state.State;
import com.example.examscanner.authentication.state.StateFactory;
import com.example.examscanner.authentication.state.StateHolder;
import com.example.examscanner.communication.CommunicationFacadeFactory;
import com.example.examscanner.components.scan_exam.AbstractComponentInstrumentedTest;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.persistence.local.AppDatabaseFactory;
import com.example.examscanner.repositories.exam.ExamRepositoryFactory;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;
import com.example.examscanner.use_case_contexts_creators.CornerDetectionContext3Setuper;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertTrue;

@Ignore("ignore")
public class ResolveAnswersViewModelTest extends AbstractComponentInstrumentedTest {

    private CornerDetectionContext3Setuper usecaseContext;
    private ResolveAnswersViewModel out;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        TestObserver to = new TestObserver(){
            @Override
            public void onNext(Object value) {
                StateFactory.get().login(StateHolder.getDefaultHolder(), value);
            }
        };
        AuthenticationHandlerFactory.getTest().authenticate().subscribe(to);
        usecaseContext = new CornerDetectionContext3Setuper(getState());
        usecaseContext.setup();
        out = new ResolveAnswersViewModel(
                new ImageProcessingFactory().create(),
                new ScannedCaptureRepositoryFactory().create()
        );

        AppDatabaseFactory.tearDownDb();
        ExamRepositoryFactory.tearDown();
        CommunicationFacadeFactory.tearDown();
        ScannedCaptureRepositoryFactory.tearDown();
        //TODO - bob need to be admin. probably down the road it won't be possible to just read the DB
        AuthenticationHandlerFactory.getTest().authenticate("bobexamscanner80@gmail.com", "Ycombinator").subscribe(to);
    }

    private State getState() {
        State[] ss = new State[1];
        TestObserver<FirebaseAuth> observer = new TestObserver<FirebaseAuth>(){
            @Override
            public void onNext(FirebaseAuth firebaseAuth) {
                StateFactory.get().login(s->ss[0]=s, firebaseAuth);
            }

            @Override
            public void onError(Throwable t) {
                super.onError(t);
            }

            @Override
            public void onSuccess(FirebaseAuth value) {
                super.onSuccess(value);
            }
        };
        AuthenticationHandlerFactory.getTest().authenticate().subscribe(observer);
        observer.awaitCount(1);
        assert ss[0]!=null;
        return ss[0];
    }

    @Test
    public void theSolutionIsStoredInTheDB() {
        final List<ScannedCapture> scannedCaptures = new ScannedCaptureRepositoryFactory().create().get(s -> true);
        assertTrue(scannedCaptures.contains(usecaseContext.getSc()));
    }
}