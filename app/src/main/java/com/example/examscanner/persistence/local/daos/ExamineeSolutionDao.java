package com.example.examscanner.persistence.local.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.examscanner.persistence.local.entities.ExamineeSolution;
import com.example.examscanner.persistence.local.entities.relations.ExamineeSolutionWithExamineeAnswers;
import com.example.examscanner.persistence.local.entities.relations.SESessionWithExamineeSolutions;

import java.util.List;

@Dao
public interface ExamineeSolutionDao {
    @Insert
    long insert(ExamineeSolution es);

    @Query("SELECT * FROM ExamineeSolution")
    List<ExamineeSolution> getAll();

    @Transaction
    @Query("SELECT * FROM examineesolution")
    List<ExamineeSolutionWithExamineeAnswers> getExamineeSolutionWithExamineeAnswers();

    @Transaction
    @Query("SELECT *  FROM ScanExamSession WHERE id IS :sId LIMIT 1")
    SESessionWithExamineeSolutions getSessionWithExamineeSolutions(long sId);

    @Query("SELECT * FROM ExamineeSolution WHERE id IS :sId LIMIT 1")
    ExamineeSolution getById(long sId);

    @Delete
    public void delete(ExamineeSolution es);

    @Update
    void update(ExamineeSolution es);
}
