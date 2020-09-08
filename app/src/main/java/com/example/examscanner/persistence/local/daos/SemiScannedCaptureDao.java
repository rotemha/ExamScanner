package com.example.examscanner.persistence.local.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.examscanner.persistence.local.entities.SemiScannedCapture;
import com.example.examscanner.persistence.local.entities.relations.SESessionWithSSCs;

import java.util.List;

@Dao
public interface SemiScannedCaptureDao {

    @Query("SELECT *  FROM semiscannedcapture")
    List<SemiScannedCapture> getAll();

    @Query("SELECT *  FROM semiscannedcapture WHERE id IS :id LIMIT 1")
    SemiScannedCapture findById(long id);

    @Insert
    long[] insertAll(SemiScannedCapture... sscs);

    @Insert
    long insert(SemiScannedCapture sscs);

    @Transaction
    @Query("SELECT *  FROM ScanExamSession WHERE id IS :sId LIMIT 1")
    SESessionWithSSCs getSessionWithSSCs(long sId);
}
