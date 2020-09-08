package com.example.examscanner.communication.entities_interfaces;

import com.example.examscanner.repositories.grader.Grader;

import java.util.List;

public interface ExamEntityInterface {
    public long getID();
    public long[] getVersionsIds();
    public String getCourseName();
    public String getUrl();
    public String getYear();
    public int getTerm();
    public long getSessionId();
    public int getSemester();
    public int getNumOfQuestions();
    public String getManagerId();
    public String[] getGradersIds();

    public int getUploaded();

    boolean getDownloaded();
}
