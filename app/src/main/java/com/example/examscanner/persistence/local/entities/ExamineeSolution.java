package com.example.examscanner.persistence.local.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity
public class ExamineeSolution {
    public static final String pkName = "id";
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String examineeId;
    public static final String fkSession = "sessionId";
    public static final String fkVersion = "versionId";
    @ForeignKey(entity = ScanExamSession.class, parentColumns = ScanExamSession.pkName, childColumns = fkSession)
    private long sessionId;
    @ForeignKey(entity = Version.class, parentColumns = Version.pkName, childColumns = fkVersion)
    private long versionId;
    private String remoteId;
    private boolean examineeIdOccupied;
    private boolean isValid;

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public boolean isBitmapUploaded() {
        return bitmapUploaded;
    }

    public void setBitmapUploaded(boolean bitmapUploaded) {
        this.bitmapUploaded = bitmapUploaded;
    }

    private boolean isApproved;
    private boolean bitmapUploaded;

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public ExamineeSolution(String examineeId, long sessionId, long versionId, String remoteId, boolean isValid, boolean isApproved, boolean bitmapUploaded) {
        this.examineeId = examineeId;
        this.sessionId = sessionId;
        this.versionId = versionId;
        this.remoteId = remoteId;
        this.isApproved = isApproved;
        this.bitmapUploaded = bitmapUploaded;
        examineeIdOccupied = false;
        this.isValid = isValid;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public long getId() {
        return id;
    }

    public String getExamineeId() {
        return examineeId;
    }

    public void setExamineeId(String examineeId) {
        this.examineeId = examineeId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getVersionId() {
        return versionId;
    }

    public void setVersionId(long versionId) {
        this.versionId = versionId;
    }

    public String getBitmapPath() {
        return "SOLUTION_"+String.valueOf(id);
    }

    public String getRemoteId() {
        return remoteId;
    }
    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }


    public void setExamineeIdOccupied(boolean b) {
        examineeIdOccupied = b;
    }

    public boolean isExamineeIdOccupied() {
        return examineeIdOccupied;
    }
}
