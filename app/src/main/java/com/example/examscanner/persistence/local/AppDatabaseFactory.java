package com.example.examscanner.persistence.local;

import android.util.Log;

import androidx.room.Room;


import com.example.examscanner.communication.ContextProvider;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static androidx.test.core.app.ApplicationProvider.*;


public class AppDatabaseFactory {
    private static final String TAG = "ExamScanner";
    private static AppDatabase instance;
    private static AppDatabase testInstance;
    private static boolean isTestMode = false;

    public static synchronized AppDatabase getInstance(){
        if(isTestMode){
            return getTestInstance();
        }else{
            return getRealInstance();
        }
    }
    private static AppDatabase getRealInstance(){
        if(instance==null){
            instance = Room.databaseBuilder(ContextProvider.get(),AppDatabase.class, "database-name")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
    private static AppDatabase getTestInstance(){
        if(testInstance==null){
            testInstance = Room.inMemoryDatabaseBuilder(getApplicationContext(),AppDatabase.class).build();
        }
        return testInstance;
    }
    public static void setTestMode(){
        Log.d(TAG, "Setting test mode in db");
        isTestMode=true;
    }

    public static void setTestInstance(AppDatabase testInstance){
        Log.d(TAG, "Setting test mode in db");
        isTestMode=true;
        AppDatabaseFactory.testInstance = testInstance;
    }

    public static void tearDownDb(){
        Log.d(TAG, "tearing db down");
        testInstance.clearAllTables();
    }
}
