package com.example.examscanner.core_algorithm;

import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.components.scan_exam.detect_corners.CornerDetectionViewModel;
import com.example.examscanner.image_processing.ImageProcessor;
import com.example.examscanner.use_case_contexts_creators.CornerDetectionContext2Setuper;

import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static com.example.examscanner.ImageProcessorsGenerator.alignmentSubIp;

public class SmallCoreAlgorithmJPGTest extends CoreAlgorithmAbstractTest {

    private interface SetUpCallback{CornerDetectionContext2Setuper createContext();}
    private SetUpCallback setupCallback;
    private CornerDetectionViewModel out;


    @Before
    public void setUp() {
        CornerDetectionContext2Setuper.stubExamRepo = true;
    }

    public void lateSetup() {
//        TestObserver<FirebaseAuth> observer = new TestObserver<FirebaseAuth>(){
//            @Override
//            public void onNext(FirebaseAuth firebaseAuth) {
////                currentUserId = firebaseAuth.getUid();
//            }
//        };
        super.setUp();
    }

    @Override
    @After
    public void tearDown(){
        CornerDetectionContext2Setuper.stubExamRepo = false;
        super.tearDown();
    }

    @NotNull
    @Override
    protected CornerDetectionContext2Setuper getUseCaseContext() {
        return setupCallback.createContext();
    }

    @Test
    public void scanAnswersByPositionsInsatnce7() {
        setupCallback = ()->new CornerDetectionContext2Setuper(getState(),BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_7());
        lateSetup();
        super.scanAnswersByPositions();
    }



}
