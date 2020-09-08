package com.example.examscanner.image_processing;
import android.graphics.Bitmap;
import android.graphics.Point;

public interface ImageProcessingFacade {
    public void detectCorners(Bitmap bm, DetectCornersConsumer consumer);
    public Bitmap transformToRectangle(Bitmap bitmap, Point upperLeft, Point upperRight, Point bottomRight, Point bottomLeft);
    public void scanAnswers(Bitmap bitmap, int amountOfQuestions, ScanAnswersConsumer consumer);
    public void scanAnswers(Bitmap bitmap, int amountOfQuestions, ScanAnswersConsumer consumer, float[] leftMostXs, float[] upperMostYs);
    public void scanAnswers(Bitmap bitmap, ScanAnswersConsumer consumer);
    public Bitmap align(Bitmap bitmap, Bitmap perfectExamImg);
    public Bitmap createFeedbackImage(Bitmap bitmap, float[] lefts, float[] tops,int[] selections,int[] ids, String examineeId, Boolean[] wasConflicted);
    public Bitmap createFailFeedbackImage(Bitmap bitmap);
}


