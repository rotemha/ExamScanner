package com.example.examscanner.communication.tasks;

import io.reactivex.Completable;

public class VeporizedTask extends Task {
    public VeporizedTask(Completable completable, String desc) {
        super(completable, desc);
    }

    @Override
    public void addContinuation(Continuation continuation, Continuation onVeporized) {
        onVeporized.cont();
    }
}
