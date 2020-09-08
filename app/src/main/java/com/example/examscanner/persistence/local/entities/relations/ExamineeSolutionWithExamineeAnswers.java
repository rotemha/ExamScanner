package com.example.examscanner.persistence.local.entities.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.examscanner.persistence.local.entities.ExamineeAnswer;
import com.example.examscanner.persistence.local.entities.ExamineeSolution;

import java.util.List;

public class ExamineeSolutionWithExamineeAnswers {
    @Embedded
    private ExamineeSolution examineeSolution;
    @Relation(
            parentColumn = ExamineeSolution.pkName,
            entityColumn = ExamineeAnswer.fkESid
    )
    private List<ExamineeAnswer> examineeAnswers;

    public ExamineeSolutionWithExamineeAnswers(ExamineeSolution examineeSolution, List<ExamineeAnswer> examineeAnswers) {
        this.examineeSolution = examineeSolution;
        this.examineeAnswers = examineeAnswers;
    }

    public ExamineeSolution getExamineeSolution() {
        return examineeSolution;
    }

    public void setExamineeSolution(ExamineeSolution examineeSolution) {
        this.examineeSolution = examineeSolution;
    }

    public List<ExamineeAnswer> getExamineeAnswers() {
        return examineeAnswers;
    }

    public void setExamineeAnswers(List<ExamineeAnswer> examineeAnswers) {
        this.examineeAnswers = examineeAnswers;
    }
}
