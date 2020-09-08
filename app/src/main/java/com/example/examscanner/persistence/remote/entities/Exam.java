package com.example.examscanner.persistence.remote.entities;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class Exam {

    public static String metaManager = "manager";
    public static String metaGraders = "graders";
    public static String metaCourseName = "courseName";
    public static String metaSemester = "semester";
    public static String metaTerm = "term";
    public static String metaYear = "year";
    public static String metaSeal = "seal";
    public static String metaUrl = "url";
    public static String metaqnum = "numberOfQuestions";
    public static String metaUploaded = "uploaded";
    public static String metaNumOfVersions = "numOfVersions";
    public static String metaTotal = "total";
    public static String metaAverage = "average";
    public String manager;
    public List<String> graders;
    public String courseName;
    public int semester;
    public int term;
    public String year;
    public boolean seal = false;
    public String url;
    private String id;
    public int numberOfQuestions;
    public int uploaded;
    public int numOfVersions;
    public int total;
    public float average;

    public Exam() {
    }

    public Exam(String manager, List<String> graders, String courseName, int semester, int term, String year, boolean seal, String url, int numberOfQuestions, int uploaded, int numOfVersions) {
        this.manager = manager;
        this.graders = graders;
        this.courseName = courseName;
        this.semester = semester;
        this.term = term;
        this.year = year;
        this.seal = seal;
        this.url = url;
        this.numberOfQuestions = numberOfQuestions;
        this.uploaded = uploaded;
        this.numOfVersions = numOfVersions;
        this.total = 0;
        this.average = 0;
    }
    public static Exam theDeletedExam(String key) {
        Exam exam = new Exam(){
            @Override
            public boolean _isDeleted() {
                return true;
            }
        };
        exam._setId(key);
        return exam;
    }

    public boolean _isDeleted(){
        return false;
    }

    public String _getId() {
        return id;
    }
    public void  _setId(String theId) {
        id= theId;
    }
    public boolean _isUploaded(){
        return uploaded >0;
    }
}
