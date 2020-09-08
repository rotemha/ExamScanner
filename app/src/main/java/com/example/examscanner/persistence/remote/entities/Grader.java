package com.example.examscanner.persistence.remote.entities;

public class Grader {
    public static String metaUserId ="userId";
    public static String metaEmail ="email";
    public String userId;
    public String email;

    public Grader(String email , String id) {
        this.userId = id;
        this.email = email;
    }
}
