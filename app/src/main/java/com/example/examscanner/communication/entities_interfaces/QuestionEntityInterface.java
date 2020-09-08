package com.example.examscanner.communication.entities_interfaces;

public interface QuestionEntityInterface {
    public long getId();
    public long getVersionId();
    public long getCorrectAnswer();
    public int getLeftX();
    public int getUpY();
    public int getRightX();
    public int getBottomY();
    public int getNum();
}
