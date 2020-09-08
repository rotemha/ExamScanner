package com.example.examscanner.repositories.scanned_capture;

import android.graphics.PointF;

import androidx.annotation.NonNull;

import com.example.examscanner.repositories.exam.Question;

import java.util.List;

public class ResolvedAnswer extends Answer{
    private final PointF upperLeft;
    private final PointF bottomRight;
  //  private Boolean wasConflictedBefore = false;

    public PointF getUpperLeft() {
        return upperLeft;
    }

    public PointF getBottomRight() {
        return bottomRight;
    }

    private final int selection;

    public ResolvedAnswer(int id, PointF upperLeft, PointF bottomRight, int selection) {
        super(id);
        this.upperLeft = upperLeft;
        this.bottomRight = bottomRight;
        this.selection = selection;
    }

    @Override
    public boolean isResolved() {
        return true;
    }


    @Override
    public int getSelection() {
        return selection;
    }

    @Override
    public float getLeft() {
        return upperLeft.x;
    }

    @Override
    public float getUp() {
        return upperLeft.y;
    }

    @Override
    public float getRight() {
        return 0;
    }

    @Override
    public float getBottom() {
        return 0;
    }

//    public void updateBeenConflictedThenResolved() {
//        wasConflictedBefore = true;
//    }

//    public Boolean hasBeenConflictedThenResolved() {
//        return wasConflictedBefore;
//    }

    @Override
    public boolean isCorrect(Question questionByNumber) {
        return questionByNumber.getAns()==selection;
    }

    @NonNull
    @Override
    public String toString() {
        return String.valueOf(selection);
    }
}
