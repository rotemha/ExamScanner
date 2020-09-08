package com.example.examscanner.components.scan_exam.capture.camera;

import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.example.examscanner.components.scan_exam.capture.CameraOutputHandlerImpl;

public class CameraMangerFactory {
    private FragmentActivity activity;
    private View root;
    private static CameraManager stubInstacne;

    public CameraMangerFactory(FragmentActivity activity, View root) {
        this.activity = activity;
        this.root = root;
    }

    public CameraManager create(){
        if(stubInstacne!=null) return stubInstacne;
        else return new CameraManagerImpl(activity,root);
    }

    public static void setStubInstance(CameraManager theStubInstance){
        stubInstacne = theStubInstance;
    }
}
