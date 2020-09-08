package com.example.examscanner.log;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

public interface ESLogger {
    public void setupUserIdentifier(String identifier);
    public void log(String tag, String message, Throwable e);
    public void logmem();

    void log(String tag, String format);
}
