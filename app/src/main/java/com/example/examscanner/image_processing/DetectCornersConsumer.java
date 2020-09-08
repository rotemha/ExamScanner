package com.example.examscanner.image_processing;


import android.graphics.PointF;

public interface DetectCornersConsumer {
    public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight);
}
