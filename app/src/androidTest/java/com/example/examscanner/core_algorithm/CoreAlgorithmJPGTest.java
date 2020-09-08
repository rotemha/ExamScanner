package com.example.examscanner.core_algorithm;

import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.components.scan_exam.detect_corners.CornerDetectionViewModel;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.image_processing.ImageProcessor;
import com.example.examscanner.repositories.scanned_capture.Answer;
import com.example.examscanner.repositories.scanned_capture.ResolvedAnswer;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;
import com.example.examscanner.use_case_contexts_creators.CornerDetectionContext2Setuper;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static com.example.examscanner.ImageProcessorsGenerator.alignmentSubIp;
import static com.example.examscanner.ImageProcessorsGenerator.fakeIP;
import static org.junit.Assert.assertEquals;

public class CoreAlgorithmJPGTest extends CoreAlgorithmAbstractTest {

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
    public void scanAnswersByPositionsSanity() {
        setupCallback = ()->super.getUseCaseContext();
        lateSetup();
        super.scanAnswersByPositions();
    }

    @Test
    public void scanAnswersByPositionsInsatnce1() {
        setupCallback = ()->new CornerDetectionContext2Setuper(getState(),BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_1());
        lateSetup();
        super.scanAnswersByPositions();
    }
    @Test
    public void scanAnswersByPositionsInsatnce2() {
        setupCallback = ()->new CornerDetectionContext2Setuper(getState(),BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_2());
        lateSetup();
        super.scanAnswersByPositions();
    }
    @Test
    public void scanAnswersByPositionsInsatnce3() {
        USINIG_REAL_DB =true;
        setupCallback = ()->new CornerDetectionContext2Setuper(getState(),BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_3());
        lateSetup();
        super.scanAnswersByPositions();
        USINIG_REAL_DB =false;
    }
    @Test
    public void scanAnswersByPositionsInsatnce4() {
        setupCallback = ()->new CornerDetectionContext2Setuper(getState(),BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_4());
        lateSetup();
        super.scanAnswersByPositions();
    }
    @Test
    public void scanAnswersByPositionsInsatnce5() {
        setupCallback = ()->new CornerDetectionContext2Setuper(getState(),BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_5());
        lateSetup();
        super.scanAnswersByPositions();
    }
    @Test
    public void scanAnswersByPositionsInsatnce6() {
        setupCallback = ()->new CornerDetectionContext2Setuper(getState(),BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_6());
        lateSetup();
        super.scanAnswersByPositions();
    }
    @Test
    public void scanAnswersByPositionsInsatnce7() {
        setupCallback = ()->new CornerDetectionContext2Setuper(getState(),BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_7());
        lateSetup();
        super.scanAnswersByPositions();
    }
    @Test
    public void scanAnswersByPositionsInsatnce8() {
        setupCallback = ()->new CornerDetectionContext2Setuper(getState(),BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_8());
        lateSetup();
        super.scanAnswersByPositions();
    }

    @Test
    public void scanAnswersByPositionsInsatnce9() {
        setupCallback = ()->new CornerDetectionContext2Setuper(
                getState(),
                BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_9(),
                BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_in1()
        );
        lateSetup();
        super.scanAnswersByPositions();
    }

    @Test
    public void scanAnswersByPositionsInsatnce15() {
        setupCallback = ()->new CornerDetectionContext2Setuper(getState(),BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_15());
        lateSetup();
        super.scanAnswersByPositions_Uncertain();
    }

    @Test
    public void scanAnswersByPositionsInsatnce16() {
        setupCallback = ()->new CornerDetectionContext2Setuper(getState(),BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_16());
        lateSetup();
        super.scanAnswersByPositions_Uncertain();
    }

    @Test
    public void scanAnswersByPositionsInsatnce17() {
        setupCallback = ()->new CornerDetectionContext2Setuper(getState(),BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_17());
        lateSetup();
        super.scanAnswersByPositions_Uncertain();
    }

    @Test
    public void scanAnswersByPositionsInsatnce18() {
        setupCallback = ()->new CornerDetectionContext2Setuper(getState(),BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_18());
        lateSetup();
        super.scanAnswersByPositions_Uncertain();
    }

    @Test
    public void scanAnswersByPositionsInsatnce19() {
        setupCallback = ()->new CornerDetectionContext2Setuper(getState(),BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_19());
        lateSetup();
        super.scanAnswersByPositions_Uncertain();
    }

    @Test
    public void scanAnswersByPositionsInsatnce20() {
        setupCallback = ()->new CornerDetectionContext2Setuper(getState(),BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_20());
        lateSetup();
        super.scanAnswersByPositions_Uncertain();
    }

    @Test
    public void scanAnswersByPositionsInsatnce21() {
        setupCallback = ()->new CornerDetectionContext2Setuper(getState(),BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_21());
        lateSetup();
        super.scanAnswersByPositions_Uncertain();
    }

    @Test
    public void scanAnswersByPositionsInsatnce22() {
        setupCallback = ()->new CornerDetectionContext2Setuper(getState(),BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_22());
        lateSetup();
        super.scanAnswersByPositions_Uncertain();
    }

    @Test
    public void scanAnswersByPositionsInsatnce23() {
        setupCallback = ()->new CornerDetectionContext2Setuper(getState(),BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_23());
        lateSetup();
        super.scanAnswersByPositions_Uncertain();
    }

    @Test
    public void scanAnswersByPositionsInsatnce24() {
        setupCallback = ()->new CornerDetectionContext2Setuper(getState(),BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_24());
        lateSetup();
        super.scanAnswersByPositions_Uncertain();
    }

    @Test
    public void scanAnswersByPositionsInsatnce25() {
        setupCallback = ()->new CornerDetectionContext2Setuper(getState(),BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_25());
        lateSetup();
        super.scanAnswersByPositions_Uncertain();
    }

    @Test
    public void scanAnswersByPositionsInsatnce26() {
        setupCallback = ()->new CornerDetectionContext2Setuper(getState(),BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_26());
        lateSetup();
        super.scanAnswersByPositions_Uncertain();
    }

    @Ignore
    @Test
    public void scanAnswersByPositionsInsatnce001() {
        setupCallback = ()->new CornerDetectionContext2Setuper(
                getState(),
                BitmapsInstancesFactoryAndroidTest.getCaspl182_V1_ins_001(),
                BitmapsInstancesFactoryAndroidTest.getCaspl182_V1_orig()
        );
        lateSetup();
        super.scanAnswersByPositions();
    }
    @Test
    public void scanAnswersByPositionsInsatnce27() {
        setupCallback = () -> new CornerDetectionContext2Setuper(
                getState(),
                alignmentSubIp(BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_27(), new ImageProcessor(getApplicationContext()))
        );
        lateSetup();
        super.scanAnswersByPositions();
    }
    @Test
    public void scanAnswersByPositionsInsatnce28() {
        setupCallback = () -> new CornerDetectionContext2Setuper(
                getState(),
                alignmentSubIp(BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_28(), new ImageProcessor(getApplicationContext()))
        );
        lateSetup();
        super.scanAnswersByPositions();
    }


}
