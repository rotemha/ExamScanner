package com.example.examscanner.repositories.scanned_capture;

import androidx.annotation.NonNull;

import com.example.examscanner.repositories.exam.Question;

import java.util.List;

public abstract class Answer {
    public static final int MISSING = -2;
    public static final int CONFLICTED = -1;

    private final int ansNum;

    public Answer(int id) {
        this.ansNum = id;
    }
    public int getAnsNum(){return ansNum;}
    public boolean isResolved(){
        return false;
    }
    public boolean isConflicted(){
        return false;
    }
    public boolean isResolvedConflictedMissing(){
        return false;
    }
    public boolean isMissing(){
        return false;
    }
    public Answer commitResolution(){return this;};
    public void addMe(List<ConflictedAnswer> l){return;};

    public abstract int getSelection();


    public abstract float getLeft();

    public abstract float getUp();

    public abstract float getRight();

    public abstract float getBottom();

    public abstract boolean isCorrect(Question questionByNumber);
}
