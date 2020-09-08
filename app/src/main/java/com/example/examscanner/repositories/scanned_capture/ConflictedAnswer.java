package com.example.examscanner.repositories.scanned_capture;

import android.graphics.PointF;

import androidx.annotation.NonNull;

import com.example.examscanner.components.scan_exam.reslove_answers.resolve_conflicted_answers.Choice;
import com.example.examscanner.repositories.exam.Question;

import java.util.List;

public class ConflictedAnswer extends Answer{
    private final PointF upperLeft;
    private final PointF bottomRight;

    public ConflictedAnswer(int id, PointF upperLeft, PointF bottomRight) {
        super(id);
        this.upperLeft = upperLeft;
        this.bottomRight = bottomRight;
    }

    @Override
    public boolean isConflicted() {
        return true;
    }

    @Override
    public void addMe(List<ConflictedAnswer> l) {
        l.add(this);
    }

    @Override
    public int getSelection() {
        return CONFLICTED;
    }

    public PointF getUpperLeft() {
        return upperLeft;
    }

    public PointF getBottomRight() {
        return bottomRight;
    }


    public ResolvedConflictedAnswer resolve(Choice c) {
        return new ResolvedConflictedAnswer(this,c);
    }

    @NonNull
    @Override
    public String toString() {
        return "C";
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
        return bottomRight.x;
    }

    @Override
    public float getBottom() {
        return bottomRight.y;
    }

    @Override
    public boolean isCorrect(Question questionByNumber) {
        return false;
    }
}
