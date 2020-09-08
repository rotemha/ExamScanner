package com.example.examscanner.persistence.local.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.examscanner.persistence.local.entities.ExamineeAnswer;
import com.example.examscanner.persistence.local.entities.relations.ExamineeSolutionWithExamineeAnswers;

@Dao
public interface ExamineeAnswerDao {

    @Insert
    long insert(ExamineeAnswer examineeAnswer);

    @Query("SELECT * FROM EXAMINEESOLUTION WHERE id IS :esId LIMIT 1")
    ExamineeSolutionWithExamineeAnswers getExamineeSolutionWithExamineeAnswers(long esId);

    @Query("SELECT * FROM ExamineeAnswer WHERE id IS :id LIMIT 1")
    ExamineeAnswer getById(long id);

    @Query("SELECT * FROM ExamineeAnswer WHERE examineeSolutionId IS :solutionId AND questionId IS :questionId LIMIT 1")
    ExamineeAnswer getBySolutionIdAndQuestionId(long solutionId, long questionId);

    @Update
    void update(ExamineeAnswer ea);
}
