package com.example.examscanner.persistence.local.files_management;

import android.content.Context;

import com.example.examscanner.communication.ContextProvider;
import com.example.examscanner.communication.RealFacadeImple;

import org.jetbrains.annotations.NotNull;

public class FilesManagerFactory {
    private static boolean testMode = false;
    private static FilesManager instance;
    public static FilesManager create(){
        return getRealInstance();
    }

    private static FilesManager getRealInstance() {
        if(instance == null){
            instance = new FilesManagerImpl(ContextProvider.get());
        }
        return instance;
    }



    public static void tearDown() {
        if(instance!=null)instance.tearDown();
        instance= null;
    }

    public static void setTestMode(Context c) {
        FilesManagerImpl.setTestMode();
        instance = new FilesManagerImpl(c);
    }

    public static void setStubInstance(FilesManager fm){
        instance=fm;
    }
}
