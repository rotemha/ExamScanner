package com.example.examscanner.log;

import android.util.Log;

class EsLoggeImpl implements ESLogger {
    FirebaseESLogger remoteLogger;
    public EsLoggeImpl() {
        remoteLogger = new FirebaseESLogger();
    }

    @Override
    public void setupUserIdentifier(String identifier) {
        remoteLogger.setupUserIdentifier(identifier);
    }

    @Override
    public void log(String tag, String message, Throwable e) {
        Log.d(tag, message, e);
        remoteLogger.log(tag, message, e);
    }

    @Override
    public void logmem() {
        remoteLogger.logmem();
    }

    @Override
    public void log(String tag, String format) {
        Log.d(tag, format);
        remoteLogger.log(tag, format);
    }
}
