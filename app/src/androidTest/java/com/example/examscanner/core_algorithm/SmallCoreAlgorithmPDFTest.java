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


public class SmallCoreAlgorithmPDFTest extends CoreAlgorithmAbstractTest {



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
    public void scanAnswersByPositionsInsatnce3() {
        setupCallback = ()->new CornerDetectionContext2Setuper(BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_3(), getState());
        lateSetup();
        super.scanAnswersByPositions();
    }


}
