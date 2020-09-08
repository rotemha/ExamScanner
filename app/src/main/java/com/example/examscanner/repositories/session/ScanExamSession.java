package com.example.examscanner.repositories.session;

public class ScanExamSession extends Session{
    private long examId;

    public ScanExamSession(long id, long examId) {
        super(id);
        this.examId = examId;
    }

    public long examId() {
        return examId;
    }
}
