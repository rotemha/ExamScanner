package com.example.examscanner.persistence.local.entities.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.examscanner.persistence.local.entities.Exam;
import com.example.examscanner.persistence.local.entities.Version;

import java.util.List;

public class ExamWithVersions {
    @Embedded
    private Exam exam;

    @Relation(
            parentColumn = Exam.pkName,
            entityColumn = Version.fkExam
    )
    private List<Version> versions;

    public ExamWithVersions(Exam exam, List<Version> versions) {
        this.exam = exam;
        this.versions = versions;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public void setVersions(List<Version> versions) {
        this.versions = versions;
    }

    public List<Version> getVersions() {
        return versions;
    }
}
