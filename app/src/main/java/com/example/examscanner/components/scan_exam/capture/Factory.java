package com.example.examscanner.components.scan_exam.capture;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;

public class Factory extends FragmentFactory {
    @NonNull
    @Override
    public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
        return new CaptureFragment();
    }
}
