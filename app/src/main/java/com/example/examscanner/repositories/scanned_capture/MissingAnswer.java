package com.example.examscanner.repositories.scanned_capture;

import androidx.annotation.NonNull;

import com.example.examscanner.repositories.exam.Question;

public class MissingAnswer extends Answer{
    public MissingAnswer(int id) {
        super(id);
    }

    @Override
    public boolean isMissing() {
        return true;
    }

    @Override
    public int getSelection() {
        return MISSING;
    }

    @Override
    public float getLeft() {
        return 0;
    }

    @Override
    public float getUp() {
        return 0;
    }

    @Override
    public float getRight() {
        return 0;
    }

    @Override
    public float getBottom() {
        return 0;
    }

    @Override
    public boolean isCorrect(Question questionByNumber) {
        return false;
    }

    @NonNull
    @Override
    public String toString() {
        return "M";
    }
}
