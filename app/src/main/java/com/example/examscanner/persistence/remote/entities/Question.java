package com.example.examscanner.persistence.remote.entities;

public class Question {
    public static String metaNum = "num";
    public static String metaAns = "ans";
    public static String metaLeft = "left";
    public static String metaUp = "up";
    public static String metaRight = "right";
    public static String metaBottom = "bottom";
    public static String metaVersionId = "versionId";
    public int num;
    public int ans;
    public int left;
    public int up;
    public int right;
    public String versionId;
    public int bottom;
    private String id;

    public Question(int num, int ans, int left, int up, int right, String versionId, int bottom) {
        this.num = num;
        this.ans = ans;
        this.left = left;
        this.up = up;
        this.right = right;
        this.versionId = versionId;
        this.bottom = bottom;
    }

    public String _getId() {
        return id;
    }

    public void _setId(String id) {
        this.id = id;
    }
}
