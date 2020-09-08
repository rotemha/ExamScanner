package com.example.examscanner.image_processing;

import android.graphics.Bitmap;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class FineAdjustment implements Comparable<FineAdjustment> {
    private final static int XDELTA = 20;
    private final static int YDELTA = 10;
    private int xFineAdjustment;
    private int yFineAdjustment;
    private Score score;

    private FineAdjustment(int xFineAdjustment, int yFineAdjustment, Score score) {
        this.xFineAdjustment = xFineAdjustment;
        this.yFineAdjustment = yFineAdjustment;
        this.score =score;
    }

    public static FineAdjustment create(Mat mat, int left, int up, int rWidth, int rHeight, Scalar mean) {
        FineAdjustment theAdjustment = new FineAdjustment(0,0, Score.create(mat,left, up, rWidth, rHeight,mean));
        for (int x = left- XDELTA; x < left+XDELTA ; x++) {
            for (int y = up- YDELTA; y < up+YDELTA ; y++) {
                Score currentScore = Score.create(mat,x, y, rWidth, rHeight,mean);
                FineAdjustment candidate = new FineAdjustment(x-left,y-up, currentScore);
                if(theAdjustment.compareTo(candidate)<=0){
                    theAdjustment = candidate;
                }
            }
        }
//        helperAsjViewr( left, up, theAdjustment,  mat,rWidth,  rHeight);
        //        helperAsjViewr( left, up, candidate,  mat,rWidth,  rHeight);
        return theAdjustment;
    }

    private static Bitmap helperAsjViewr(int left, int y, FineAdjustment fa, Mat mat,int rWidth, int rHeight){
        return ImageProcessor.helperMatToBitmap(mat.submat(new Rect(left+fa.getXAdj(), y+fa.getYAdj(), rWidth, rHeight)));
    }

    private static Bitmap helper2Bm(Mat m){
        return ImageProcessor.helperMatToBitmap(m);
    }

    @Override
    public int compareTo(FineAdjustment o) {
        return score.compareTo(o.score);
    }

    public int getXAdj() {
        return xFineAdjustment;
    }

    public int getYAdj() {
        return yFineAdjustment;
    }

    private static class Score implements Comparable<Score>{
        private int numOfBlacks = 0;

        public Score(int blackPixels) {
            numOfBlacks  = blackPixels;
        }

        public static Score create(Mat mat, int left, int up, int rWidth, int rHeight, Scalar mean) {
            Rect rect = new Rect( left, up, rWidth , rHeight );
            Mat subMat = mat.submat(rect);
            Imgproc.cvtColor(subMat, subMat, Imgproc.COLOR_BGR2GRAY);
            Mat cloneONLYFORTESTING = subMat.clone();
            Core.inRange(subMat, mean, ImageProcessor.param_UPPER_BOUND_BLACKS_COUNTING, subMat);
//            Core.bitwise_not(subMat, subMat);
            int non_black_pixels = Core.countNonZero(subMat);
            int black_pixels = subMat.rows() * subMat.cols() - non_black_pixels;
            return new Score(black_pixels);
        }

        @Override
        public int compareTo(Score o) {
            return Integer.compare(numOfBlacks, o.numOfBlacks);
        }
    }
    private static void helperPrintMat(Mat mat){
        for (int i = 0; i < mat.cols(); i++) {
            for (int j = 0; j < mat.rows(); j++) {
                System.out.println(mat.get(j,i)[0]);
            }
            System.out.println();
        }
    }
}
