package com.example.examscanner.image_processing;

import org.opencv.core.Point;
import org.opencv.core.Scalar;

class Selection {
    private static final int Y_ANS_PADDING = 10;
    private static int NO_ANSWER_ANS = 0;
    private static int LOWEST_VALID_ANS = 1;
    private static int HIGHEST_VALID_ANS = 5;
    private static int THICKNESS = 3;
    private static final int X_ANS_PADDING = 10;
    private final Point location;
    private double FONT_SCALE = 5.0;

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
    public Selection(int selection, int x, int y, int tempW, int tempH, Boolean wasResolved) {
        location = new Point(x+ tempW+X_ANS_PADDING, y +tempH- Y_ANS_PADDING);
        if(LOWEST_VALID_ANS<=selection && selection <= HIGHEST_VALID_ANS){
            color = new Scalar(0,0,200);
            // resolved conflicted answer should be marked with unique color
            if(wasResolved)
                color = new Scalar(0,200,0);
            rep = String.valueOf(selection);
        }else if(selection == NO_ANSWER_ANS){
            color = new Scalar(200,0,0);
            rep = "REJECTED";
            FONT_SCALE = 2.5;
        }
        else{
            color = new Scalar(120,0,0);
            rep = "?";
        }
    }

    public double getFontScale() {
        return FONT_SCALE;
    }


    public Point getLocation() {
        return location;
    }
}
