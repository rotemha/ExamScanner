package com.example.examscanner.persistence.local.entities.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.examscanner.persistence.local.entities.ScanExamSession;
import com.example.examscanner.persistence.local.entities.SemiScannedCapture;

import java.util.List;

public class SESessionWithSSCs {
    @Embedded
    private ScanExamSession scanExamSession;

    @Relation(
            parentColumn = ScanExamSession.pkName,
            entityColumn = SemiScannedCapture.fkSessionId
    )
    private List<SemiScannedCapture> sscs;

    public SESessionWithSSCs(ScanExamSession scanExamSession, List<SemiScannedCapture> sscs) {
        this.scanExamSession = scanExamSession;
        this.sscs = sscs;
    }

    public ScanExamSession getScanExamSession() {
        return scanExamSession;
    }

    public void setScanExamSession(ScanExamSession scanExamSession) {
        this.scanExamSession = scanExamSession;
    }

    public List<SemiScannedCapture> getSscs() {
        return sscs;
    }

    public void setSscs(List<SemiScannedCapture> sscs) {
        this.sscs = sscs;
    }
}
