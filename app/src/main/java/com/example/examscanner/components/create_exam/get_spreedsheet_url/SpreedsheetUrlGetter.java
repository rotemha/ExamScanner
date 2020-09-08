package com.example.examscanner.components.create_exam.get_spreedsheet_url;

import android.content.Intent;
import android.graphics.Bitmap;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public interface SpreedsheetUrlGetter {
    public void get(Fragment activity, int pickfileRequestCode);
    public String accessUrl(Intent data, FragmentActivity activity);
}
