package com.example.examscanner.components.create_exam.get_spreedsheet_url;

public class SpreedsheetUrlGetterFactory {
    private static SpreedsheetUrlGetter instance;
    public static SpreedsheetUrlGetter get(){
        if(instance == null){
            instance = new SpreedsheetUrlGetterImpl();
        };
        return instance;
    }
}
