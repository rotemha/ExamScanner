package com.example.examscanner.persistence.local.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.examscanner.persistence.local.entities.Exam;
import com.example.examscanner.persistence.local.entities.relations.ExamWithVersions;

import java.util.List;

@Dao
public interface ExamDao {
    @Insert
    long insert(Exam exam);

    @Query("SELECT *  FROM exam WHERE id IS :id LIMIT 1")
    Exam getById(long id);

    @Transaction
    @Query("SELECT * FROM exam WHERE id IS :id LIMIT 1")
    ExamWithVersions getExamWithVersions(long id);

    @Query("SELECT * FROM exam WHERE examCreationSessionId IS :sId LIMIT 1")
    long getBySessionId(long sId);

    @Query("SELECT *  FROM exam")
    List<Exam> getAll();

    @Update
    void update(Exam exam);

    @Query("SELECT * FROM exam WHERE courseName IS :courseName AND semester IS :semester AND term IS :term AND year IS :year LIMIT 1")
    Exam getByCoursenameSemeseterTermYear(String courseName, int semester, int term, String year);

    @Delete
    void delete(Exam e);
}
