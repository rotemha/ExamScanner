package com.example.examscanner.persistence.remote.files_management;



public class RemoteFilesManagerFactory {
    private static RemoteFilesManager instance;
    public static RemoteFilesManager get(){
        if(instance==null){
            instance  = new RemoteFilesManagerImpl();
        }
        return instance;
    }
    public static void tearDown(){
        instance.tearDown();
    }

    public static void setTestMode(){
        get().setTestMode();
    }

    public static void setStubInstabce(RemoteFilesManager remoteFilesManagerStub) {
        instance = remoteFilesManagerStub;
    }
}
