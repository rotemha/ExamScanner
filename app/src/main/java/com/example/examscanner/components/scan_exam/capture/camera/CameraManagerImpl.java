package com.example.examscanner.components.scan_exam.capture.camera;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.camera.camera2.Camera2Config;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraXConfig;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.examscanner.R;
import com.example.examscanner.components.scan_exam.capture.CameraOutputHandlerImpl;
import com.example.examscanner.log.ESLogeerFactory;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import static androidx.constraintlayout.widget.Constraints.TAG;

class CameraManagerImpl implements CameraXConfig.Provider,CameraManager{
    private static String TAG = "ExamScanner";
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private FragmentActivity activity;
    private ImageCapture imageCapture;
    private View root;
    private Executor executor;

    public CameraManagerImpl(FragmentActivity activity, View root) {
        this.activity = activity;
        this.root = root;
        executor = ContextCompat.getMainExecutor(activity);
    }

    @Override
    public void setUp() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(activity);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                ESLogeerFactory.getInstance().log(TAG, "setUp()", e);
                e.printStackTrace();
            }
        }, executor);
    }
    @SuppressLint("RestrictedApi")
    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder()
                .setTargetName("Preview")
                .build();
        PreviewView previewView = (PreviewView)root.findViewById(R.id
                .preview_view);
        preview.setSurfaceProvider(previewView.getPreviewSurfaceProvider());
        imageCapture =
                new ImageCapture.Builder()
                        .setTargetRotation(previewView.getDisplay().getRotation())
                        .build();

        CameraSelector cameraSelector =
                new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        cameraProvider.bindToLifecycle(activity, cameraSelector, preview, imageCapture);
    }

    @Override
    public View.OnClickListener createCaptureClickListener(CameraOutputHander handler){
        return new View.OnClickListener() {
            final ImageCapture.OutputFileOptions outputFileOptions =
                    new ImageCapture.OutputFileOptions.Builder(new File(activity.getFilesDir(),"foo.jpg")).build();
            @Override
            public void onClick(View view) {
                imageCapture.takePicture(
                        outputFileOptions,
                        executor,
                        new ImageCapture.OnImageSavedCallback(){
                            @RequiresApi(api = Build.VERSION_CODES.P)
                            @Override
                            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                                Matrix matrix = new Matrix();
                                matrix.postRotate(90f);
                                Bitmap source = BitmapFactory.decodeFile(activity.getFilesDir()+"/foo.jpg");
                                Bitmap trans = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
                                handler.handleBitmap(trans);
                            }

                            @SuppressLint("RestrictedApi")
                            @Override
                            public void onError(@NonNull ImageCaptureException exception) {
                                ESLogeerFactory.getInstance().log(TAG, "imageCapture.takePicture()", exception);
                                CameraManagerImpl.this.onDestroy();
                                exception.printStackTrace();
                            }
                        }

                );
            }
        };
    }
    @NonNull
    @Override
    public CameraXConfig getCameraXConfig() {
        return Camera2Config.defaultConfig();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onDestroy(){
        cameraProviderFuture = ProcessCameraProvider.getInstance(activity);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                cameraProvider.unbindAll();
//                cameraProvider.shutdown();
                Log.d(TAG, "CameraManagerImpl::onDestroy");
            } catch (ExecutionException | InterruptedException e) {
                ESLogeerFactory.getInstance().log(TAG, "CameraManagerImpl::onDestroy ERROR", e);
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(activity));
    }
    @Override
    public void onPause(){
        cameraProviderFuture = ProcessCameraProvider.getInstance(activity);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                unbind(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                ESLogeerFactory.getInstance().log(TAG, "onPause()", e);
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(activity));
    }

    private void unbind(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
    }
}
