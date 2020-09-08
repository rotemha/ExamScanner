package com.example.examscanner.persistence.remote.files_management;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface RemoteFilesManager {
    public Completable store(String path, byte[] data);
    public Observable<byte[]> get(String path);
    public void tearDown();
    public void setTestMode();
    public Observable<String> createUrl(String pathToRemoteBm);
    public void deleteExam(String remoteId);
}
