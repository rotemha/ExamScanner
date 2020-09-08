package com.example.examscanner.authentication;

public class UIAuthenticationHandlerFactory {
    public static UIAuthenticationHandler get(){
        return new UIFirebaseAuthenticationHandler();
    }
    public static void setTestMode(){
        UIFirebaseAuthenticationHandler.smartLockEnabled=false;
    }
    public static void tearDown(){
        UIFirebaseAuthenticationHandler.smartLockEnabled=true;
    }
}
