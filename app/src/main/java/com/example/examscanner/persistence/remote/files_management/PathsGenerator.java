package com.example.examscanner.persistence.remote.files_management;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.nio.file.Paths;

public class PathsGenerator {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String genVersionPath(String examId, int verNum){
        return Paths.get(examId, "VER_"+String.valueOf(verNum)).toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String genExamineeSolution(String examId, String solutionId){
        return Paths.get(examId, canonic(solutionId)).toString();
    }

    private static String canonic(String solutionId) {
        return "SOL_"+solutionId.replace('/','_').replace('\\', '_');
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String genExamineeSolutionOrig(String remoetExamId, String solutionId) {
        return Paths.get(remoetExamId, "ORIG",canonic(solutionId)).toString();
    }
}
