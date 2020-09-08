package com.example.examscanner.image_processing;

import org.opencv.core.Point;
import org.opencv.core.Scalar;

class Id {
    private static final int Y_ANS_PADDING = 10;
    private static int LOWEST_VALID_ANS = 1;
    private static int HIGHEST_VALID_ANS = 5;
    private static int THICKNESS = 3;
    private static final int X_ANS_PADDING = 10;
    private final Point location;
    private double FONT_SCALE = 3.0;

    public int getThickness() {
        return THICKNESS;
    }

    public int getFontSize() {
        return FONT_SIZE;
    }

    private static int FONT_SIZE = 5;
    private Scalar color;

    public Scalar getColor() {
        return color;
    }

    public String getRep() {
        return rep;
    }

    private String rep;
    public Id(int id, int x, int y, int tempW, int tempH) {
        int shiftLeft = (int)(0.4 * tempW);
        location = new Point(x- shiftLeft, y +tempH- Y_ANS_PADDING);
        color = new Scalar(0,0,0);
        rep = String.format("%d", id);
    }

    public double getFontScale() {
        return FONT_SCALE;
    }


    public Point getLocation() {
        return location;
    }
}
