package com.example.examscanner;

import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.examscanner.authentication.UIAuthenticationHandler;
import com.example.examscanner.authentication.state.State;
import com.example.examscanner.authentication.state.StateHolder;
import com.example.examscanner.log.ESLogeerFactory;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.grader.Grader;

import io.reactivex.Completable;

public class MainActivityViewModel<T> extends ViewModel implements StateHolder {

    private static final String APP_TAG = "ExamScanner";
    private static String MSG_PREF = "MainActivityViewModel::";
    private final UIAuthenticationHandler tUIAuthenticationHandler;
    private State theState;
    private Repository<Grader> graderRepo;



    public MainActivityViewModel(State theState, UIAuthenticationHandler<T> tUIAuthenticationHandler, Repository<Grader> graderRepo) {
        this.tUIAuthenticationHandler = tUIAuthenticationHandler;
        this.theState = theState;
        this.graderRepo = graderRepo;
    }

    public boolean isAuthenticated() {
        return theState.isAuthenticated();
    }

    public void authenticate() {
        theState.login(this, tUIAuthenticationHandler.getOnResultContent());
        Completable.fromAction(() ->
                graderRepo.create(new Grader(theState.getUserEmail(), theState.getId())))
                .subscribe(()->{},
                        (t)-> { Log.d(APP_TAG, MSG_PREF, t);});
        ESLogeerFactory.getInstance().setupUserIdentifier(theState.getUserEmail());

    }

    @Override
    public void setState(State s) {
        this.theState = s;
    }

    public Object getState() {
        return theState.getContent();
    }

    public Intent generateAuthenticationIntent() {
        return tUIAuthenticationHandler.generateAuthenticationIntent();
    }
}
