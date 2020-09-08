package com.example.examscanner.stubs;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.example.examscanner.image_processing.DetectCornersConsumer;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ScanAnswersConsumer;

public class ImageProcessorStub implements ImageProcessingFacade {
    @Override
    public void detectCorners(Bitmap bm, DetectCornersConsumer consumer) {

    }

    @Override
    public Bitmap transformToRectangle(Bitmap bitmap, Point upperLeft, Point upperRight, Point bottomRight, Point bottomLeft) {
        return null;
    }

    @Override
    public void scanAnswers(Bitmap bitmap, int amountOfQuestions, ScanAnswersConsumer consumer) {
        ScannedCapturesInstancesFactory.instance1(consumer);
    }

    @Override
    public void scanAnswers(Bitmap bitmap, int amountOfQuestions, ScanAnswersConsumer consumer, float[] leftMostXs, float[] upperMostYs) {

    }

    @Override
    public void scanAnswers(Bitmap bitmap, ScanAnswersConsumer consumer) {
        ScannedCapturesInstancesFactory.instance1(consumer);
    }

    @Override
    public Bitmap align(Bitmap bitmap, Bitmap perfectExamImg) {
        return null;
    }

    @Override
    public Bitmap createFeedbackImage(Bitmap bitmap, float[] lefts, float[] tops,int[] selections, int[] ids, String s, Boolean[] wasConflicted) {
        return null;
    }

    @Override
    public Bitmap createFailFeedbackImage(Bitmap bitmap) {
        return bitmap;
    }
}
