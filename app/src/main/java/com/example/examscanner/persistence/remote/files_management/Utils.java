package com.example.examscanner.persistence.remote.files_management;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class Utils {

    public static byte[] toByteArray(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap toBitmap(byte[] bytes){
            return BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
    }
}
