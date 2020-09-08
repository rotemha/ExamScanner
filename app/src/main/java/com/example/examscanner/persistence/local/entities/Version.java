package com.example.examscanner.persistence.local.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        indices = {@Index(value = {"examId", "verNum"}, unique = true)},
        foreignKeys ={@ForeignKey(onDelete = CASCADE,entity = Exam.class,
        parentColumns = Exam.pkName,childColumns = Version.fkExam)}
        )
public class Version {
    public static final String pkName = "id";

    @PrimaryKey(autoGenerate = true)
    private long id;
    public static final String fkExam = "examId";
    @ForeignKey(entity = Exam.class, parentColumns =Exam.pkName , childColumns =fkExam )
    private long examId;

    public void setRemoteVersionId(String remoteVersionId) {
        this.remoteVersionId = remoteVersionId;
    }

    private String remoteVersionId;
    private boolean isUploaded;

    public boolean isDownloaded() {
        return isDownloaded;
    }

    public void setDownloaded(boolean downloaded) {
        isDownloaded = downloaded;
    }

    private boolean isDownloaded;

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    private int verNum;
    private boolean bitmapUploaded;

    public boolean isBitmapUploaded() {
        return bitmapUploaded;
    }

    public void setBitmapUploaded(boolean bitmapUploaded) {
        this.bitmapUploaded = bitmapUploaded;
    }

    public Version(int verNum, long examId, String remoteVersionId, boolean isUploaded, boolean isDownloaded, boolean bitmapUploaded) {
        this.verNum = verNum;
        this.examId = examId;
        this.remoteVersionId = remoteVersionId;
        this.isUploaded = isUploaded;
        this.isDownloaded = isDownloaded;
        this.bitmapUploaded = bitmapUploaded;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getExamId() {
        return examId;
    }

    public void setExamId(long examId) {
        this.examId = examId;
    }

    public int getVerNum() {
        return verNum;
    }

    public String getRemoteVersionId() {
        return remoteVersionId;
    }

    public String _getBitmapPath(){
        return "VERISION_"+String.valueOf(id);
    }



}
