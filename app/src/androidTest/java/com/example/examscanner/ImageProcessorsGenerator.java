package com.example.examscanner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;

import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.components.scan_exam.reslove_answers.ScannedCapturesInstancesFactory;
import com.example.examscanner.image_processing.DetectCornersConsumer;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.image_processing.ImageProcessor;
import com.example.examscanner.image_processing.ScanAnswersConsumer;

public class ImageProcessorsGenerator {
    private static Context c;

    public static ImageProcessingFacade nullIP(){
        return new ImageProcessingFacade() {
            @Override
            public void detectCorners(Bitmap bm, DetectCornersConsumer consumer) {
                consumer.consume(new PointF(),new PointF(),new PointF(),new PointF());
            }

            @Override
            public Bitmap transformToRectangle(Bitmap bitmap, Point upperLeft, Point upperRight, Point bottomRight, Point bottomLeft) {
                return bitmap;
            }

            @Override
            public void scanAnswers(Bitmap bitmap, int amountOfQuestions, ScanAnswersConsumer consumer) {
                consumer.consume(0,null,null,null,null,null,null);
            }

            @Override
            public void scanAnswers(Bitmap bitmap, int amountOfQuestions, ScanAnswersConsumer consumer, float[] leftMostXs, float[] upperMostYs) {

            }

            @Override
            public void scanAnswers(Bitmap bitmap, ScanAnswersConsumer consumer) {
                consumer.consume(0,null,null,null,null,null,null);
            }

            @Override
            public Bitmap align(Bitmap bitmap, Bitmap perfectExamImg) {
                return bitmap;
            }

            @Override
            public Bitmap createFeedbackImage(Bitmap bitmap, float[] lefts, float[] tops,int[] selections, int[] ids, String s, Boolean[] wasConflicted) {
                return bitmap;
            }

            @Override
            public Bitmap createFailFeedbackImage(Bitmap bitmap) {
                return bitmap;
            }
        };
    }
    public static ImageProcessingFacade slowIP(){
        return new ImageProcessingFacade() {
            @Override
            public void detectCorners(Bitmap bm, DetectCornersConsumer consumer) {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                consumer.consume(new PointF(0,0),new PointF(0,0),new PointF(0,0),new PointF(0,0));
            }

            @Override
            public Bitmap transformToRectangle(Bitmap bitmap, Point upperLeft, Point upperRight, Point bottomRight, Point bottomLeft) {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return BitmapsInstancesFactoryAndroidTest.getTestJpg1();
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
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ScannedCapturesInstancesFactory.instance1(consumer);
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
                return bitmap;
            }

            @Override
            public Bitmap createFeedbackImage(Bitmap bitmap, float[] lefts, float[] tops,int[] selections, int[] ids, String s, Boolean[] wasConflicted) {
                return bitmap;
            }

            @Override
            public Bitmap createFailFeedbackImage(Bitmap bitmap) {
                return bitmap;
            }
        };
    }
    public static ImageProcessingFacade quickIP(){
        return new ImageProcessingFacade() {
            @Override
            public void detectCorners(Bitmap bm, DetectCornersConsumer consumer) {
                consumer.consume(new PointF(),null,null,new PointF());
            }

            @Override
            public Bitmap transformToRectangle(Bitmap bitmap, Point upperLeft, Point upperRight, Point bottomRight, Point bottomLeft) {
                return BitmapsInstancesFactoryAndroidTest.getRandom();
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
                return bitmap;
            }

            @Override
            public Bitmap createFailFeedbackImage(Bitmap bitmap) {
                return bitmap;
            }
        };
    }

    public static ImageProcessingFacade fakeIP(){
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
                ScannedCapturesInstancesFactory.instance1(consumer);
            }

            @Override
            public void scanAnswers(Bitmap bitmap, int amountOfQuestions, ScanAnswersConsumer consumer, float[] leftMostXs, float[] upperMostYs) {
                ScannedCapturesInstancesFactory.instance1(consumer);
            }

            @Override
            public void scanAnswers(Bitmap bitmap, ScanAnswersConsumer consumer) {
                ScannedCapturesInstancesFactory.instance1(consumer);
            }

            @Override
            public Bitmap align(Bitmap bitmap, Bitmap perfectExamImg) {
                return bitmap;
            }

            @Override
            public Bitmap createFeedbackImage(Bitmap bitmap, float[] lefts, float[] tops,int[] selections, int[] ids, String s, Boolean[] wasConflicted) {
                return bitmap;
            }

            @Override
            public Bitmap createFailFeedbackImage(Bitmap bitmap) {
                return bitmap;
            }
        };
    }

    public static ImageProcessingFacade halfFakeIP(Context c){
        ImageProcessingFacade real = new ImageProcessor(c);
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
                ScanAnswersConsumer wConsumer = new ScanAnswersConsumer() {
                    @Override
                    public void consume(int numOfAnswersDetected, int[] answersIds, float[] lefts, float[] tops, float[] rights, float[] bottoms, int[] selections) {
                        selections[5] = -1;
                        selections[8] = -1;
                        consumer.consume(numOfAnswersDetected, answersIds, lefts, tops, rights, bottoms, selections);
                    }
                };
                real.scanAnswers(bitmap, amountOfQuestions, wConsumer, leftMostXs, upperMostYs);
            }

            @Override
            public void scanAnswers(Bitmap bitmap, ScanAnswersConsumer consumer) {
                ScannedCapturesInstancesFactory.instance1(consumer);
            }

            @Override
            public Bitmap align(Bitmap bitmap, Bitmap perfectExamImg) {
                return real.align(bitmap, perfectExamImg);
            }

            @Override
            public Bitmap createFeedbackImage(Bitmap bitmap, float[] lefts, float[] tops,int[] selections, int[] ids, String s, Boolean[] wasConflicted) {
                return real.createFeedbackImage(bitmap, lefts, tops, selections, ids, s,wasConflicted);
            }

            @Override
            public Bitmap createFailFeedbackImage(Bitmap bitmap) {
                return bitmap;
            }
        };
    }
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
                return real.createFeedbackImage(bitmap, lefts, tops, selections, ids, s,wasConflicted);
            }

            @Override
            public Bitmap createFailFeedbackImage(Bitmap bitmap) {
                return bitmap;
            }
        };
    }
}
