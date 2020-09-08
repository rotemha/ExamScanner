package com.example.examscanner.authentication;

import android.content.Intent;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;

public interface AuthenticationHandler<T> {
    public Observable<T> authenticate();
    public Observable<String> authenticateAndReturnId();
    public Observable<T> authenticate(String username, String password);

    Observable<Object> signIn(String username, String pas);
}
