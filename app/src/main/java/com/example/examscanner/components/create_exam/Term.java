package com.example.examscanner.components.create_exam;

public class Term {
    private  int value;
    private String viewValue;

    public static Term createByViewValue(String viewValue){
        if(viewValue.equals("A")) return new Term(0,"A");
        if(viewValue.equals("B")) return new Term(1,"B");
        if(viewValue.equals("C")) return new Term(2,"C");
        return null;
    }

    private Term(int value, String viewValue){

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
