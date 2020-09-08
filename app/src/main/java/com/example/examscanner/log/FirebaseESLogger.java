package com.example.examscanner.log;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

class FirebaseESLogger implements ESLogger {

    private static final boolean LOG_MEM = true;
    @Override
    public void setupUserIdentifier(String identifier) {
        FirebaseCrashlytics.getInstance().setUserId(identifier);
    }

    @Override
    public void log(String tag, String message, Throwable e) {
        FirebaseCrashlytics.getInstance().setCustomKey("TAG", tag);
        FirebaseCrashlytics.getInstance().log(message);
        FirebaseCrashlytics.getInstance().recordException(e);
    }

    @Override
    public void logmem() {
        if(LOG_MEM){
            log("","", new LOG_MEM_EXCEPTION());
        }
    }

    @Override
    public void log(String tag, String format) {
        FirebaseCrashlytics.getInstance().setCustomKey("TAG", tag);
        FirebaseCrashlytics.getInstance().log(format);
    }
    private static class LOG_MEM_EXCEPTION extends RuntimeException{}
}
