package com.example.examscanner.persistence.local.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Exam {
    public static final String pkName = "id";
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String courseName;
    private int term;
    private String year;
    private String url;
    private int semester;
    public static final String fkSid = "examCreationSessionId";
    @ForeignKey(entity = ExamCreationSession.class, parentColumns = ExamCreationSession.pkName , childColumns ="examCreationSessionId" )
    private long examCreationSessionId;
    private String remoteId;
    private int numberOfQuestions;
    private String managerId;
    private String[] gradersIds;
    private int uploaded;

    public int getNumOfVersions() {
        return numOfVersions;
    }

    public void setNumOfVersions(int numOfVersions) {
        this.numOfVersions = numOfVersions;
    }

    private int numOfVersions;
    private boolean downloaded;


    public Exam(String courseName, int term, String year, String url, int semester, long examCreationSessionId, String remoteId, int numberOfQuestions, String managerId, String[] gradersIds, int uploaded, int numOfVersions, boolean downloaded) {
        this.courseName = courseName;
        this.term = term;
        this.year = year;
        this.url = url;
        this.semester = semester;
        this.examCreationSessionId = examCreationSessionId;
        this.remoteId = remoteId;
        this.numberOfQuestions = numberOfQuestions;
        this.managerId = managerId;
        this.gradersIds = gradersIds;
        this.uploaded = uploaded;
        this.numOfVersions = numOfVersions;
        this.downloaded = downloaded;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public long getId() {
        return id;
    }

    public long getExamCreationSessionId() {
        return examCreationSessionId;
    }

    public void setExamCreationSessionId(long examCreationSessionId) {
        this.examCreationSessionId = examCreationSessionId;
    }


    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRemoteId() {
        return remoteId;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public String  getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String[] getGradersIds() {
        return gradersIds;
    }

    public void setGradersIds(String[] gradersIds) {
        this.gradersIds = gradersIds;
    }


    public int getUploaded() { return uploaded;}
    public void setUploaded(int uploaded) { this.uploaded = uploaded;}

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }
}
