package com.example.examscanner.repositories.grader;

public class Grader {
    private String userEmail;
    private String userId;

    public Grader(String userEmail, String userId) {
        this.userEmail = userEmail;
        this.userId  =userId;
    }


    public String getUserEmail() {
        return userEmail;
    }

    public String getId(){
        return userId;
    }

    public String getIdentifier(){
        return userEmail;
    }
}
