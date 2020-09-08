package com.example.examscanner.components.scan_exam.capture;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import com.example.examscanner.components.scan_exam.capture.camera.CameraOutputHander;
import com.example.examscanner.log.ESLogeerFactory;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class CameraOutputHandlerImpl implements CameraOutputHander {
    private static String TAG = "ExamScanner";
    private static String MSG_PREF = "CameraOutputHandlerImpl::";
    private CaptureViewModel captureViewModel;
    private final CompositeDisposable processRequestDisposableContainer;
    private OnBeggining cont;
    private OnEnding onEnd;
    private OnError onError;

    public CameraOutputHandlerImpl(CaptureViewModel captureViewModel, CompositeDisposable processRequestDisposableContainer, OnBeggining cont, OnEnding onEnd, OnError onError) {
        this.captureViewModel = captureViewModel;
        this.processRequestDisposableContainer = processRequestDisposableContainer;
        this.cont = cont;
        this.onEnd = onEnd;
        this.onError = onError;
    }

    @Override
    public View.OnClickListener handleBitmap(Bitmap bm) {
        captureViewModel.consumeCapture(
                new Capture(bm,
                        captureViewModel.getCurrentExamineeId().getValue(),
                        captureViewModel.getCurrentVersion().getValue()
                )
        );
        captureViewModel.clearExamineeId();
        captureViewModel.clearVersion();
        cont.cont();
        processRequestDisposableContainer.add(
                Completable.fromAction(this::processCapture)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onCaptureProcessed, this::onCapturePtocessError)
        );
        return null;
    }

    private void processCapture() {
        captureViewModel.processCapture();
    }

    private void onCapturePtocessError(Throwable throwable) {
        onError.cont(MSG_PREF + "onCapturePtocessError", throwable);
    }

    private void onCaptureProcessed() {
        Completable.fromAction(
                () -> {captureViewModel.postProcessCapture();}
        ).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(()->{
            ESLogeerFactory.getInstance().logmem();
        }, t->{
            ESLogeerFactory.getInstance().log(TAG, MSG_PREF,t );});
        onEnd.cont();
    }
    public interface OnBeggining {
        public void cont();
    }

    public interface OnEnding {
        public void cont();
    }

    public interface OnError {
        public void cont(String pref, Throwable t);
    }
}
