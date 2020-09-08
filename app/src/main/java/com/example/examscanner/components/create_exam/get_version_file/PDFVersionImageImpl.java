package com.example.examscanner.components.create_exam.get_version_file;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import com.github.barteksc.pdfviewer.*;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Objects;

public class PDFVersionImageImpl implements VersionImageGetter {

    private static final String TAG = "ExamScanner";
    private static final String MSG_PREF = "VersionImageImpl::";

    @Override
    public void get(Fragment frag, int pickfileRequestCode) {
        Intent intent = new Intent(Intent. ACTION_OPEN_DOCUMENT );
        intent.setType("application/pdf");
        frag.startActivityForResult(intent, pickfileRequestCode);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Bitmap accessBitmap(Intent data, FragmentActivity activity) {
        try {
            return toBitmap(activity, data.getData());
//            return MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
        } catch (IOException | InvalidVersionFileExcpetion e) {
            Log.d(TAG,MSG_PREF+" accessBitmap",e);
            e.printStackTrace();
        }
        throw new FailedGettingVersionImageException();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private Bitmap toBitmap(FragmentActivity activity, Uri uri) throws IOException, InvalidVersionFileExcpetion {
        ParcelFileDescriptor parcelFileDescriptor = activity.getContentResolver().openFileDescriptor(uri, "r");
        return new PDF2BitmapConverter().convert(parcelFileDescriptor , activity);
//        PdfRenderer renderer = new PdfRenderer(parcelFileDescriptor);
//        Bitmap bitmap;
//        final int pageCount = renderer.getPageCount();
//        if(pageCount!=1){
//            throw new InvalidVersionFileExcpetion("PDF should be with exactly 1 page");
//        }
//        PdfRenderer.Page page = renderer.openPage(0);
//        int width = activity.getResources().getDisplayMetrics().densityDpi / 72 * page.getWidth();
//        int height = activity.getResources().getDisplayMetrics().densityDpi / 72 * page.getHeight();
//        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888, true);
//        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
//        page.close();
//        return bitmap;
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static class InvalidVersionFileExcpetion extends Throwable {
        public InvalidVersionFileExcpetion(String s) {
            super(s);
        }
    }
    public static class PDF2BitmapConverter{
        @RequiresApi(api = Build.VERSION_CODES.O)
        public Bitmap convert(ParcelFileDescriptor parcelFileDescriptor, Context context) throws IOException, InvalidVersionFileExcpetion {
            PdfRenderer renderer = new PdfRenderer(parcelFileDescriptor);
            Bitmap bitmap;
            final int pageCount = renderer.getPageCount();
            if(pageCount!=1){
                throw new InvalidVersionFileExcpetion("PDF should be with exactly 1 page");
            }
            PdfRenderer.Page page = renderer.openPage(0);
            int width = context.getResources().getDisplayMetrics().densityDpi / 72 * page.getWidth();
            int height = context.getResources().getDisplayMetrics().densityDpi / 72 * page.getHeight();
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(bitmap, 0, 0, null);
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            page.close();
            return bitmap;
        }
    }
}
