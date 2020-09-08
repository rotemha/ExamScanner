package com.example.examscanner.components.create_exam;

public class Semester {
    private final int value;

    private final String viewValue;
    public static Semester createByViewValue(String viewValue){
        if(viewValue.equals("Fall")) return new Semester(0,"Fall");
        if(viewValue.equals("Spring")) return new Semester(1,"Spring");
        if(viewValue.equals("Summer")) return new Semester(2,"Summer");
        return null;
    }

    private Semester(int value, String viewValue){

        this.value = value;
        this.viewValue = viewValue;
    }
    public int getValue() {
        return value;
    }

    public String getViewValue() {
        return viewValue;
    }
}
