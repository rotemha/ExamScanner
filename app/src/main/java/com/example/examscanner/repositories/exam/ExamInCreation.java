package com.example.examscanner.repositories.exam;

import com.example.examscanner.repositories.grader.Grader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ExamInCreation extends Exam {

    public ExamInCreation(long sessionId) {
        super(
                null,
                -1,
                new Future<List<Version>>() {
                    @Override
                    public boolean cancel(boolean mayInterruptIfRunning) {
                        return false;
                    }

                    @Override
                    public boolean isCancelled() {
                        return false;
                    }

                    @Override
                    public boolean isDone() {
                        return false;
                    }

                    @Override
                    public List<Version> get() throws ExecutionException, InterruptedException {
                        return new ArrayList<>();
                    }
                    @Override
                    public List<Version> get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {return null;}
                },
                new ArrayList<>(),
                "THE_EMPTY_COURSE_NAME",
                -1,
                -1,
                sessionId,
                "THE_EMPTY_YEAR",
                -1,
                0,
                ExamineeIdsSocket.getEmpty(),
                true,
                DownloadCompletable.getEmpty(),
                DownloadCompletable.getEmpty()
        );
    }

    public Exam commit(String userId, String courseName, int term, int semester, List<Grader>  graders, String year, String examUrl, int numberOfQuestions, int uploaded) {
        this.managerId = userId;
        this.courseName = courseName;
        this.term = term;
        this.semester = semester;
        this.year=year;
        this.graders = graders;
        this.url = examUrl;
        this.numOfQuestions = numberOfQuestions;
        this.uploaded = uploaded;
        return this;
    }
}
