package com.example.examscanner.stubs;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;

import com.example.examscanner.image_processing.DetectCornersConsumer;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ScanAnswersConsumer;

public class ImageProcessorStubFactory {
    public static ImageProcessingFacade alignmentSubIp(Bitmap bm, ImageProcessingFacade real){
        return new ImageProcessingFacade() {
            @Override
            public void detectCorners(Bitmap bm, DetectCornersConsumer consumer) {
                consumer.consume(new PointF(0,0),new PointF(0,0),new PointF(0,0),new PointF(0,0));
            }

            @Override
            public Bitmap transformToRectangle(Bitmap bitmap, Point upperLeft, Point upperRight, Point bottomRight, Point bottomLeft) {
                return bitmap;
            }

            @Override
            public void scanAnswers(Bitmap bitmap, int amountOfQuestions, ScanAnswersConsumer consumer) {
                real.scanAnswers(bitmap,amountOfQuestions,consumer);
            }

            @Override
            public void scanAnswers(Bitmap bitmap, int amountOfQuestions, ScanAnswersConsumer consumer, float[] leftMostXs, float[] upperMostYs) {
                real.scanAnswers(bitmap, amountOfQuestions, consumer, leftMostXs, upperMostYs);
            }

            @Override
            public void scanAnswers(Bitmap bitmap, ScanAnswersConsumer consumer) {
                real.scanAnswers(bitmap, consumer);
            }

            @Override
            public Bitmap align(Bitmap bitmap, Bitmap perfectExamImg) {
                return  bm;
            }

            @Override
            public Bitmap createFeedbackImage(Bitmap bitmap, float[] lefts, float[] tops,int[] selections, int[] ids, String s, Boolean[] wasConflicted) {
                return real.createFeedbackImage(bitmap, lefts, tops, selections, ids, s, wasConflicted);
            }

            @Override
            public Bitmap createFailFeedbackImage(Bitmap bitmap) {
                return bitmap;
            }
        };
    }
}
