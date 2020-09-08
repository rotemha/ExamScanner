package com.example.examscanner.components.create_exam.get_version_file;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.io.IOException;

class JPGVersionImageImpl implements VersionImageGetter {

    private static final String TAG = "ExamScanner";
    private static final String MSG_PREF = "VersionImageImpl::";

    @Override
    public void get(Fragment frag, int pickfileRequestCode) {
        Intent intent = new Intent(Intent. ACTION_OPEN_DOCUMENT );
        intent.setType("image/jpeg");
        frag.startActivityForResult(intent, pickfileRequestCode);
    }

    @Override
    public Bitmap accessBitmap(Intent data, FragmentActivity activity) {
        Uri uri = data.getData();
        try {
            return MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
        } catch (IOException e) {
            Log.d(TAG,MSG_PREF+" accessBitmap",e);
            e.printStackTrace();
        }
        throw new FailedGettingVersionImageException();
    }
}
