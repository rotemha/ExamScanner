package com.example.examscanner.repositories.corner_detected_capture;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;

import com.example.examscanner.repositories.exam.Version;

public class CornerDetectedCapture {
    private long id;
    private Bitmap bm;
    private long sessionId;
    private int leftMostX;
    private int upperMostY;
    private int rightMostX;
    private int bottomMosty;
    private Point upperLeft;
    private Point upperRight;
    private Point bottomRight;
    private Point bottomLeft;
    private Version version;

    public Point getUpperLeft() {
        return upperLeft;
    }

    public Point getUpperRight() {
        return upperRight;
    }

    public Point getBottomRight() {
        return bottomRight;
    }

    public Point getBottomLeft() {
        return bottomLeft;
    }

    public CornerDetectedCapture(Bitmap bitmap, int leftMostX, int upperMostY, int rightMostX, int bottomMosty, long sessionId) {
        this.leftMostX = leftMostX;
        this.upperMostY = upperMostY;
        this.rightMostX = rightMostX;
        this.bottomMosty = bottomMosty;
        this.bm = bitmap;
        this.sessionId=sessionId;
    }

    public CornerDetectedCapture(Bitmap bitmap, PointF upperLeft, PointF bottomRight, long sessionId) {
        this.leftMostX = (int)upperLeft.x;
        this.upperMostY = (int)upperLeft.y;
        this.rightMostX = (int)bottomRight.x;
        this.bottomMosty = (int)bottomRight.y;
        this.bm = bitmap;
        this.sessionId=sessionId;
    }
    public CornerDetectedCapture(Bitmap bitmap, PointF upperLeft, PointF upperRight ,PointF bottomRight,  PointF bottomLeft, long sessionId) {
        this.upperLeft = new Point((int)upperLeft.x,(int)upperLeft.y);
        this.upperRight = new Point((int)upperRight.x,(int)upperRight.y);
        this.bottomRight = new Point((int)bottomRight.x,(int)bottomRight.y);
        this.bottomLeft = new Point((int)bottomLeft.x,(int)bottomLeft.y);
        this.bm = bitmap;
        this.sessionId=sessionId;
    }

    public CornerDetectedCapture() {

    }

    public void setId(long id) {
        this.id = id;
    }

    public Bitmap getBitmap() {
        return bm;
    }

    public long getId() {
        return id;
    }

    public void setBitmap(Bitmap bm) {
        this.bm = bm;
    }



    public int getLeftMostX() {
        return leftMostX;
    }

    public int getUpperMostY() {
        return upperMostY;
    }

    public int getRightMostX() {
        return rightMostX;
    }

    public int getBottomMostY() {
        return bottomMosty;
    }

    public long getSession() {
        return sessionId;
    }


    public void setVersion(Version version) {
        this.version = version;
    }

    public boolean hasVersion() {
        return version!=null;
    }

    public void setUpperLeft(Point point) {
        upperLeft = point;
    }

    public void setUpperRight(Point point) {
        upperRight = point;
    }

    public void setBottomRight(Point point) {
        bottomRight  = point;
    }

    public void setBottomLeft(Point point) {
        bottomLeft = point;
    }

    public Version getVersion() {
        return version;
    }
}
