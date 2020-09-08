package com.example.examscanner.stubs;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;

import com.example.examscanner.MainActivity;
import com.example.examscanner.image_processing.DetectCornersConsumer;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ScanAnswersConsumer;

public class StubImageProcessingFactory {
    private static BitmapInstancesFactory bmFact;
    public static ImageProcessingFacade create(MainActivity mainActivity) {
        bmFact = new BitmapInstancesFactory(mainActivity);
        return new ImageProcessingFacade() {
            @Override
            public void detectCorners(Bitmap bm, DetectCornersConsumer consumer) {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                consumer.consume(new PointF(),null,null,new PointF());
            }

            @Override
            public Bitmap transformToRectangle(Bitmap bitmap, Point upperLeft, Point upperRight, Point bottomRight, Point bottomLeft) {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return bmFact.getRandom();
            }

            @Override
            public void scanAnswers(Bitmap bitmap, int amountOfQuestions, ScanAnswersConsumer consumer) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ScannedCapturesInstancesFactory.instance1(consumer);
            }

            @Override
            public void scanAnswers(Bitmap bitmap, int amountOfQuestions, ScanAnswersConsumer consumer, float[] leftMostXs, float[] upperMostYs) {

            }

            @Override
            public void scanAnswers(Bitmap bitmap, ScanAnswersConsumer consumer) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ScannedCapturesInstancesFactory.instance1(consumer);
            }

            @Override
            public Bitmap align(Bitmap bitmap, Bitmap perfectExamImg) {
                return null;
            }

            @Override
            public Bitmap createFeedbackImage(Bitmap bitmap, float[] lefts, float[] tops,int[] selections, int[] ids, String s, Boolean[] wasConflicted) {
                return bmFact.getRandom();
            }

            @Override
            public Bitmap createFailFeedbackImage(Bitmap bitmap) {
                return bitmap;
            }
        };
    }
}
