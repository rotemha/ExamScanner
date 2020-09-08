package com.example.examscanner.communication.tasks;

public class IdsGenerator {
    public static String forSolution(long solId){
        return "SOL_"+String.valueOf(solId);
    }
}
