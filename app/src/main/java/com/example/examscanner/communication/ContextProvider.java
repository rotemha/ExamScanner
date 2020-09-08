package com.example.examscanner.communication;

import android.content.Context;

public class ContextProvider {
    private static Context context;
    public static void set(Context theContext){
        context = theContext;
    }
    public static Context get(){
        return context;
    }
}
