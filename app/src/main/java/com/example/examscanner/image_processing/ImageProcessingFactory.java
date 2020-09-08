package com.example.examscanner.image_processing;

import android.content.Context;

import com.example.examscanner.stubs.BitmapInstancesFactory;

import org.opencv.android.OpenCVLoader;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

public class ImageProcessingFactory {
    private static ImageProcessingFacade testInstance;
    private static ImageProcessingFacade stubInstance;
    private static boolean inTestMode = false;
    private static Context testContext;
//    private BitmapInstancesFactory bmFact;

    public ImageProcessingFactory() {//TODO - remove dependency
    }

    public static void setTestMode(Context context) {
        OpenCVLoader.initDebug();
        inTestMode = true;
        testContext =context;
    }

    public ImageProcessingFacade create(){
        if (testInstance!=null) return testInstance;
        if (stubInstance!=null) return stubInstance;
        if (inTestMode) return new ImageProcessor(testContext);
        else return new ImageProcessor();
    }
    public static void ONLYFORTESTINGsetTestInstance(ImageProcessingFacade theStubInstance){
        stubInstance=theStubInstance;
    }
}
