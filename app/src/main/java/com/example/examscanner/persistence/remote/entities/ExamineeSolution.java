package com.example.examscanner.persistence.remote.entities;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.HashMap;
import java.util.Map;

public class ExamineeSolution {
    private static int LOW_VALID_ANS = 1;
    private static int HIGH_VALID_ANS = 5;
    public static String metaAnswers = "answers";
    public static String metaVersionId = "versionId";
    public static Object metaGrade = "grade";
    public static Object metaGrader = "graderEmail";
    public static String metaBitmapUrl = "bitmapUrl";
    public static String metaIsValid ="isValid";
    public static String metaOrigBitmapUrl = "origBitmapUrl";
    public static String metaExamineeId = "examineeId";
    public Map<Integer, Integer> answers;
    public String versionId;
    public Float grade;
    private String id;
    public String examineeId;
    public String bitmapUrl;
    public String origBitmapUrl;
    public boolean isValid;

    public ExamineeSolution(String versionId, Map<Integer, Integer> answers, boolean isValid) {
        this.examineeId = examineeId;
        this.versionId = versionId;
        this.grade = grade;
        this.answers = answers;
        this.isValid =isValid;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ExamineeSolution(String examineeId, String versionId, float grade, Map<Integer, Integer> answers, String bitmapUrl, String origBitmapUrl, boolean isValid) {
        this.examineeId = examineeId;
        this.versionId = versionId;
        this.grade = grade;
        this.answers = answers;
        this.bitmapUrl = bitmapUrl;
        this.origBitmapUrl = origBitmapUrl;
        this.isValid =isValid;
        for (Integer key:answers.keySet()) {
            final Integer val = answers.get(key);
            int mapVal = mapAns(val);
            if(mapVal!=val){
                answers.replace(key,mapVal);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ExamineeSolution getInvalidInstance() {
        return new ExamineeSolution(
                "invalid_examineeId",
                "invalid_versionID",
                0,
                new HashMap<>(),
                "invalid_nitmapPath",
                "invalid_nitmapPath",
                false
        );
    }

    public String _getId(){
        return id;
    }
    public void _setId(String id){
        this.id=id;
    }

    public static int mapAns(int ans){
        if(LOW_VALID_ANS <= ans && ans <= HIGH_VALID_ANS){
            return ans;
        }else{
            return -1;
        }
    }
}
