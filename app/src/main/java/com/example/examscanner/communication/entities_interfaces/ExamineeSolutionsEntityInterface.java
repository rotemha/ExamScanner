package com.example.examscanner.communication.entities_interfaces;

import android.graphics.Bitmap;

public interface ExamineeSolutionsEntityInterface {
    public String getExaimneeId();
    public long getId();
    public Bitmap getBitmap();
    public long getSessionId();
    public long getVersionId();
    public long[] getExamineeAnswersIds();
    public boolean getExamieeIdIsOccupiedByAnotherSolution();

    boolean getIsValid();
}
