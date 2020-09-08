package com.example.examscanner.repositories.exam;

import io.reactivex.Observable;

public interface ExamineeIdsSocket {
    static ExamineeIdsSocket getEmpty() {
        return new ExamineeIdsSocket() {
            @Override
            public Observable<String> observe() {
                return Observable.empty();
            }
        };
    }

    public Observable<String> observe();
}
