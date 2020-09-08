package com.example.examscanner.persistence.local.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.examscanner.persistence.local.entities.Question;
import com.example.examscanner.persistence.local.entities.relations.VersionWithQuestions;

@Dao
public interface QuestionDao {
    @Insert
    long insert(Question question);

    @Transaction
    @Query("SELECT * FROM version WHERE id IS :vId LIMIT 1")
    VersionWithQuestions getVersionWithQuestions(long vId);


    @Query("SELECT * FROM question WHERE verId IS :verId AND questionNum IS :qNum LIMIT 1")
    Question getQuestionByVerIdAndQNum(long verId, int qNum);

    @Query("SELECT * FROM question WHERE id IS :qId LIMIT 1")
    Question get(long qId);

    @Update
    void update(Question maybeQuestion);
}
