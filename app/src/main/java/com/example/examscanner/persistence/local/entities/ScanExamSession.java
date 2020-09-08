package com.example.examscanner.persistence.local.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity
public class ScanExamSession {
    public static final String pkName = "id";
    @PrimaryKey(autoGenerate = true)
    private long id;
    public static final String fkSid = "examIdOfSession";
    @ForeignKey(entity = Exam.class, parentColumns = Exam.pkName , childColumns ="examIdOfSession" )
    private long examIdOfSession;

    public ScanExamSession(long examIdOfSession) {
        this.examIdOfSession = examIdOfSession;
    }

    public long getExamIdOfSession() {
        return examIdOfSession;
    }

    public void setExamIdOfSession(long examIdOfSession) {
        this.examIdOfSession = examIdOfSession;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
