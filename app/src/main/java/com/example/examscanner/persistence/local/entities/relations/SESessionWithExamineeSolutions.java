package com.example.examscanner.persistence.local.entities.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.examscanner.persistence.local.entities.ScanExamSession;
import com.example.examscanner.persistence.local.entities.ExamineeSolution;

import java.util.List;

public class SESessionWithExamineeSolutions {
    @Embedded
    private ScanExamSession scanExamSession;
    @Relation(parentColumn = ScanExamSession.pkName, entityColumn =ExamineeSolution.fkSession )
    private List<ExamineeSolution> examineeSolutions;

    public SESessionWithExamineeSolutions(ScanExamSession scanExamSession, List<ExamineeSolution> examineeSolutions) {
        this.scanExamSession = scanExamSession;
        this.examineeSolutions = examineeSolutions;
    }

    public ScanExamSession getScanExamSession() {
        return scanExamSession;
    }

    public void setScanExamSession(ScanExamSession scanExamSession) {
        this.scanExamSession = scanExamSession;
    }

    public List<ExamineeSolution> getExamineeSolutions() {
        return examineeSolutions;
    }

    public void setExamineeSolutions(List<ExamineeSolution> examineeSolutions) {
        this.examineeSolutions = examineeSolutions;
    }
}
