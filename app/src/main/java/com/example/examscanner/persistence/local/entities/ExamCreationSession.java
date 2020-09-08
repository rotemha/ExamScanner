package com.example.examscanner.persistence.local.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ExamCreationSession {
    public static final String pkName = "id";
    @PrimaryKey(autoGenerate = true)
    private long id;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
