package com.example.examscanner.persistence.remote;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDatabaseFactory {
    private static FirebaseDatabase instance;
    private static FirebaseDatabase testInstance;
    static boolean inTestMode=false;
    public static FirebaseDatabase get() {
        if(inTestMode){
            return getTestInstance();
        }else{
            return getRealInstance();
        }
    }

    public static void setTestMode() {
        inTestMode=true;
    }

    private static FirebaseDatabase getRealInstance() {
        if(instance==null){
            instance = FirebaseDatabase.getInstance();
        }
        return instance;
    }

    private static FirebaseDatabase getTestInstance() {
        if(testInstance==null){
            testInstance = FirebaseDatabase.getInstance("http://10.0.2.2:9000?ns=examscanner-de46e");
        }
        return testInstance;
    }
}
