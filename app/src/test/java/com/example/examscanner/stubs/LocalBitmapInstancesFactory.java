package com.example.examscanner.stubs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class LocalBitmapInstancesFactory extends BitmapInstancesFactory {
    private final String dirPath;

    public LocalBitmapInstancesFactory(Context context, String dirPath) {
        super(context);
        this.dirPath = dirPath;
    }

    private static final String testJpg1FilePath = "test_jpg_1.jpg";
    private static final String testJpg2FilePath = "test_jpg_2.jpg";
    private static final String testJpg3FilePath = "test_jpg_3.jpg";
    private static final String testTemplate1FilePath = "test_template_1.jpg";
    private static final String testJpg1MarkedFilePath = "test_jpg_1_marked.jpg";
    private static final String testJpg2MarkedFilePath = "test_jpg_2_marked.jpg";
    private static final String testJpg3MarkedFilePath = "test_jpg_3_marked.jpg";
    private String genPath(String fileName){
        return dirPath+"\\"+fileName;
    }
    @Override
    public Bitmap getTestJpg1() {
        return getBitmapFromAssets(genPath(testJpg1FilePath));
    }

    @Override
    public Bitmap getTestJpg2() {
        return getBitmapFromAssets(genPath(testJpg2FilePath));
    }

    @Override
    public Bitmap getTestJpg3() {
        return getBitmapFromAssets(genPath(testJpg3FilePath));
    }

    @Override
    public Bitmap getTestTemplate1() {
        return getBitmapFromAssets(genPath(testTemplate1FilePath));
    }

    @Override
    public Bitmap getTestJpg1Marked() {
        return getBitmapFromAssets(genPath(testJpg1MarkedFilePath));
    }

    @Override
    public Bitmap getTestJpg2Marked() {
        return getBitmapFromAssets(genPath(testJpg2MarkedFilePath));
    }

    @Override
    public Bitmap getTestJpg3Marked() {
        return getBitmapFromAssets(genPath(testJpg3MarkedFilePath));
    }

    @Override
    public Bitmap getRandom() {
        return getBitmapFromAssets(genPath(testJpg1MarkedFilePath));
    }

    private Bitmap getBitmapFromAssets(String path) {
        return  BitmapFactory.decodeFile(path);
    }

}
