package com.example.examscanner.components.scan_exam.detect_corners;

import com.example.examscanner.persistence.remote.RemoteDatabaseFacade;
import com.example.examscanner.persistence.remote.entities.Exam;
import com.example.examscanner.persistence.remote.entities.Grader;
import com.example.examscanner.persistence.remote.entities.Question;
import com.example.examscanner.persistence.remote.entities.Version;
import com.example.examscanner.persistence.remote.files_management.RemoteFilesManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class RemoteFilesManagerStub implements RemoteFilesManager {

    @Override
    public Completable store(String path, byte[] data) {
        return Completable.fromAction(()->{});
    }

    @Override
    public Observable<byte[]> get(String path) {
        return Observable.fromCallable(()->new byte[0]);
    }

    @Override
    public void tearDown() {

    }

    @Override
    public void setTestMode() {

    }

    @Override
    public Observable<String> createUrl(String pathToRemoteBm) {
        return Observable.fromCallable(()->"");
    }

    @Override
    public void deleteExam(String remoteId) {

    }
}
