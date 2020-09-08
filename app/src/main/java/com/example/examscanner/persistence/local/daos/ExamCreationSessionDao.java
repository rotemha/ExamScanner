package com.example.examscanner.persistence.local.daos;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.examscanner.persistence.local.entities.ExamCreationSession;

@Dao
public interface ExamCreationSessionDao {
    @Insert
    long insert(ExamCreationSession examCreationSession);
}
