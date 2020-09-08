package com.example.examscanner.log;

public class ESLogeerFactory {
    public static ESLogger instance;
    public static synchronized ESLogger getInstance(){
        if(instance ==null){
            instance = new EsLoggeImpl();
        }
        return instance;
    }
}
