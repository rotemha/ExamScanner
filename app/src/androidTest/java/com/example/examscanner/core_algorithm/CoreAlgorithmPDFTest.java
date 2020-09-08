package com.example.examscanner.core_algorithm;

import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.components.scan_exam.detect_corners.CornerDetectionViewModel;
import com.example.examscanner.image_processing.ImageProcessor;
import com.example.examscanner.use_case_contexts_creators.CornerDetectionContext2Setuper;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static com.example.examscanner.ImageProcessorsGenerator.alignmentSubIp;


public class CoreAlgorithmPDFTest extends CoreAlgorithmAbstractTest {



    private interface SetUpCallback{CornerDetectionContext2Setuper createContext();}
    private SetUpCallback setupCallback;
    private CornerDetectionViewModel out;


    @Before
    public void setUp() {}

    public void lateSetup() {
        super.setUp();
    }


    @NotNull
    @Override
    protected CornerDetectionContext2Setuper getUseCaseContext() {
        CornerDetectionContext2Setuper context = setupCallback.createContext();
        context.setPDF(true);
        return context;
    }

    @Test
    public void scanAnswersByPositionsSanity() {
        setupCallback = ()->super.getUseCaseContext();
        lateSetup();
        super.scanAnswersByPositions();
    }

    @Test
    public void scanAnswersByPositionsInsatnce1() {
        setupCallback = ()->new CornerDetectionContext2Setuper(BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_1(), getState());
        lateSetup();
        super.scanAnswersByPositions();
    }
    @Test
    public void scanAnswersByPositionsInsatnce2() {
        setupCallback = ()->new CornerDetectionContext2Setuper(BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_2(), getState());
        lateSetup();
        super.scanAnswersByPositions();
    }
    @Test
    public void scanAnswersByPositionsInsatnce3() {
        setupCallback = ()->new CornerDetectionContext2Setuper(BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_3(), getState());
        lateSetup();
        super.scanAnswersByPositions();
    }
    @Test
    public void scanAnswersByPositionsInsatnce4() {
        setupCallback = ()->new CornerDetectionContext2Setuper(BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_4(), getState());
        lateSetup();
        super.scanAnswersByPositions();
    }
    @Test
    public void scanAnswersByPositionsInsatnce5() {
        setupCallback = ()->new CornerDetectionContext2Setuper(BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_5(), getState());
        lateSetup();
        super.scanAnswersByPositions();
    }
    @Test
    public void scanAnswersByPositionsInsatnce6() {
        setupCallback = ()->new CornerDetectionContext2Setuper(BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_6(), getState());
        lateSetup();
        super.scanAnswersByPositions();
    }
    @Test
    public void scanAnswersByPositionsInsatnce7() {
        setupCallback = ()->new CornerDetectionContext2Setuper(BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_7(), getState());
        lateSetup();
        super.scanAnswersByPositions();
    }
    @Test
    public void scanAnswersByPositionsInsatnce8() {
        setupCallback = ()->new CornerDetectionContext2Setuper(BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_8(), getState());
        lateSetup();
        super.scanAnswersByPositions();
    }

    @Test
    public void scanAnswersByPositionsInsatnce9() {
        setupCallback = ()->new CornerDetectionContext2Setuper(
                BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_9(),
                BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_in1(),
                getState()
        );
        lateSetup();
        super.scanAnswersByPositions();
    }

    @Test
    public void scanAnswersByPositionsInsatnce10() {
        setupCallback = ()->new CornerDetectionContext2Setuper(
                getState(),
                BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_10(),
                BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_in1()
        );
        lateSetup();
        super.scanAnswersByPositions();
    }

    @Test
    public void scanAnswersByPositionsInsatnce11() {
        setupCallback = ()->new CornerDetectionContext2Setuper(
                getState(),
                BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_11(),
                BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_in1()
        );
        lateSetup();
        super.scanAnswersByPositions();
    }

    @Test
    public void scanAnswersByPositionsInsatnce12() {
        setupCallback = ()->new CornerDetectionContext2Setuper(
                getState(),
                BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_12(),
                BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_in1()
        );
        lateSetup();
        super.scanAnswersByPositions();
    }

    @Test
    public void scanAnswersByPositionsInsatnce13() {
        setupCallback = ()->new CornerDetectionContext2Setuper(
                getState(),
                BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_13(),
                BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_in1()
        );
        lateSetup();
        super.scanAnswersByPositions();
    }

    @Test
    public void scanAnswersByPositionsInsatnce14() {
        setupCallback = ()->new CornerDetectionContext2Setuper(
                getState(),
                BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_14(),
                BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_in1()
        );
        lateSetup();
        super.scanAnswersByPositions();
    }

        @Test
    public void scanAnswersByPositionsInsatnce27() {
        setupCallback = ()->new CornerDetectionContext2Setuper(
                getState(),
                alignmentSubIp(BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_27(), new ImageProcessor(getApplicationContext()))
        );
        lateSetup();
        super.scanAnswersByPositions();
    }

    @Test
    public void scanAnswersByPositionsInsatnce28() {
        setupCallback = ()->new CornerDetectionContext2Setuper(
                getState(),
                BitmapsInstancesFactoryAndroidTest.getex0_V1_ins_1(),
                BitmapsInstancesFactoryAndroidTest.get_pdf_ex0_V1_ins_in(),
                80
        );
        lateSetup();
        super.scanAnswersByPositions();
    }



}
