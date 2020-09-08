package com.example.examscanner.persistence.local.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.examscanner.persistence.local.entities.ScanExamSession;

import java.util.List;

@Dao
public interface ScanExammSessionDao {
    @Query("SELECT * FROM ScanExamSession")
    List<ScanExamSession> getAll();

    @Query("SELECT * FROM ScanExamSession WHERE id IS :scanExamSessionId LIMIT 1")
    ScanExamSession getById(long scanExamSessionId);

    @Insert
    long[] insertAll(ScanExamSession... scanExamSessions);

    @Insert
    long insert(ScanExamSession scanExamSession);

    @Delete
    void delete(ScanExamSession scanExamSession);
}
