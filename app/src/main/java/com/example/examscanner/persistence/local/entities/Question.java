package com.example.examscanner.persistence.local.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        indices = {@Index(value = {"verId", "questionNum"},unique = true)},
        foreignKeys ={@ForeignKey(onDelete = CASCADE,entity = Version.class,
                parentColumns = Version.pkName,childColumns = Question.fkVer)}
)
public class Question {
    public static final String pkName = "id";
    @PrimaryKey(autoGenerate = true)
    private long id;
    private int questionNum;
    public static final String fkVer = "verId";
    @ForeignKey(entity = Version.class, parentColumns =Version.pkName , childColumns = "verId")
    private long verId;
    private int correctAns;
    private int leftX;
    private int upY;
    private int rightX;
    private int borromY;

    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }
    private String remoteId;

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    private boolean isUploaded;
    private boolean downloaded;

    public Question(int questionNum, long verId, int correctAns, int leftX, int upY, int rightX, int borromY, String remoteId, boolean isUploaded, boolean downloaded) {
        this.questionNum = questionNum;
        this.verId = verId;
        this.correctAns = correctAns;
        this.leftX = leftX;
        this.upY = upY;
        this.rightX = rightX;
        this.borromY = borromY;
        this.remoteId = remoteId;
        this.isUploaded = isUploaded;
        this.downloaded = downloaded;
    }

    public int getLeftX() {
        return leftX;
    }

    public void setLeftX(int leftX) {
        this.leftX = leftX;
    }

    public int getUpY() {
        return upY;
    }

    public void setUpY(int upY) {
        this.upY = upY;
    }

    public int getRightX() {
        return rightX;
    }

    public void setRightX(int rightX) {
        this.rightX = rightX;
    }

    public int getBorromY() {
        return borromY;
    }

    public void setBorromY(int borromY) {
        this.borromY = borromY;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(int questionNum) {
        this.questionNum = questionNum;
    }

    public long getVerId() {
        return verId;
    }

    public void setVerId(long verId) {
        this.verId = verId;
    }

    public int getCorrectAns() {
        return correctAns;
    }

    public void setCorrectAns(int correctAns) {
        this.correctAns = correctAns;
    }

    public String getRemoteId() {
        return remoteId;
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }
}
