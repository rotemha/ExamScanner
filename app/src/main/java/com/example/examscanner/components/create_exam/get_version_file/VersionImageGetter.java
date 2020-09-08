package com.example.examscanner.components.create_exam.get_version_file;

import android.content.Intent;
import android.graphics.Bitmap;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public interface VersionImageGetter {
    public void get(Fragment activity, int pickfileRequestCode);
    public Bitmap accessBitmap(Intent data, FragmentActivity activity);
}
