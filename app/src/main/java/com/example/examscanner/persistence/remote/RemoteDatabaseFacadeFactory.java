package com.example.examscanner.persistence.remote;

import com.google.firebase.database.FirebaseDatabase;

public class RemoteDatabaseFacadeFactory {
    private static RemoteDatabaseFacade instance;
    public static RemoteDatabaseFacade get(){
        if(instance==null){
            instance = new RemoteDatabaseFacadeImpl();
        }
        return instance;
    }

    public static void tearDown() {
        if(FirebaseDatabaseFactory.inTestMode){
            FirebaseDatabaseFactory.get().getReference().setValue(null);
        }
        instance=null;
    }

    public static void setStubInstance(RemoteDatabaseFacade stubInstance) {
        instance = stubInstance;
    }
}
