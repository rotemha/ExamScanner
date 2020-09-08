package com.example.examscanner.persistence.local.entities.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.examscanner.persistence.local.entities.Question;
import com.example.examscanner.persistence.local.entities.Version;

import java.util.List;

public class VersionWithQuestions {
    @Embedded
    private Version version;

    @Relation(
            parentColumn = Version.pkName,
            entityColumn = Question.fkVer
    )
    private List<Question> questions;

    public VersionWithQuestions(Version version, List<Question> questions) {
        this.version = version;
        this.questions = questions;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
