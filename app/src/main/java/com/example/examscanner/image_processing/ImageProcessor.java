//package com.example.examscanner.image_processing;
//
//import android.graphics.Bitmap;
//import android.graphics.PointF;
//import android.os.Build;
//
//import androidx.annotation.RequiresApi;
//
//import com.example.examscanner.stubs.BitmapInstancesFactory;
//
//import org.opencv.android.Utils;
//import org.opencv.core.Core;
//import org.opencv.core.CvType;
//import org.opencv.core.Mat;
//import org.opencv.core.MatOfKeyPoint;
//import org.opencv.core.Point;
//import org.opencv.core.Rect;
//import org.opencv.core.Scalar;
//import org.opencv.core.Size;
//import org.opencv.features2d.FastFeatureDetector;
//import org.opencv.imgproc.Imgproc;
//
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//
//
//public class ImageProcessor implements ImageProcessingFacade {
//    private BitmapInstancesFactory bmFact;//TODO - remove dependecy
//    public ImageProcessor(BitmapInstancesFactory bmFact) {
//        this.bmFact = bmFact;
//    }
//
//
//    // given an image of an exam try to detect the 4 corners of the exam
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public void detectCorners(Bitmap bm, DetectCornersConsumer consumer){
//        Mat mat = new Mat();
//        Bitmap bmp32 = bm.copy(Bitmap.Config.ARGB_8888, true);
//        Utils.bitmapToMat(bmp32, mat);
//        // First find all the corners in the given image
//        List<Point> points = cornerDetection(mat);
//        // Then, supposing a rectangle exists, find its 4 corners coordinates
//        List<Point> filtered = filterPoints(points);
//        List<Point> clockwiseOrderedPoints = orderPoints(filtered);
//        Point upperLeft = clockwiseOrderedPoints.get(0);
//        Point upperRight = clockwiseOrderedPoints.get(1);
//        Point bottomRight = clockwiseOrderedPoints.get(2);
//        Point bottomLeft = clockwiseOrderedPoints.get(3);
//        consumer.consume(
//                new PointF((float)upperLeft.x, (float) upperLeft.y),
//                new PointF((float)upperRight.x,(float)upperRight.y),
//                new PointF((float)bottomLeft.x,(float)bottomLeft.y),
//                new PointF((float)bottomRight.x,(float)bottomRight.y)
//        );
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    private List<Point> orderPoints(List<Point> points) {
//        List<Point> ordered = new ArrayList<>();
//        double maxPointSum = Collections.max(points.stream().map(point -> point.x + point.y).collect(Collectors.toList()));
//        double minPointSum = Collections.min(points.stream().map(point -> point.x + point.y).collect(Collectors.toList()));
//        Point bottomRight = points.stream().filter(point -> point.x + point.y == maxPointSum).collect(Collectors.toList()).get(0);
//        Point topLeft = points.stream().filter(point -> point.x + point.y == minPointSum).collect(Collectors.toList()).get(0);
//        ordered.add(0, topLeft);
//        ordered.add(2, bottomRight);
//        double maxPointDiff = Collections.max(points.stream().map(point -> point.y - point.x).collect(Collectors.toList()));
//        double minPointDiff = Collections.min(points.stream().map(point -> point.y - point.x).collect(Collectors.toList()));
//        Point bottomLeft = points.stream().filter(point -> point.x + point.y == maxPointDiff).collect(Collectors.toList()).get(0);
//        Point topRight = points.stream().filter(point -> point.x + point.y == minPointDiff).collect(Collectors.toList()).get(0);
//        ordered.add(1, topRight);
//        ordered.add(3, bottomLeft);
//        return ordered;
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    @Override
//    public Bitmap transformToRectangle(Bitmap bitmap, android.graphics.Point upperLeft, android.graphics.Point upperRight, android.graphics.Point bottomRight, android.graphics.Point bottomLeft) {
//
//        Mat inputMat = new Mat();
//        Bitmap bmp32 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//        Utils.bitmapToMat(bmp32, inputMat);
//
//        double widthCandidateA = Math.sqrt(Math.pow((bottomRight.x - bottomLeft.x), 2) + Math.pow((bottomRight.y - bottomLeft.y), 2));
//        double widthCandidateB = Math.sqrt(Math.pow((upperRight.x - upperLeft.x), 2) + Math.pow((upperRight.y - upperLeft.y), 2));
//        double newWidth = Math.max(widthCandidateA, widthCandidateB);
//
//        double heightCandidateA = Math.sqrt(Math.pow((upperRight.x - bottomRight.x), 2) + Math.pow((upperRight.y - bottomRight.y), 2));
//        double heightCandidateB = Math.sqrt(Math.pow((upperLeft.x - bottomLeft.x), 2) + Math.pow((upperLeft.y - bottomLeft.y), 2));
//        double newHeight = Math.max(heightCandidateA, heightCandidateB);
//
//
//        // compute the perspective transform matrix and then apply it
////         dst = [(0,0), (newWidth-1,0), (newWidth-1, newHeight-1), (0, newHeight-1)]
////         M = cv2.getPerspectiveTransform(rect, dst)
////         warped = cv2.warpPerspective(image, M, (maxWidth, maxHeight))
//
//        Mat src = new Mat(4,1,CvType.CV_32FC2);
//        src.put((int)upperLeft.y,(int)upperLeft.x, (int)upperRight.y,(int)upperRight.x, (int)bottomLeft.y,(int)bottomLeft.x, (int)bottomRight.y,(int)bottomRight.x);
//        Mat dst = new Mat(4,1,CvType.CV_32FC2);
//        dst.put(0,0, 0, inputMat.width(), inputMat.height(), inputMat.width(), inputMat.height(),0);
//
//        Mat perspectiveTransform = Imgproc.getPerspectiveTransform(src, dst);
//        Mat transformedMat = inputMat.clone();
//        Imgproc.warpPerspective(inputMat, transformedMat, perspectiveTransform, new Size(inputMat.width(), inputMat.height()));
//
//        Bitmap transformedBitmap = Bitmap.createBitmap(transformedMat.cols(), transformedMat.rows(), Bitmap.Config.ARGB_8888);
//        Utils.matToBitmap(transformedMat, transformedBitmap);
//        return transformedBitmap;
//    }
//
//    private Point getBottomRight(List<Point> points) {
//        PointComparator pointComp = new PointComparator();
//        Collections.sort(points, pointComp);
//        return (points.get(2).x < points.get(3).x) ? points.get(3): points.get(2);
//    }
//
//    private Point getBottomLeft(List<Point> points) {
//        PointComparator pointComp = new PointComparator();
//        Collections.sort(points, pointComp);
//        return (points.get(2).x < points.get(3).x) ? points.get(2): points.get(3);
//    }
//
//    private Point getUpperLeft(List<Point> points) {
//        PointComparator pointComp = new PointComparator();
//        Collections.sort(points, pointComp);
//        return (points.get(0).x < points.get(1).x) ? points.get(0): points.get(1);
//
//    }
//
//    private Point getUpperRight(List<Point> points) {
//        PointComparator pointComp = new PointComparator();
//        Collections.sort(points, pointComp);
//        return (points.get(0).x < points.get(1).x) ? points.get(1): points.get(0);
//
//    }
//
//    class PointComparator implements Comparator {
//        public int compare(Object o1,Object o2){
//            Point p1=(Point)o1;
//            Point p2=(Point)o2;
//
//            if(p1.x == p2.x && p1.y == p2.y)
//                return 0;
//            else if(p1.y > p2.y)
//                return 1;
//            else if(p1.y == p2.y && p1.x > p2.x)
//                return 1;
//            else
//                return -1;
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    private List<Point> filterPoints(List<Point> points) {
//        List<Point> pointsWithoutDuplicates = new ArrayList<>();
//        int i = 0;
//        for(Point p : points){
//            if(pointsWithoutDuplicates.stream().filter(point -> point.x == p.x).collect(Collectors.toList()).isEmpty()) {
//                pointsWithoutDuplicates.add(p);
//                i++;
//                if(i == 4)
//                    break;
//            }
//        }
//        return pointsWithoutDuplicates;
//    }
//
//
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public void scanAnswers(Bitmap bitmap, int[] leftMostXs, int[] upperMostYs, ScanAnswersConsumer consumer){
//
//        Mat exam = new Mat();
//        Bitmap bmp32 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//        Utils.bitmapToMat(bmp32, exam);
////        Mat img_template = Imgcodecs.imread("template.png");
//
//        Bitmap bm = bmFact.getTestTemplate1();
//        Mat img_template = new Mat();
//        Utils.bitmapToMat(bm, img_template);
//        int amountOfQuestions = leftMostXs.length;
//        int answersIds[] = new int[amountOfQuestions];
//        float lefts[] = new float[amountOfQuestions];
//        float tops[] = new float[amountOfQuestions];
//        float rights[] = new float[amountOfQuestions];
//        float bottoms[] = new float[amountOfQuestions];
//        int selections[] = new int[amountOfQuestions];
//        Map<Point, Integer> answersMap = findQuestions(exam, img_template, leftMostXs, upperMostYs);
//        sortQuestions(answersMap, answersIds, lefts, tops, rights, bottoms, selections, img_template.cols(), img_template. rows(), exam.cols(), exam.rows());
//        consumer.consume(amountOfQuestions, answersIds, lefts, tops, rights, bottoms, selections);
//    }
//
//
//    public void scanAnswers(Bitmap bitmap, int amountOfQuestions, ScanAnswersConsumer consumer){
//
//        Mat exam = new Mat();
//        Bitmap bmp32 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//        Utils.bitmapToMat(bmp32, exam);
////        Mat img_template = Imgcodecs.imread("template.png");
//
//        Bitmap bm = bmFact.getTestTemplate1();
//        Mat img_template = new Mat();
//        Utils.bitmapToMat(bm, img_template);
//        int answersIds[] = new int[amountOfQuestions];
//        float lefts[] = new float[amountOfQuestions];
//        float tops[] = new float[amountOfQuestions];
//        float rights[] = new float[amountOfQuestions];
//        float bottoms[] = new float[amountOfQuestions];
//        int selections[] = new int[amountOfQuestions];
//        Map<Point, Integer> answersMap = findQuestions(exam, img_template, amountOfQuestions);
//        sortQuestions(answersMap, answersIds, lefts, tops, rights, bottoms, selections, img_template.cols(), img_template. rows(), exam.cols(), exam.rows());
//        consumer.consume(amountOfQuestions, answersIds, lefts, tops, rights, bottoms, selections);
//
//    }
//
//    @Override
//    public void scanAnswers(Bitmap bitmap, int amountOfQuestions, ScanAnswersConsumer consumer, int[] leftMostXs, int[] upperMostYs) {
//
//    }
//
//    public void scanAnswers(Bitmap bitmap, ScanAnswersConsumer consumer){
//        Mat exam = new Mat();
//        Bitmap bmp32 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//        Utils.bitmapToMat(bmp32, exam);
//        Bitmap bm = bmFact.getTestTemplate1();
//        Mat img_template = new Mat();
//        Utils.bitmapToMat(bm, img_template);
////        Mat img_template = Imgcodecs.imread("template.png");
//        Map<Point, Integer> answersMap = findQuestions(exam, img_template);
//        int amountOfQuestions = answersMap.size();
//        int answersIds[] = new int[amountOfQuestions];
//        float lefts[] = new float[amountOfQuestions];
//        float tops[] = new float[amountOfQuestions];
//        float rights[] = new float[amountOfQuestions];
//        float bottoms[] = new float[amountOfQuestions];
//        int selections[] = new int[amountOfQuestions];
//        sortQuestions(answersMap, answersIds, lefts, tops, rights, bottoms, selections, img_template.cols(), img_template. rows(), exam.cols(), exam.rows());
//        consumer.consume(amountOfQuestions, answersIds, lefts, tops, rights, bottoms, selections);
//    }
//
//
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    private Map<Point, Integer> findQuestions(Mat img_exam, Mat img_template, int[] leftMostXs, int[] upperMostYs) {
//
//        int numOfQuestions = leftMostXs.length;
//        int unIdentifiedThreshold = (int) Math.ceil(numOfQuestions*0.2);
//        Map<Point,Integer> answersMap = new HashMap<>();
//        for(int i=0; i<numOfQuestions; i++){
//            Point matchLoc = new Point(leftMostXs[i], upperMostYs[i]);
//            Rect rect = new Rect((int)matchLoc.x, (int)matchLoc.y, img_template.cols(), img_template.rows());
//            Mat currAns = img_exam.submat(rect);
//            List<Mat> imgChunks = splitImage2(currAns,5);
//            int correctAns = markedAnswer2(imgChunks);
//            answersMap.put(matchLoc, correctAns);
//        }
//
//        List<Integer> unidentified = answersMap.values().stream().filter(x -> x == -1).collect(Collectors.toList());
//        if(unidentified.size() > unIdentifiedThreshold){
//            answersMap = findQuestions(img_exam, img_template, numOfQuestions);
//        }
//
//        return answersMap;
//    }
//
//    private int markedAnswer2(List<Mat> imgChunks) {
//        int maxBlack = 0;
//        int maxImg = 0;
//        for(int i = 0; i < imgChunks.size(); i++){
//            Mat currMat = imgChunks.get(i);
//            //CONVERT IMAGE TO B&W AND DETERMINE IF IT HAS MORE BLACK OR WHITE
//            Imgproc.cvtColor(currMat, currMat, Imgproc.COLOR_BGR2GRAY);
//            int non_black_pixels = Core.countNonZero(currMat);
//            int black_pixels = currMat.rows() * currMat.cols() - non_black_pixels;
//            if(maxBlack < black_pixels){
//                maxBlack = black_pixels;
//                maxImg = i+1;
//            }
//        }
//
//        return maxImg;
//    }
//
//    private List<Mat> splitImage2(Mat mat, int chunkNumbers) {
//
//        List<Mat> chunkedImages = new ArrayList<Mat>(chunkNumbers);
//        int sizeOfPart = mat.cols()/chunkNumbers;
//        for(int i = 0; i < chunkNumbers; i++){
//            Rect rect = new Rect((int)i*sizeOfPart, 0, sizeOfPart, mat.rows());
//            Mat ith_ans = mat.submat(rect);
//            chunkedImages.add(ith_ans);
//        }
//        return chunkedImages;
//    }
//
//    public Map<Point,Integer> findQuestions(Mat img_exam, Mat img_template, int numOfQuestions)
// {
//
//        int result_cols = img_exam.cols() - img_template.cols() + 1;
//        int result_rows = img_exam.rows() - img_template.rows() + 1;
//        Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1); //CV_32FC1 means 32 bit floating point signed depth in one channel
//
//        // / Do the Matching and Normalize
//        Imgproc.matchTemplate(img_exam, img_template, result, Imgproc.TM_CCOEFF_NORMED);
//        //     Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());
//
//
//        Core.MinMaxLocResult mmr;
//        Point matchLoc;
//        Map<Point,Integer> answersMap = new HashMap<>();
//        int i = 0;
//
//        while(true)
//        {
//            i++;
//            mmr = Core.minMaxLoc(result);
//            // match loc is the left upper point of the found template
//            // x + -> right direction
//            // y + -> down direction
//            matchLoc = mmr.maxLoc;
//            // threshold chosen empirically
//            if(i <= numOfQuestions)
//            {
//                Mat img_result = img_exam.clone();
//                Imgproc.rectangle(img_exam, matchLoc, new Point(matchLoc.x + img_template.cols(),
//                        matchLoc.y + img_template.rows()), new Scalar(0, 0, 255), -1);
//                // find the answer
//                // part the matching image into 5
//                Bitmap img_bitmap = Bitmap.createBitmap(img_result.cols(), img_result.rows(),Bitmap.Config.ARGB_8888);
//                Utils.matToBitmap(img_result, img_bitmap);
//                Bitmap bm = Bitmap.createBitmap(img_bitmap, (int)matchLoc.x,(int)matchLoc.y, img_template.cols(), img_template.rows());
//                ArrayList<Bitmap> imgChunks = splitImage(bm,5);
//                int correctAns = markedAnswer(imgChunks);
//                answersMap.put(matchLoc, correctAns+1);
//                // erase this matching to prevent infinite loop
//                Imgproc.rectangle(result, matchLoc, new Point(matchLoc.x + img_template.cols(),
//                        matchLoc.y + img_template.rows()), new Scalar(0, 0, 0), -1);
//            }
//            else
//            {
//                break; // No more results within tolerance, break search
//            }
//        }
//        return answersMap;
//
//
//    }
//
//
//
//    private Map<Point,Integer> findQuestions(Mat img_exam, Mat img_template) {
//        int result_cols = img_exam.cols() - img_template.cols() + 1;
//        int result_rows = img_exam.rows() - img_template.rows() + 1;
//        Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1); //CV_32FC1 means 32 bit floating point signed depth in one channel
//
//        // / Do the Matching and Normalize
//        Imgproc.matchTemplate(img_exam, img_template, result, Imgproc.TM_CCOEFF_NORMED);
//        //     Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());
//
//
//        Core.MinMaxLocResult mmr;
//        Point matchLoc;
//        Map<Point,Integer> answersMap = new HashMap<>();
//
//        while(true)
//        {
//            mmr = Core.minMaxLoc(result);
//            // match loc is the left upper point of the found template
//            // x + -> right direction
//            // y + -> down direction
//            matchLoc = mmr.maxLoc;
//            // threshold chosen empirically
//            if(mmr.maxVal >=0.67)
//            {
//                Mat img_result = img_exam.clone();
//                Imgproc.rectangle(img_exam, matchLoc, new Point(matchLoc.x + img_template.cols(),
//                        matchLoc.y + img_template.rows()), new Scalar(0, 0, 255), -1);
//                // find the answer
//                // part the matching image into 5
//                Bitmap img_bitmap = Bitmap.createBitmap(img_result.cols(), img_result.rows(),Bitmap.Config.ARGB_8888);
//                Utils.matToBitmap(img_result, img_bitmap);
//                Bitmap bm = Bitmap.createBitmap(img_bitmap, (int)matchLoc.x,(int)matchLoc.y, img_template.cols(), img_template.rows());
//                ArrayList<Bitmap> imgChunks = splitImage(bm,5);
//                int correctAns = markedAnswer(imgChunks);
//                answersMap.put(matchLoc, correctAns+1);
//                // erase this matching to prevent infinite loop
//                Imgproc.rectangle(result, matchLoc, new Point(matchLoc.x + img_template.cols(),
//                        matchLoc.y + img_template.rows()), new Scalar(0, 0, 0), -1);
//
////                Point diagonalPoint = new Point(matchLoc.x + img_template.cols(), matchLoc.y + img_template.rows());
////                Rect rect = new Rect((int)matchLoc.x, (int)matchLoc.y, img_template.cols(), img_template.rows());
////                Mat currAns = img_exam.submat(rect);
////                ArrayList<Bitmap> imgChunks = splitImage(bm,5);
////                int correctAns = markedAnswer(imgChunks);
////                answersMap.put(matchLoc, correctAns);
////                // draw rectangle around the found template in the output picture (red)
////                Imgproc.rectangle(img_exam, matchLoc, diagonalPoint, new Scalar(0, 0, 255), 2);
////                // erase this matching in order to find next matching (black)
////                Imgproc.rectangle(result, matchLoc, diagonalPoint, new Scalar(0, 0, 0), -1);
//            }
//            else
//            {
//                break; // No more results within tolerance, break search
//            }
//        }
//        return answersMap;
//
//
//    }
//
//
//
//    private void sortQuestions(Map<Point, Integer> answersMap, int[] answersIds,float[] lefts,float[] tops,float[] rights,float[] bottoms,int[] selections, int delta_x, int delta_y, int img_cols, int img_rows)
//    {
//        // sort points:
//        // x value is the most significant
//        class PointComparator implements Comparator {
//            public int compare(Object o1,Object o2){
//                Point p1=(Point)o1;
//                Point p2=(Point)o2;
//
//                if(p1.x == p2.x && p1.y == p2.y)
//                    return 0;
//                else if(p1.x > p2.x)
//                    return 1;
//                else if(p1.x == p2.x && p1.y > p2.y)
//                    return 1;
//                else
//                    return -1;
//            }
//        }
//
//        Comparator pointComp = new PointComparator();
//        Set<Point> keys = answersMap.keySet();
//        ArrayList<Point> keysList = new ArrayList<>(keys);
//        Collections.sort(keysList, pointComp);
//        Map<Integer, Integer> ret = new HashMap<>();
//        int q_num = 0;
//        for(Point p : keysList){
//            answersIds[q_num] = q_num+1;
//            selections[q_num] = answersMap.get(p);
//            lefts[q_num] = (float) (p.x/img_rows);
//            tops[q_num] = (float) (p.y/img_cols);
//            rights[q_num] = (float) ((p.x + delta_x)/img_rows);
//            bottoms[q_num] = (float) ((p.y + delta_y)/img_cols);
//            q_num++;
//        }
//    }
//
//
//    // find out which answer was marked by determining which sub-image in imagesArr
//    // has the bigget number of colored pixeles
//    private int markedAnswer(ArrayList<Bitmap> imgChunks) {
//
////        let maxBlack = 0;
////        let maxImg = 0;
////        for(let i = 0; i < imagesArr.length; i++){
////            let currAns = imagesArr[i];
////            //CONVERT IMAGE TO B&W AND DETERMINE IF IT HAS MORE BLACK OR WHITE
////            cv.cvtColor(currAns, currAns, cv.COLOR_BGR2GRAY);
////            let nonBlackPixels = cv.countNonZero(currAns);
////            let blackPixels = currAns.rows * currAns.cols - nonBlackPixels;
////            if(maxBlack < blackPixels){
////                maxBlack = blackPixels;
////                maxImg = i+1;
////            }
////        }
////
////        return maxImg;
//
//        int maxBlack = 0;
//        int maxImg = -2;
//        for(int i = 0; i < imgChunks.size(); i++){
//            Mat currMat = new Mat();
//            Bitmap currBitMap = imgChunks.get(i);
//            Utils.bitmapToMat(currBitMap, currMat);
//            //CONVERT IMAGE TO B&W AND DETERMINE IF IT HAS MORE BLACK OR WHITE
//            Imgproc.cvtColor(currMat, currMat, Imgproc.COLOR_BGR2GRAY);
//            int non_black_pixels = Core.countNonZero(currMat);
//            int black_pixels = currMat.rows() * currMat.cols() - non_black_pixels;
//            if(maxBlack < black_pixels){
//                maxBlack = black_pixels;
//                maxImg = i;
//            }
//        }
//
//        return maxImg;
//    }
//
//
//    // split source image into chunksNum smaller images
//    // split is done according to the image width
//    private ArrayList<Bitmap> splitImage(Bitmap bitmap, int chunkNumbers) {
//
//        ArrayList<Bitmap> chunkedImages = new ArrayList<Bitmap>(chunkNumbers);
//        int sizeOfPart = bitmap.getWidth()/chunkNumbers;
//        for(int i = 0; i < chunkNumbers; i++){
//            Bitmap cropped = Bitmap.createBitmap(bitmap,i*sizeOfPart,0,sizeOfPart, bitmap.getHeight());
//            chunkedImages.add(cropped);
//        }
//        return chunkedImages;
//    }
//
//    // Detect corners in the given image
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    private List<Point> cornerDetection(Mat inputImg){
//
//        MatOfKeyPoint pointsMat = new MatOfKeyPoint();
//        int threshold = 20;
//        boolean nonMaxSup = true;
//        // initiate FAST object with chosen values
//        //FastFeatureDetector fast = FastFeatureDetector.create(threshold, nonMaxSup, FastFeatureDetector.TYPE_7_12);
//        FastFeatureDetector fast = FastFeatureDetector.create();
//        // find and draw the keypoints
//        fast.detect(inputImg, pointsMat);
//
//        Scalar redColor = new Scalar(255, 0, 0);
//        Mat mRgba = inputImg.clone();
////        Features2d.drawKeypoints(mRgba, pointsMat, mRgba, redColor);
//        List<Point> points = pointsMat.toList().stream().map(p -> p.pt).collect(Collectors.toList());
//        return points;
//    }
//}

package com.example.examscanner.image_processing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.examscanner.R;
import com.example.examscanner.communication.ContextProvider;

import org.jetbrains.annotations.NotNull;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.FastFeatureDetector;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.opencv.imgproc.Imgproc.THRESH_BINARY;

public class ImageProcessor implements ImageProcessingFacade {

    Mat questionTemplate;
    final int thresh = 50;
    final int N = 11;
    final static Scalar param_LOW_BOUND_BLACKS_COUNTING = new Scalar(120);
    final static Scalar param_UPPER_BOUND_BLACKS_COUNTING = new Scalar(255);
    final static boolean param_USE_FINE_AJDUSTMENT = true;
    private static final int ORIGINAL_WIDTH = 2550;
    private static final int ORIGINAL_HEIGHT = 3300;
    private static final float F_ORIGINAL_WIDTH = 2550;
    private static final float F_ORIGINAL_HEIGHT = 3300;
    static final double LARGE_X_DISTANCE = 0.10;

    private Bitmap bitmapFromMat(Mat mat) {
        Bitmap bm = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, bm);
        return bm;
    }

    private Mat matFromBitmap(Bitmap bm) {
        Mat mat = new Mat();
        Utils.bitmapToMat(bm, mat);
        return mat;
    }


    private Mat loadFromResource(int file) {

        Mat img = null;
        try {
            img = Utils.loadResource(ContextProvider.get(), file);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return img;
    }


    private Bitmap loadFromAssets(String filename) {

        Bitmap bitmap = null;
        try {
            InputStream ims = ContextProvider.get().getAssets().open(filename);
            bitmap = BitmapFactory.decodeStream(ims);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public ImageProcessor() {

        questionTemplate = loadFromResource(R.drawable.template);
        Imgproc.cvtColor(questionTemplate, questionTemplate, Imgproc.COLOR_BGR2GRAY);
    }

    public ImageProcessor(Context c) {

        Mat img = null;
        try {
            img = Utils.loadResource(c, R.drawable.template);

        } catch (IOException e) {
            e.printStackTrace();
        }

        questionTemplate = img;
        Imgproc.cvtColor(questionTemplate, questionTemplate, Imgproc.COLOR_BGR2GRAY);
    }


    // given an image of an exam try to detect the 4 corners of the exam
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void detectCorners(Bitmap bm, DetectCornersConsumer consumer) {
        if (bm == null || consumer == null)
            throw new NullPointerException("given input is null");

        Mat mat = matFromBitmap(bm);
        // First find all the corners in the given image
        List<Point> points = detectCorners(mat);
        // Then, supposing a rectangle exists, find its 4 corners coordinates
        // List<Point> filtered = removePoints(points);
        List<Point> clockwiseOrderedPoints = orderPoints(points);
        Point bottomRight = clockwiseOrderedPoints.get(0);
        Point upperRight = clockwiseOrderedPoints.get(1);
        Point upperLeft = clockwiseOrderedPoints.get(2);
        Point bottomLeft = clockwiseOrderedPoints.get(3);
        consumer.consume(
                new PointF((float) upperLeft.x, (float) upperLeft.y),
                new PointF((float) upperRight.x, (float) upperRight.y),
                new PointF((float) bottomLeft.x, (float) bottomLeft.y),
                new PointF((float) bottomRight.x, (float) bottomRight.y)
        );

//                new PointF((float) upperLeft.x, (float) upperLeft.y),
//                new PointF((float) upperRight.x, (float) upperRight.y),
//                new PointF((float) bottomLeft.x, (float) bottomLeft.y),
//                new PointF((float) upperRight.x, (float) upperRight.y)
//        );

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    List<Point> orderPoints(List<Point> points) {
        List<Double> Xs = points.stream().map(p -> p.x).collect(Collectors.toList());
        List<Double> Ys = points.stream().map(p -> p.y).collect(Collectors.toList());
        double sumXs = Xs.stream().reduce(0.0, (subtotal, p) -> subtotal + p) / points.size();
        double sumYs = Ys.stream().reduce(0.0, (subtotal, p) -> subtotal + p) / points.size();
        class PointComparator implements Comparator {
            public int compare(Object o1, Object o2) {
                Point p1 = (Point) o1;
                Point p2 = (Point) o2;
                double first = (Math.atan2(p1.x - sumXs, p1.y - sumYs) + 2 * Math.PI) % (2 * Math.PI);
                double second = (Math.atan2(p2.x - sumXs, p2.y - sumYs) + 2 * Math.PI) % (2 * Math.PI);
                return (int) Math.ceil(first - second);
            }
        }
        PointComparator pc = new PointComparator();
        points.sort(pc);
        return points;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Bitmap transformToRectangle(Bitmap bitmap, android.graphics.Point upperLeft, android.graphics.Point upperRight, android.graphics.Point bottomRight, android.graphics.Point bottomLeft) {

        if (bitmap == null || upperLeft == null || upperRight == null || bottomLeft == null || bottomRight == null)
            throw new NullPointerException("given input is null");


        Mat inputMat = matFromBitmap(bitmap);

        double widthCandidateA = Math.sqrt(Math.pow((bottomRight.x - bottomLeft.x), 2) + Math.pow((bottomRight.y - bottomLeft.y), 2));
        double widthCandidateB = Math.sqrt(Math.pow((upperRight.x - upperLeft.x), 2) + Math.pow((upperRight.y - upperLeft.y), 2));
        double newWidth = Math.max(widthCandidateA, widthCandidateB);

        double heightCandidateA = Math.sqrt(Math.pow((upperRight.x - bottomRight.x), 2) + Math.pow((upperRight.y - bottomRight.y), 2));
        double heightCandidateB = Math.sqrt(Math.pow((upperLeft.x - bottomLeft.x), 2) + Math.pow((upperLeft.y - bottomLeft.y), 2));
        double newHeight = Math.max(heightCandidateA, heightCandidateB);

        // compute the perspective transform matrix and then apply it

        MatOfPoint2f src = new MatOfPoint2f(
                new Point(upperLeft.x, upperLeft.y),
                new Point(upperRight.x, upperRight.y),
                new Point(bottomRight.x, bottomRight.y),
                new Point(bottomLeft.x, bottomLeft.y));

        MatOfPoint2f dst = new MatOfPoint2f(
                new Point(0, 0),
                new Point(newWidth - 1, 0),
                new Point(newWidth - 1, newHeight - 1),
                new Point(0, newHeight - 1)
        );


//        Mat src = new Mat(4, 1, CvType.CV_32FC2);
//        src.put((int) upperLeft.y, (int) upperLeft.x, (int) upperRight.y, (int) upperRight.x, (int) bottomLeft.y, (int) bottomLeft.x, (int) bottomRight.y, (int) bottomRight.x);
//        Mat dst = new Mat(4, 1, CvType.CV_32FC2);
//        dst.put(0, 0, 0, inputMat.width(), inputMat.height(), inputMat.width(), inputMat.height(), 0);

        Mat perspectiveTransform = Imgproc.getPerspectiveTransform(src, dst);
        Mat transformedMat = new Mat();
        Imgproc.warpPerspective(inputMat, transformedMat, perspectiveTransform, new Size(newWidth, newHeight));

        Bitmap transformedBitmap = Bitmap.createBitmap(transformedMat.cols(), transformedMat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(transformedMat, transformedBitmap);
        return transformedBitmap;
    }


    private Point getBottomRight(List<Point> points) {
        PointComparator pointComp = new PointComparator();
        Collections.sort(points, pointComp);
        return (points.get(2).x < points.get(3).x) ? points.get(3) : points.get(2);
    }

    private Point getBottomLeft(List<Point> points) {
        PointComparator pointComp = new PointComparator();
        Collections.sort(points, pointComp);
        return (points.get(2).x < points.get(3).x) ? points.get(2) : points.get(3);
    }

    private Point getUpperLeft(List<Point> points) {
        PointComparator pointComp = new PointComparator();
        Collections.sort(points, pointComp);
        return (points.get(0).x < points.get(1).x) ? points.get(0) : points.get(1);

    }

    private Point getUpperRight(List<Point> points) {
        PointComparator pointComp = new PointComparator();
        Collections.sort(points, pointComp);
        return (points.get(0).x < points.get(1).x) ? points.get(1) : points.get(0);

    }

    class PointComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            Point p1 = (Point) o1;
            Point p2 = (Point) o2;

            if (p1.x == p2.x && p1.y == p2.y)
                return 0;
            else if (p1.y > p2.y)
                return 1;
            else if (p1.y == p2.y && p1.x > p2.x)
                return 1;
            else
                return -1;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    List<Point> filterPoints(List<Point> points) {
        List<Point> pointsWithoutDuplicates = new ArrayList<>();
        int i = 0;
        for (Point p : points) {
            if (pointsWithoutDuplicates.stream().filter(point -> point.x == p.x).collect(Collectors.toList()).isEmpty()) {
                pointsWithoutDuplicates.add(p);
                i++;
                if (i == 4)
                    break;
            }
        }
        return pointsWithoutDuplicates;
    }

    private static double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
    }

    List<Point> removePoints(List<Point> points) {
        int thresh = 50;

        int n = points.size();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                Point point1 = points.get(i);
                Point point2 = points.get(j);
                if (distance(point1, point2) < thresh) {
                    points.remove(point2);
                    j--;
                    n--;
                }
            }
        }
        return points;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void scanAnswers(Bitmap bitmap, int amountOfQuestions, ScanAnswersConsumer consumer, float[] leftMostXs, float[] upperMostYs) {

        if (bitmap == null || leftMostXs == null || upperMostYs == null || consumer == null)
            throw new ImageProcessingError("given input is null");

        try{
            Mat scaledTemplate = questionTemplate.clone();
            final float xScaleConcreteToOrig = (float) bitmap.getWidth() / (float) ORIGINAL_WIDTH;
            final float yScaleConcreteToOrig = (float) bitmap.getHeight() / (float) ORIGINAL_HEIGHT;
            Imgproc.resize(
                    scaledTemplate,
                    scaledTemplate,
                    new Size(
                            (scaledTemplate.width() * xScaleConcreteToOrig),
                            scaledTemplate.height() * yScaleConcreteToOrig
                    )
            );
            int[] scaledLefts = new int[leftMostXs.length];
            int[] scaledUps = new int[upperMostYs.length];
            for (int i = 0; i < leftMostXs.length; i++) {
                scaledLefts[i] = (int) (leftMostXs[i] * bitmap.getWidth());
                scaledUps[i] = (int) (upperMostYs[i] * bitmap.getHeight());
            }
            Mat exam = matFromBitmap(bitmap);
            int answersIds[] = new int[amountOfQuestions];
            float lefts[] = new float[amountOfQuestions];
            float tops[] = new float[amountOfQuestions];
            float rights[] = new float[amountOfQuestions];
            float bottoms[] = new float[amountOfQuestions];
            int selections[] = new int[amountOfQuestions];
            Map<Point, Integer> answersMap = findQuestions(exam, scaledTemplate, scaledLefts, scaledUps);
            sortQuestions(answersMap, answersIds, lefts, tops, rights, bottoms, selections, scaledTemplate.cols(), scaledTemplate.rows(), exam.cols(), exam.rows());
            consumer.consume(amountOfQuestions, answersIds, lefts, tops, rights, bottoms, selections);
        }catch (Exception e){
            throw new ImageProcessingError(e);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void scanAnswers(Bitmap bitmap, int amountOfQuestions, ScanAnswersConsumer consumer) {

        if (bitmap == null )
            throw new NullPointerException("bitmap null");
        if (amountOfQuestions <= 0 )
            throw new NullPointerException("amountOfQuestions <= 0");
        if (consumer == null)
            throw new NullPointerException("consumer == null");

        Mat exam = matFromBitmap(bitmap);
        int answersIds[] = new int[amountOfQuestions];
        float lefts[] = new float[amountOfQuestions];
        float tops[] = new float[amountOfQuestions];
        float rights[] = new float[amountOfQuestions];
        float bottoms[] = new float[amountOfQuestions];
        int selections[] = new int[amountOfQuestions];
        Map<Point, Integer> answersMap = findQuestions(exam, questionTemplate, amountOfQuestions);
        sortQuestions(answersMap, answersIds, lefts, tops, rights, bottoms, selections, questionTemplate.cols(), questionTemplate.rows(), exam.cols(), exam.rows());
        consumer.consume(amountOfQuestions, answersIds, lefts, tops, rights, bottoms, selections);
        //FOR_DEBUGGING: use in evalutaor showRedRectangles(lefts,rights,exam,questionTemplate.width(),questionTemplate.height())

    }


    public void scanAnswers(Bitmap bitmap, ScanAnswersConsumer consumer) {

        if (bitmap == null )
            throw new NullPointerException("bitmap == null");
        if (consumer == null)
            throw new NullPointerException("consumer == null");

        Mat exam = matFromBitmap(bitmap);

        Map<Point, Integer> answersMap = findQuestions(exam, questionTemplate);
        int amountOfQuestions = answersMap.size();
        int answersIds[] = new int[amountOfQuestions];
        float lefts[] = new float[amountOfQuestions];
        float tops[] = new float[amountOfQuestions];
        float rights[] = new float[amountOfQuestions];
        float bottoms[] = new float[amountOfQuestions];
        int selections[] = new int[amountOfQuestions];
        sortQuestions(answersMap, answersIds, lefts, tops, rights, bottoms, selections, questionTemplate.cols(), questionTemplate.rows(), exam.cols(), exam.rows());
        consumer.consume(amountOfQuestions, answersIds, lefts, tops, rights, bottoms, selections);

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    Map<Point, Integer> findQuestions(Mat img_exam, Mat img_template, int[] leftMostXs, int[] upperMostYs) {

        int numOfQuestions = leftMostXs.length;
        int unIdentifiedThreshold = (int) Math.ceil(numOfQuestions * 0.2);
        Map<Point, Integer> answersMap = new HashMap<>();
        FineAdjustment[] adjustments = new FineAdjustment[numOfQuestions];
        CummulatedAdjustment cummulatedAdjustment = CummulatedAdjustment.get(img_exam.cols());
        for (int i = 0; i < numOfQuestions; i++) {
            cummulatedAdjustment.getNext(leftMostXs[i], upperMostYs[i]);
            leftMostXs[i] +=cummulatedAdjustment.getCurrXAdj();
            upperMostYs[i] +=cummulatedAdjustment.getCurrYAdj();
            Point matchLoc = new Point(leftMostXs[i], upperMostYs[i]);
            Rect rect = null;
            final Scalar lowerWhiteBound = getLowerWhiteBound(img_exam.submat(new Rect(leftMostXs[i], upperMostYs[i], img_template.cols(), img_template.rows())));
            if (param_USE_FINE_AJDUSTMENT) {
                FineAdjustment adjustment = FineAdjustment.create(
                        img_exam,
                        leftMostXs[i],
                        upperMostYs[i],
                        img_template.cols(),
                        img_template.rows(),
                        lowerWhiteBound
                );
                cummulatedAdjustment.accumulateX(adjustment.getXAdj());
                cummulatedAdjustment.accumulateY(adjustment.getYAdj());
                rect = new Rect(
                        (int) matchLoc.x + adjustment.getXAdj() ,
                        (int) matchLoc.y + adjustment.getYAdj() ,
                        (img_template.cols()), (img_template.rows())
                );
                adjustments[i] = adjustment;
            } else {
                rect = new Rect((int) matchLoc.x, (int) matchLoc.y, (img_template.cols()), (img_template.rows()));
            }
            Mat currAns = img_exam.submat(rect);
            List<Mat> imgChunks = splitImage2(currAns, 5);
            int correctAns = markedAnswer2(imgChunks, lowerWhiteBound);
            answersMap.put(matchLoc, correctAns);
        }
        //DEBUGGING_HACK: use in the evaluator showRedRectanglesWithAdj(leftMostXs,upperMostYs,img_exam,img_template.width(),img_template.height(),forDebugging_ADJUSTMENTS)
        //DEBUGGING_HACK: use in the evalutaor showRedRectangles(leftMostXs,upperMostYs,img_exam,img_template.width(),img_template.height())
        List<Integer> unidentified = answersMap.values().stream().filter(x -> x == -1).collect(Collectors.toList());
        if (unidentified.size() > unIdentifiedThreshold) {
          //  answersMap = findQuestions(img_exam, img_template, numOfQuestions);
        }
        return answersMap;
    }

    @NotNull
    private Scalar getLowerWhiteBound(Mat submat) {
        return Core.mean(submat);
    }

    private Bitmap showRedRectanglesWithAdj(int[] xs, int[] ys, Mat mat, int tempW, int tempH, FineAdjustment[] adj) {
        for (int i = 0; i < xs.length; i++) {
            Imgproc.rectangle(mat, new Point(xs[i] + adj[i].getXAdj(), ys[i] + adj[i].getYAdj()), new Point(xs[i] + adj[i].getXAdj() + tempW,
                    ys[i] + adj[i].getYAdj() + tempH), new Scalar(0, 0, 255), -1);
        }
        Bitmap bm = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, bm);
        return bm;
    }

    private Bitmap generateFeedbackBitmap(int[] xs, int[] ys, Bitmap bm, int tempW, int tempH) {
        Mat mat = matFromBitmap(bm);
        for (int i = 0; i < xs.length; i++) {
            Imgproc.rectangle(
                    mat,
                    new Point(xs[i], ys[i]),
                    new Point(xs[i] + tempW, ys[i] + tempH),
                    new Scalar(120, 120, 0),
                    5
            );
        }
        Bitmap ans = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, ans);
        return ans;
    }

    private static Bitmap showRedRectangles(int[] xs, int[] ys, Mat mat, int tempW, int tempH) {
        for (int i = 0; i < xs.length; i++) {
            Imgproc.rectangle(mat, new Point(xs[i], ys[i]), new Point(xs[i] + tempW,
                    ys[i] + tempH), new Scalar(0, 0, 255), -1);
        }
        Bitmap bm = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, bm);
        return bm;
    }

    private static Bitmap showRedRectangles(float[] xs, float[] ys, Mat mat, int tempW, int tempH) {
        int[] sXs = new int[xs.length];
        int[] sYs = new int[ys.length];
        for (int i = 0; i < sXs.length; i++) {
            sXs[i] = (int) (mat.width() * xs[i]);
            sYs[i] = (int) (mat.height() * ys[i]);
        }
        return showRedRectangles(sXs, sXs, mat, tempW, tempH);
    }

    protected int markedAnswer2(List<Mat> imgChunks, Scalar mean) {
        int maxBlack = 0;
        Map<Integer, Integer> bp = new HashMap<>();
        int sizeOfChunk = imgChunks.get(0).cols() * imgChunks.get(0).rows();
        for (int i = 0; i < imgChunks.size(); i++) {
            Mat currMat = imgChunks.get(i);
            //CONVERT IMAGE TO B&W AND DETERMINE IF IT HAS MORE BLACK OR WHITE
            Imgproc.cvtColor(currMat, currMat, Imgproc.COLOR_BGR2GRAY);
            Mat cloneONLYFORTESTING = currMat.clone();
            Core.inRange(currMat, mean, param_UPPER_BOUND_BLACKS_COUNTING, currMat);
//            Core.bitwise_not(currMat, currMat);

            int non_black_pixels = Core.countNonZero(currMat);
            int black_pixels = currMat.rows() * currMat.cols() - non_black_pixels;
            bp.put(i+1, black_pixels);
        }

        List<Map.Entry<Integer, Integer>> list = new ArrayList<>(bp.entrySet());
        list.sort(Map.Entry.<Integer, Integer>comparingByValue().reversed());

        Map<Integer, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<Integer, Integer> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        List<Integer> temp = result.values().stream().collect(Collectors.toList());
        if (Math.abs(temp.get(0) - temp.get(1)) < 0.1 * sizeOfChunk ) {
            return -1;
        }

        else return result.keySet().stream().collect(Collectors.toList()).get(0);
    }

    protected List<Mat> splitImage2(Mat mat, int chunkNumbers) {

        List<Mat> chunkedImages = new ArrayList<Mat>(chunkNumbers);
        int sizeOfPart = mat.cols() / chunkNumbers;
        for (int i = 0; i < chunkNumbers; i++) {
            Rect rect = new Rect((int) i * sizeOfPart, 0, sizeOfPart, mat.rows());
            Mat ith_ans = mat.submat(rect);
            chunkedImages.add(ith_ans);
        }
        return chunkedImages;
    }

    public static Bitmap helper(Mat result) {
        Bitmap bm = Bitmap.createBitmap(result.cols(), result.rows(), Bitmap.Config.ARGB_8888);
        Imgproc.cvtColor(result, result, Imgproc.COLOR_GRAY2RGB);
        Utils.matToBitmap(result, bm);
        return bm;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    Map<Point, Integer> findQuestions(Mat img_exam, Mat img_template, int numOfQuestions) {

        Size scaleSize = new Size(ORIGINAL_WIDTH, ORIGINAL_HEIGHT);
        Imgproc.resize(img_exam, img_exam, scaleSize);

        int result_cols = img_exam.cols() - img_template.cols() + 1;
        int result_rows = img_exam.rows() - img_template.rows() + 1;
        Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1); //CV_32FC1 means 32 bit floating point signed depth in one channel

        Imgproc.cvtColor(img_exam, img_exam, Imgproc.COLOR_BGR2GRAY);

        // / Do the Matching and Normalize
        Imgproc.matchTemplate(img_exam, img_template, result, Imgproc.TM_CCOEFF_NORMED);
        Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());


        Core.MinMaxLocResult mmr;
        Point matchLoc;
        Map<Point, Integer> answersMap = new HashMap<>();
        int i = 0;

        while (true) {
            i++;
            mmr = Core.minMaxLoc(result);
            // match loc is the left upper point of the found template
            // x + -> right direction
            // y + -> down direction
            matchLoc = mmr.maxLoc;
            Mat img_result = img_exam.clone();

            if (i <= numOfQuestions) {
//                if(!checkOverlapping(answersMap, matchLoc)) {
                if (true) {
                    final Scalar lowerWhiteBound = getLowerWhiteBound(img_exam.submat(new Rect((int) matchLoc.x, (int) matchLoc.y, img_template.cols(), img_template.rows())));
                    Imgproc.rectangle(img_exam, matchLoc, new Point(matchLoc.x + img_template.cols(),
                            matchLoc.y + img_template.rows()), new Scalar(0, 0, 255), -1);
                    // find the answer
                    // part the matching image into 5
                    Bitmap img_bitmap = Bitmap.createBitmap(img_result.cols(), img_result.rows(), Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(img_result, img_bitmap);
                    Bitmap bm = Bitmap.createBitmap(img_bitmap, (int) matchLoc.x, (int) matchLoc.y, img_template.cols(), img_template.rows());
                    List<Mat> imgChunks = splitImage2(matFromBitmap(bm), 5);
                    int correctAns = markedAnswer2(imgChunks, lowerWhiteBound);
                    answersMap.put(matchLoc, correctAns );
                    // erase this matching to prevent infinite loop
//                    Imgproc.rectangle(result, matchLoc, new Point(matchLoc.x + img_template.cols(),
//                            matchLoc.y + img_template.rows()), new Scalar(0, 0, 0), -1);
                    Imgproc.rectangle(result, new Point(matchLoc.x - 70, matchLoc.y - 25), new Point(matchLoc.x + img_template.cols(),
                            matchLoc.y + img_template.rows()), new Scalar(0, 0, 0), -1);
                } else {
                    Imgproc.rectangle(result, matchLoc, new Point(matchLoc.x + img_template.cols(),
                            matchLoc.y + img_template.rows()), new Scalar(0, 0, 0), -1);
                    i--;
                }
            } else {
                break; // No more results within tolerance, break search
            }
        }
        return answersMap;


    }

    public static Bitmap helperMatToBitmap(Mat m) {
        Bitmap bm = Bitmap.createBitmap(m.width(), m.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(m, bm);
        return bm;
    }

    private static List<Bitmap> helperMatListToBitmapList(List<Mat> ms) {
        List<Bitmap> ans = new ArrayList<>();
        for (Mat m : ms) {
            ans.add(helperMatToBitmap(m));
        }
        return ans;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean checkOverlapping(Map<Point, Integer> answersMap, Point matchLoc) {
        boolean temp = answersMap.keySet().stream().anyMatch(p -> p.x == matchLoc.x && Math.abs(p.y - matchLoc.y) < 70);
        temp = temp || answersMap.keySet().stream().anyMatch(p -> Math.abs(p.x - matchLoc.x) < 70 && p.x != matchLoc.x);
        return temp;
    }


    Map<Point, Integer> findQuestions(Mat img_exam, Mat img_template) {

        Size scaleSize = new Size(2550, 3300);
        Imgproc.resize(img_exam, img_exam, scaleSize);

        int result_cols = img_exam.cols() - img_template.cols() + 1;
        int result_rows = img_exam.rows() - img_template.rows() + 1;
        Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1); //CV_32FC1 means 32 bit floating point signed depth in one channel


        Imgproc.blur(img_exam, img_exam, new Size(3, 3));
        Imgproc.blur(img_template, img_template, new Size(3, 3));

        Imgproc.cvtColor(img_exam, img_exam, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(img_template, img_template, Imgproc.COLOR_BGR2GRAY);

        Imgproc.Canny(img_exam, img_exam, 50, 200);
        Imgproc.Canny(img_template, img_template, 50, 200);


        // / Do the Matching and Normalize
        Imgproc.matchTemplate(img_exam, img_template, result, Imgproc.TM_CCOEFF_NORMED);
        Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());


        Core.MinMaxLocResult mmr;
        Point matchLoc;
        Map<Point, Integer> answersMap = new HashMap<>();

        while (true) {
            mmr = Core.minMaxLoc(result);
            // match loc is the left upper point of the found template
            // x + -> right direction
            // y + -> down direction
            matchLoc = mmr.maxLoc;
            // threshold chosen empirically
            if (mmr.maxVal >= 0.67) {
                Mat img_result = img_exam.clone();
                Imgproc.rectangle(img_exam, matchLoc, new Point(matchLoc.x + img_template.cols(),
                        matchLoc.y + img_template.rows()), new Scalar(0, 0, 255), -1);
                // find the answer
                // part the matching image into 5
                Bitmap img_bitmap = Bitmap.createBitmap(img_result.cols(), img_result.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(img_result, img_bitmap);
                Bitmap bm = Bitmap.createBitmap(img_bitmap, (int) matchLoc.x, (int) matchLoc.y, img_template.cols(), img_template.rows());
                ArrayList<Bitmap> imgChunks = splitImage(bm, 5);
                int correctAns = markedAnswer(imgChunks);
                answersMap.put(matchLoc, correctAns + 1);
                // erase this matching to prevent infinite loop
                Imgproc.rectangle(result, matchLoc, new Point(matchLoc.x + img_template.cols(),
                        matchLoc.y + img_template.rows()), new Scalar(0, 0, 0), -1);

//                Point diagonalPoint = new Point(matchLoc.x + img_template.cols(), matchLoc.y + img_template.rows());
//                Rect rect = new Rect((int)matchLoc.x, (int)matchLoc.y, img_template.cols(), img_template.rows());
//                Mat currAns = img_exam.submat(rect);
//                ArrayList<Bitmap> imgChunks = splitImage(bm,5);
//                int correctAns = markedAnswer(imgChunks);
//                answersMap.put(matchLoc, correctAns);
//                // draw rectangle around the found template in the output picture (red)
//                Imgproc.rectangle(img_exam, matchLoc, diagonalPoint, new Scalar(0, 0, 255), 2);
//                // erase this matching in order to find next matching (black)
//                Imgproc.rectangle(result, matchLoc, diagonalPoint, new Scalar(0, 0, 0), -1);
            } else {
                break; // No more results within tolerance, break search
            }
        }
        return answersMap;


    }


    void sortQuestions(Map<Point, Integer> answersMap, int[] answersIds, float[] lefts, float[] tops, float[] rights, float[] bottoms, int[] selections, int delta_x, int delta_y, int img_cols, int img_rows) {
        // sort points:
        // x value is the most significant
        int largeXDistance = (int)(LARGE_X_DISTANCE * img_cols);
        class PointComparator implements Comparator {
            public int compare(Object o1, Object o2) {
                Point p1 = (Point) o1;
                Point p2 = (Point) o2;

                if (p1.x == p2.x && p1.y == p2.y)
                    return 0;
                else if (p1.x - p2.x  > largeXDistance)
                    return 1;
                else if (Math.abs(p1.x - p2.x) < largeXDistance  && p1.y > p2.y)
                    return 1;
                else
                    return -1;
            }
        }

        Comparator pointComp = new PointComparator();
        Set<Point> keys = answersMap.keySet();
        ArrayList<Point> keysList = new ArrayList<>(keys);
        Collections.sort(keysList, pointComp);
        int q_num = 0;
        for (Point p : keysList) {
            answersIds[q_num] = q_num + 1;
            selections[q_num] = answersMap.get(p);
            lefts[q_num] = ((float) p.x / (float) img_cols);
            tops[q_num] = ((float) p.y / (float) img_rows);
            rights[q_num] = ((float) (p.x + delta_x) / (float) img_cols);
            bottoms[q_num] = ((float) (p.y + delta_y) / (float) img_rows);
            q_num++;
        }
    }

    private String helperShowPoints(Map<Point, Integer> answersMap){
        String res = "";
        for (Point p:answersMap.keySet()){
            res += String.format("x:%f , y:%f, sel:%d\n", p.x, p.y, answersMap.get(p));
        }
        return res;
    }

    // JUST FOR TEST
    void sortiQuestions(Map<Point, Integer> answersMap, int[] answersIds, double[] lefts, double[] tops, double[] rights, double[] bottoms, int[] selections, int delta_x, int delta_y) {
        // sort points:
        // x value is the most significant
        class PointComparator implements Comparator {
            public int compare(Object o1, Object o2) {
                Point p1 = (Point) o1;
                Point p2 = (Point) o2;

                if (p1.x == p2.x && p1.y == p2.y)
                    return 0;
                else if (p1.x -p2.x> LARGE_X_DISTANCE)
                    return 1;
                else if (p1.x == p2.x && p1.y > p2.y)
                    return 1;
                else
                    return -1;
            }
        }

        Comparator pointComp = new PointComparator();
        Set<Point> keys = answersMap.keySet();
        ArrayList<Point> keysList = new ArrayList<>(keys);
        Collections.sort(keysList, pointComp);
        int q_num = 0;
        for (Point p : keysList) {
            answersIds[q_num] = q_num + 1;
            selections[q_num] = answersMap.get(p);
            lefts[q_num] = (p.x);
            tops[q_num] = (p.y);
            rights[q_num] = ((p.x + delta_x));
            bottoms[q_num] = ((p.y + delta_y));
            q_num++;
        }
    }


    // find out which answer was marked by determining which sub-image in imagesArr
    // has the bigget number of colored pixeles
    private int markedAnswer(ArrayList<Bitmap> imgChunks) {

//        let maxBlack = 0;
//        let maxImg = 0;
//        for(let i = 0; i < imagesArr.length; i++){
//            let currAns = imagesArr[i];
//            //CONVERT IMAGE TO B&W AND DETERMINE IF IT HAS MORE BLACK OR WHITE
//            cv.cvtColor(currAns, currAns, cv.COLOR_BGR2GRAY);
//            let nonBlackPixels = cv.countNonZero(currAns);
//            let blackPixels = currAns.rows * currAns.cols - nonBlackPixels;
//            if(maxBlack < blackPixels){
//                maxBlack = blackPixels;
//                maxImg = i+1;
//            }
//        }
//
//        return maxImg;

        int maxBlack = 0;
        int maxImg = -2;
        for (int i = 0; i < imgChunks.size(); i++) {
            Mat currMat = new Mat();
            Bitmap currBitMap = imgChunks.get(i);
            Utils.bitmapToMat(currBitMap, currMat);
            //CONVERT IMAGE TO B&W AND DETERMINE IF IT HAS MORE BLACK OR WHITE
            Imgproc.cvtColor(currMat, currMat, Imgproc.COLOR_BGR2GRAY);
            int non_black_pixels = Core.countNonZero(currMat);
            int black_pixels = currMat.rows() * currMat.cols() - non_black_pixels;
            if (maxBlack < black_pixels) {
                maxBlack = black_pixels;
                maxImg = i;
            }
        }

        return maxImg;
    }


    // split source image into chunksNum smaller images
    // split is done according to the image width
    private static ArrayList<Bitmap> splitImage(Bitmap bitmap, int chunkNumbers) {

        if (bitmap == null) return new ArrayList<>();
        ArrayList<Bitmap> chunkedImages = new ArrayList<Bitmap>(chunkNumbers);
        int sizeOfPart = bitmap.getWidth() / chunkNumbers;
        for (int i = 0; i < chunkNumbers; i++) {
            Bitmap cropped = Bitmap.createBitmap(bitmap, i * sizeOfPart, 0, sizeOfPart, bitmap.getHeight());
            chunkedImages.add(cropped);
        }
        return chunkedImages;
    }

    // Detect corners in the given image
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected List<Point> cornerDetection(Mat inputImg) {

        MatOfKeyPoint pointsMat = new MatOfKeyPoint();
        int threshold = 20;
        boolean nonMaxSup = true;
        // initiate FAST object with chosen values
        //FastFeatureDetector fast = FastFeatureDetector.create(threshold, nonMaxSup, FastFeatureDetector.TYPE_7_12);
        FastFeatureDetector fast = FastFeatureDetector.create();
        // find and draw the keypoints
        fast.detect(inputImg, pointsMat);

        Scalar redColor = new Scalar(255, 0, 0);
        Mat mRgba = inputImg.clone();
//        Features2d.drawKeypoints(mRgba, pointsMat, mRgba, redColor);
        List<Point> points = pointsMat.toList().stream().map(p -> p.pt).collect(Collectors.toList());
        return points;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Point> detectCorners(Mat image_input) {

        Mat image = image_input.clone();
        List<MatOfPoint> squares = new ArrayList<>();

//        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2HSV);
        Mat smallerImg = new Mat(new Size(image.width() / 2, image.height() / 2), image.type());

        Mat gray = new Mat(image.size(), image.type());

        Mat gray0 = new Mat(image.size(), CvType.CV_8U);

        // down-scale and upscale the image to filter out the noise
        Imgproc.pyrDown(image, smallerImg, smallerImg.size());
        Imgproc.pyrUp(smallerImg, image, image.size());

        // find squares in every color plane of the image
        for (int c = 0; c < 3; c++) {

            extractChannel(image, gray, c);

            // try several threshold levels
            for (int l = 1; l < N; l++) {
                //Cany removed... Didn't work so well


                Imgproc.threshold(gray, gray0, (l + 1) * 255 / N, 255, THRESH_BINARY);
                List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
                // find contours and store them all as a list
                Imgproc.findContours(gray0, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

                MatOfPoint approx = new MatOfPoint();

                // test each contour
                for (int i = 0; i < contours.size(); i++) {
                    // approximate contour with accuracy proportional
                    // to the contour perimeter
                    approx = approxPolyDP(contours.get(i), Imgproc.arcLength(new MatOfPoint2f(contours.get(i).toArray()), true) * 0.02, true);
                    // square contours should have 4 vertices after approximation
                    // relatively large area (to filter out noisy contours)
                    // and be convex.
                    // Note: absolute value of an area is used because
                    // area may be positive or negative - in accordance with the
                    // contour orientation

                    if (approx.toArray().length == 4 &&
                            Math.abs(Imgproc.contourArea(approx)) > 1000 &&
                            Imgproc.isContourConvex(approx)) {
                        double maxCosine = 0;

                        for (int j = 2; j < 5; j++) {
                            // find the maximum cosine of the angle between joint edges
                            double cosine = Math.abs(angle(approx.toArray()[j % 4], approx.toArray()[j - 2], approx.toArray()[j - 1]));
                            maxCosine = Math.max(maxCosine, cosine);
                        }

                        // if cosines of all angles are small
                        // (all angles are ~90 degree) then write quandrange
                        // vertices to resultant sequence
                        if (maxCosine < 0.3)
                            squares.add(approx);
                    }
                }
            }
        }

        List<List<Point>> listOfPoints = squares.stream().map(m -> m.toList()).collect(Collectors.toList());
        LinkedHashSet<List<Point>> hashSet = new LinkedHashSet<>(listOfPoints);
        ArrayList<List<Point>> lstWithoutDuplicates = new ArrayList<>(hashSet);

        // the largest rectangle is the whole image - remove it
        lstWithoutDuplicates.removeIf(l ->
                l.contains(new Point(0, 0)) && l.contains(new Point(0, image.rows() - 1)) &&
                        l.contains(new Point(image.cols() - 1, 0)) && l.contains(new Point(image.cols() - 1, image.rows() - 1)));

        List<MatOfPoint> mats = lstWithoutDuplicates.stream().map(l -> new MatOfPoint(l.get(0), l.get(1), l.get(2), l.get(3))).collect(Collectors.toList());
        // search for next largest rect area
        MatOfPoint max = mats.stream().max(Comparator.comparing(m -> Imgproc.contourArea((Mat) m))).orElseThrow(NoSuchElementException::new);
        // pts are the points of the largest rectangle
        List<Point> pts = max.toList();
        return pts;

    }


    void extractChannel(Mat source, Mat out, int channelNum) {
        List<Mat> sourceChannels = new ArrayList<Mat>();
        List<Mat> outChannel = new ArrayList<Mat>();

        Core.split(source, sourceChannels);

        outChannel.add(new Mat(sourceChannels.get(0).size(), sourceChannels.get(0).type()));

        Core.mixChannels(sourceChannels, outChannel, new MatOfInt(channelNum, 0));

        Core.merge(outChannel, out);
    }

    MatOfPoint approxPolyDP(MatOfPoint curve, double epsilon, boolean closed) {
        MatOfPoint2f tempMat = new MatOfPoint2f();

        Imgproc.approxPolyDP(new MatOfPoint2f(curve.toArray()), tempMat, epsilon, closed);

        return new MatOfPoint(tempMat.toArray());
    }

    double angle(Point pt1, Point pt2, Point pt0) {
        double dx1 = pt1.x - pt0.x;
        double dy1 = pt1.y - pt0.y;
        double dx2 = pt2.x - pt0.x;
        double dy2 = pt2.y - pt0.y;
        return (dx1 * dx2 + dy1 * dy2) / Math.sqrt((dx1 * dx1 + dy1 * dy1) * (dx2 * dx2 + dy2 * dy2) + 1e-10);
    }

    @Override
    public Bitmap align(Bitmap toBeAligned, Bitmap alignmentReference) {
        Mat mToBeAligned = matFromBitmap(toBeAligned);
        Mat mAlignmentReference = matFromBitmap(alignmentReference);
        AlignmentHandler ah = new AlignmentHandler();
        ah.align1(
                mToBeAligned,
                mAlignmentReference,
                null,
                "Series"//HAVE NO IDEA WHAT IT DOES
        );
        final Bitmap bitmap = bitmapFromMat(ah.align);
        return bitmap;
    }

    @Override
    public Bitmap createFeedbackImage(Bitmap bitmap, float[] lefts, float[] tops, int[] selections, int[] ids, String examineeId, Boolean[] wasConflicted) {
        int[] leftsI = new int[lefts.length];
        int[] topsI = new int[tops.length];
        for (int i = 0; i < lefts.length; i++) {
            leftsI[i] = (int) (lefts[i] * bitmap.getWidth());
            topsI[i] = (int) (tops[i] * bitmap.getHeight());
        }
//        return generateFeedbackBitmap(
//                leftsI,topsI, bitmap, questionTemplate.width(), questionTemplate.height()
//        );
        return generateFeedbackBitmap(leftsI, topsI, selections, bitmap, questionTemplate.width(), questionTemplate.height(), ids, examineeId, wasConflicted);
    }

    @Override
    public Bitmap createFailFeedbackImage(Bitmap bitmap) {
        Mat mat = matFromBitmap(bitmap);
        Imgproc.putText(mat, String.format("Scan failed"), new Point(mat.cols()/10, mat.rows()/2), 5, 10, new Scalar(255,0,0), 5);
        Imgproc.putText(mat, String.format("Please retake"), new Point(mat.cols()/10, mat.rows()/2 + mat.rows()/10), 5, 10, new Scalar(255,0,0), 5);
        Bitmap ans = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(mat, ans);
        return ans;
    }

    private Bitmap generateFeedbackBitmap(int[] xs, int[] ys, int[] selecetions, Bitmap bm, int tempW, int tempH,  int[] ids, String examineeId, Boolean[] wasConflicted) {
        final float xScaleConcreteToOrig = (float) bm.getWidth() / (float) ORIGINAL_WIDTH;
        final float yScaleConcreteToOrig = (float) bm.getHeight() / (float) ORIGINAL_HEIGHT;
        Mat mat = matFromBitmap(bm);
        if(Arrays.stream(wasConflicted).anyMatch(b -> b == true))
            Imgproc.putText(mat, "all conflicts resolved", new Point(0, mat.rows()/15), 5, 4.0, new Scalar(0,200,0), 3);
        Imgproc.putText(mat, examineeId, new Point(mat.cols()/2, mat.rows()/10), 5, 8.0, new Scalar(0,0,200), 3);
        for (int i = 0; i < xs.length; i++) {
            final int scaledTempW = (int) (tempW * xScaleConcreteToOrig);
            final int scaledTempH = (int) (tempH * yScaleConcreteToOrig);
            Selection selection = new Selection(selecetions[i], xs[i], ys[i], scaledTempW, scaledTempH, wasConflicted[i]);
            Id id = new Id(ids[i], xs[i], ys[i], scaledTempW, scaledTempH);
      //      if(selection.getRep().equals("-1"))

            Imgproc.rectangle(
                    mat,
                    new Point(xs[i], ys[i]),
                    new Point(xs[i] + scaledTempW, ys[i] + scaledTempH),
                    selection.getColor(),
                    5
            );
            Imgproc.putText(mat, selection.getRep(), selection.getLocation(), selection.getFontSize(), selection.getFontScale(), selection.getColor(), selection.getThickness());
            Imgproc.putText(mat, id.getRep(), id.getLocation(), id.getFontSize(), id.getFontScale(), id.getColor(), id.getThickness());
//            Log.d("DebugExamScanner", selection.getRep());
        }
        Bitmap ans = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(mat, ans);
        return ans;
    }

    private Bitmap generateFeedbackBitmapNoNums(int[] xs, int[] ys, int[] selecetions, Bitmap bm, int tempW, int tempH) {
        final float xScaleConcreteToOrig = (float) bm.getWidth() / (float) ORIGINAL_WIDTH;
        final float yScaleConcreteToOrig = (float) bm.getHeight() / (float) ORIGINAL_HEIGHT;
        Mat mat = matFromBitmap(bm);
        for (int i = 0; i < xs.length; i++) {
            final int scaledTempW = (int) (tempW * xScaleConcreteToOrig);
            final int scaledTempH = (int) (tempH * yScaleConcreteToOrig);
            Selection selection = new Selection(selecetions[i], xs[i], ys[i], scaledTempW, scaledTempH, false);
            Imgproc.rectangle(
                    mat,
                    new Point(xs[i], ys[i]),
                    new Point(xs[i] + scaledTempW, ys[i] + scaledTempH),
                    selection.getColor(),
                    5
            );
            Imgproc.putText(mat, selection.getRep(), selection.getLocation(), selection.getFontSize(), selection.getFontScale(), selection.getColor(), selection.getThickness());
//            Log.d("DebugExamScanner", selection.getRep());
        }
        Bitmap ans = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(mat, ans);
        return ans;
    }
    private Bitmap generateFeedbackBitmapTMP(int[] xs, int[] ys, Bitmap bm, int tempW, int tempH) {
        final float xScaleConcreteToOrig = (float) bm.getWidth() / (float) ORIGINAL_WIDTH;
        final float yScaleConcreteToOrig = (float) bm.getHeight() / (float) ORIGINAL_HEIGHT;
        Mat mat = matFromBitmap(bm);
        for (int i = 0; i < xs.length; i++) {
            final int scaledTempW = (int) (tempW * xScaleConcreteToOrig);
            final int scaledTempH = (int) (tempH * yScaleConcreteToOrig);
            Imgproc.rectangle(
                    mat,
                    new Point(xs[i], ys[i]),
                    new Point(xs[i] + scaledTempW, ys[i] + scaledTempH),
                    new Scalar(0,0,200),
                    5
            );
//            Log.d("DebugExamScanner", selection.getRep());
        }
        Bitmap ans = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(mat, ans);
        return ans;
    }
}

