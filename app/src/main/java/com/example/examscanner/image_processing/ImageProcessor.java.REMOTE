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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.examscanner.R;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;


public class ImageProcessor implements ImageProcessingFacade {
    Mat questionTemplate;

    private Bitmap bitmapFromMat(Mat mat){
        Bitmap bm = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, bm);
        return bm;
    }

    private Mat matFromBitmap(Bitmap bm){
        Mat mat = new Mat();
        Utils.bitmapToMat(bm, mat);
        return mat;
    }


    private Mat loadFromResource(int file) {

        Mat img = null;
        try {
            img = Utils.loadResource(getApplicationContext(), file);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return img;
    }


    private Bitmap loadFromAssets(String filename) {

        Bitmap bitmap = null;
        try {
            InputStream ims = getApplicationContext().getAssets().open(filename);
            bitmap = BitmapFactory.decodeStream(ims);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public ImageProcessor() {

        questionTemplate = loadFromResource(R.drawable.template);
    }



    // given an image of an exam try to detect the 4 corners of the exam
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void detectCorners(Bitmap bm, DetectCornersConsumer consumer) {
        if(bm == null || consumer == null)
            throw new NullPointerException("given input is null");

        Mat mat = matFromBitmap(bm);
        // First find all the corners in the given image
        List<Point> points = cornerDetection(mat);
        // Then, supposing a rectangle exists, find its 4 corners coordinates
        List<Point> filtered = removePoints(points);
        List<Point> clockwiseOrderedPoints = orderPoints(filtered);
        Point upperLeft = clockwiseOrderedPoints.get(1);
        Point upperRight = clockwiseOrderedPoints.get(2);
        Point bottomRight = clockwiseOrderedPoints.get(3);
        Point bottomLeft = clockwiseOrderedPoints.get(0);
        consumer.consume(
                new PointF((float) upperLeft.x, (float) upperLeft.y),
                new PointF((float) upperRight.x, (float) upperRight.y),
                new PointF((float) bottomLeft.x, (float) bottomLeft.y),
                new PointF((float) bottomRight.x, (float) bottomRight.y)
        );

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

        if(bitmap == null || upperLeft == null || upperRight == null || bottomLeft == null || bottomRight == null)
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
                new Point(newWidth-1,0),
                new Point(newWidth-1,newHeight-1),
                new Point(0,newHeight-1)
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

    private static double distance(Point p1, Point p2){
        return Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
    }

    List<Point> removePoints(List<Point> points) {
        int thresh = 50;

        int n = points.size();
        for(int i=0; i<n; i++ ){
            for(int j= i+1; j<n; j++){
                Point point1 = points.get(i);
                Point point2 = points.get(j);
                if(distance(point1, point2) < thresh){
                    points.remove(point2);
                    j--;
                    n--;
                }
            }
        }
        return points;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void scanAnswers(Bitmap bitmap, int amountOfQuestions, ScanAnswersConsumer consumer, int[] leftMostXs, int[] upperMostYs) {

        if(bitmap == null || leftMostXs == null || upperMostYs == null || consumer == null)
            throw new NullPointerException("given input is null");

        Mat exam = matFromBitmap(bitmap);
        int answersIds[] = new int[amountOfQuestions];
        float lefts[] = new float[amountOfQuestions];
        float tops[] = new float[amountOfQuestions];
        float rights[] = new float[amountOfQuestions];
        float bottoms[] = new float[amountOfQuestions];
        int selections[] = new int[amountOfQuestions];
        Map<Point, Integer> answersMap = findQuestions(exam, questionTemplate, leftMostXs, upperMostYs);
        sortQuestions(answersMap, answersIds, lefts, tops, rights, bottoms, selections, questionTemplate.cols(), questionTemplate.rows(), exam.cols(), exam.rows());
        consumer.consume(amountOfQuestions, answersIds, lefts, tops, rights, bottoms, selections);
    }


    public void scanAnswers(Bitmap bitmap, int amountOfQuestions, ScanAnswersConsumer consumer) {

        if(bitmap == null || amountOfQuestions <= 0 || consumer == null)
            throw new NullPointerException("invalid input");

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

    }


    public void scanAnswers(Bitmap bitmap, ScanAnswersConsumer consumer) {

        if(bitmap == null || consumer == null)
            throw new NullPointerException("given input is null");

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
        for (int i = 0; i < numOfQuestions; i++) {
            Point matchLoc = new Point(leftMostXs[i], upperMostYs[i]);
            Rect rect = new Rect((int) matchLoc.x, (int) matchLoc.y, img_template.cols(), img_template.rows());
            Mat currAns = img_exam.submat(rect);
            List<Mat> imgChunks = splitImage2(currAns, 5);
            int correctAns = markedAnswer2(imgChunks);
            answersMap.put(matchLoc, correctAns);
        }

        List<Integer> unidentified = answersMap.values().stream().filter(x -> x == -1).collect(Collectors.toList());
        if (unidentified.size() > unIdentifiedThreshold) {
            answersMap = findQuestions(img_exam, img_template, numOfQuestions);
        }

        return answersMap;
    }

    private int markedAnswer2(List<Mat> imgChunks) {
        int maxBlack = 0;
        int maxImg = 0;
        for (int i = 0; i < imgChunks.size(); i++) {
            Mat currMat = imgChunks.get(i);
            //CONVERT IMAGE TO B&W AND DETERMINE IF IT HAS MORE BLACK OR WHITE
            Imgproc.cvtColor(currMat, currMat, Imgproc.COLOR_BGR2GRAY);
            int non_black_pixels = Core.countNonZero(currMat);
            int black_pixels = currMat.rows() * currMat.cols() - non_black_pixels;
            if (maxBlack < black_pixels) {
                maxBlack = black_pixels;
                maxImg = i + 1;
            }
        }

        return maxImg;
    }

    private List<Mat> splitImage2(Mat mat, int chunkNumbers) {

        List<Mat> chunkedImages = new ArrayList<Mat>(chunkNumbers);
        int sizeOfPart = mat.cols() / chunkNumbers;
        for (int i = 0; i < chunkNumbers; i++) {
            Rect rect = new Rect((int) i * sizeOfPart, 0, sizeOfPart, mat.rows());
            Mat ith_ans = mat.submat(rect);
            chunkedImages.add(ith_ans);
        }
        return chunkedImages;
    }

    Map<Point, Integer> findQuestions(Mat img_exam, Mat img_template, int numOfQuestions) {

        int result_cols = img_exam.cols() - img_template.cols() + 1;
        int result_rows = img_exam.rows() - img_template.rows() + 1;
        Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1); //CV_32FC1 means 32 bit floating point signed depth in one channel

        // / Do the Matching and Normalize
        Imgproc.matchTemplate(img_exam, img_template, result, Imgproc.TM_CCOEFF_NORMED);
        //     Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());


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
            // threshold chosen empirically
            if (i <= numOfQuestions) {
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
            } else {
                break; // No more results within tolerance, break search
            }
        }
        return answersMap;


    }


    Map<Point, Integer> findQuestions(Mat img_exam, Mat img_template) {

        int result_cols = img_exam.cols() - img_template.cols() + 1;
        int result_rows = img_exam.rows() - img_template.rows() + 1;
        Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1); //CV_32FC1 means 32 bit floating point signed depth in one channel

        // / Do the Matching and Normalize
        Imgproc.matchTemplate(img_exam, img_template, result, Imgproc.TM_CCOEFF_NORMED);
        //     Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());


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


    private void sortQuestions(Map<Point, Integer> answersMap, int[] answersIds, float[] lefts, float[] tops, float[] rights, float[] bottoms, int[] selections, int delta_x, int delta_y, int img_cols, int img_rows) {
        // sort points:
        // x value is the most significant
        class PointComparator implements Comparator {
            public int compare(Object o1, Object o2) {
                Point p1 = (Point) o1;
                Point p2 = (Point) o2;

                if (p1.x == p2.x && p1.y == p2.y)
                    return 0;
                else if (p1.x > p2.x)
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
        Map<Integer, Integer> ret = new HashMap<>();
        int q_num = 0;
        for (Point p : keysList) {
            answersIds[q_num] = q_num + 1;
            selections[q_num] = answersMap.get(p);
            lefts[q_num] = (float) (p.x / img_rows);
            tops[q_num] = (float) (p.y / img_cols);
            rights[q_num] = (float) ((p.x + delta_x) / img_rows);
            bottoms[q_num] = (float) ((p.y + delta_y) / img_cols);
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

        if(bitmap == null) return new ArrayList<>();
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
}
