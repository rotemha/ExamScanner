package com.example.examscanner.image_processing;

class CummulatedAdjustment {
    private int prevX;
    private int prevY;
    private int currXAdj;
    private int currYAdj;
    private static double LARGE_X_DISTANCE = 0.1;
    private int cols;

    public static CummulatedAdjustment get(int cols) {
        return new CummulatedAdjustment(cols);
    }

    public CummulatedAdjustment(int cols) {
        this.cols = cols;
        prevX = -1;
        prevY = -1;
        currXAdj = 0;
        currYAdj = 0;
    }

    public void getNext(int leftMostX, int upperMostY) {
        if (upperMostY < prevY) {
            currYAdj = 0;
        }
        if (Math.abs(leftMostX - prevX) > (int)(LARGE_X_DISTANCE * cols) ) {
            currXAdj = 0;
        }
        prevY = upperMostY;
        prevX = leftMostX;
    }

    public void accumulateX(int x) {
        currXAdj += x;
    }

    public int getCurrXAdj() {
        return currXAdj;
    }

    public int getCurrYAdj() {
        return currYAdj;
    }

    public void accumulateY(int y) {
        currYAdj += y;
    }
}

