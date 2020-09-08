package com.example.examscanner.components.scan_exam.capture_and_detect_corners.unit;


import android.Manifest;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import com.example.examscanner.authentication.AuthenticationHandlerFactory;
import com.example.examscanner.authentication.state.State;
import com.example.examscanner.authentication.state.StateFactory;
import com.example.examscanner.components.scan_exam.capture_and_detect_corners.CaptureAndDetectCornersIntegrationAbsractTest;
import com.example.examscanner.persistence.remote.FirebaseDatabaseFactory;
import com.example.examscanner.persistence.remote.RemoteDatabaseFacadeFactory;
import com.example.examscanner.stubs.RemoteDatabaseStubInstance;
import com.example.examscanner.use_case_contexts_creators.CornerDetectionContext2Setuper;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.observers.TestObserver;

@RunWith(AndroidJUnit4.class)
public class CaptureAndDetectCornersIntegrationTest extends CaptureAndDetectCornersIntegrationAbsractTest {

    @Rule
    public GrantPermissionRule write = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    @Rule
    public GrantPermissionRule rule = GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE);

    @Rule
    public GrantPermissionRule camera = GrantPermissionRule.grant(Manifest.permission.CAMERA);

    private String test_course_name;

    @Before
    @Override
    public void setUp() {
        RemoteDatabaseFacadeFactory.setStubInstance(new RemoteDatabaseStubInstance());
        super.setUp();
    }

    @NotNull
    @Override
    protected CornerDetectionContext2Setuper getContext() {
        FirebaseDatabaseFactory.setTestMode();
        State[] ss = new State[1];
        TestObserver<FirebaseAuth> observer = new TestObserver<FirebaseAuth>(){
            @Override
            public void onNext(FirebaseAuth firebaseAuth) {
                StateFactory.get().login(s->ss[0]=s, firebaseAuth);
            }
        };
        AuthenticationHandlerFactory.getTest().authenticate().subscribe(observer);
        observer.awaitCount(1);
        return new CornerDetectionContext2Setuper(ss[0]);
    }

//    @Test
//    public void testTheAppStateStaysUpdatedWhenNavigatingForthAndBackBetweenCornerDetAndCapture() {
//        super.testTheAppStateStaysUpdatedWhenNavigatingForthAndBackBetweenCornerDetAndCapture();
//    }

    @Test
    public void testRetake() {
        super.testRetake();
    }

    @Test
    public void testQuickRetake() {
        super.testQuickRetake();
    }


//    @Test
//    public void testTheAppStateStaysUpdatedWhenNavigatingForthAndBacAndForthkBetweenCornerDetAndCapture() {
//        super.testTheAppStateStaysUpdatedWhenNavigatingForthAndBacAndForthkBetweenCornerDetAndCapture();
//    }


    @Test
    public void testWhenTheGraderStartCornerDetectionHeSeesHowManyCapturesThereAreANdWhereIsHeNoRepoStub() {
        super.testWhenTheGraderStartCornerDetectionHeSeesHowManyCapturesThereAreANdWhereIsHeNoRepoStub();
    }
}
