package com.example.examscanner.components.create_exam.get_version_file;

public class VersionImageGetterFactory {
    private static VersionImageGetter stubInstance;
    public VersionImageGetter create(){
        if(stubInstance!=null){
            return stubInstance;
        }
        return new PDFVersionImageImpl();
    }
    public static void setStubInstance(VersionImageGetter theStubInstance){
        stubInstance = theStubInstance;
    }


}
