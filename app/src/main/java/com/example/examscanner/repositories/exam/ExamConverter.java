package com.example.examscanner.repositories.exam;

import com.example.examscanner.communication.CommunicationFacade;
import com.example.examscanner.communication.entities_interfaces.ExamEntityInterface;
import com.example.examscanner.communication.entities_interfaces.QuestionEntityInterface;
import com.example.examscanner.communication.entities_interfaces.VersionEntityInterface;
import com.example.examscanner.repositories.Converter;
import com.example.examscanner.repositories.grader.Grader;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ExamConverter implements Converter<ExamEntityInterface, Exam> {
    private CommunicationFacade communicationFacade;

    public ExamConverter(CommunicationFacade communicationFacade) {
        this.communicationFacade = communicationFacade;
    }

    @Override
    public Exam convert(final ExamEntityInterface examEntityInterface) {
        ArrayList<Grader> graders = new ArrayList<>();
        for(String gId : examEntityInterface.getGradersIds())
            // TODO: right now there is no access to the user email in here, replace null
            graders.add(new Grader(null, gId));
        return new Exam(
                examEntityInterface.getManagerId(),
                examEntityInterface.getID(),
                toFutureVersions(examEntityInterface),
//                new ArrayList<>(),
                graders,
                examEntityInterface.getCourseName(),
                examEntityInterface.getTerm(),
                examEntityInterface.getSemester(),
                examEntityInterface.getSessionId(),
                examEntityInterface.getYear(),
                examEntityInterface.getNumOfQuestions(),
                examEntityInterface.getUploaded(),
                ()->communicationFacade.observeExamineeIds(examEntityInterface.getID()),
                examEntityInterface.getDownloaded(),
                ()->communicationFacade.observeExamDownload(examEntityInterface.getID()),
                ()->communicationFacade.observeExamUpladed(examEntityInterface.getID())
        );
    }

    @NotNull
    public VersionsFuture toFutureVersions(ExamEntityInterface examEntityInterface) {
        return new VersionsFuture(examEntityInterface.getID(), examEntityInterface.getVersionsIds());
    }

    @NotNull
    public VersionFuture toFutureVersion(long examId, long longVersionId) {
        return new VersionFuture(longVersionId);
    }

    public Future<Exam> toExamFuture(long examId) {
        return new ExamFuture(examId);
    }

    public Future<List<Question>> toFutureQuestions(long versionId) {
        return new QuestionsFuture(versionId);
    }

    public class VersionsFuture implements Future<List<Version>> {
        private final long examId;
        private final long[] vIds;

        public VersionsFuture(long examId, long[] vIds) {
            this.examId = examId;
            this.vIds = vIds;
        }

        private List<Version> lazy = null;

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
            return lazy != null;
        }

        @Override
        public List<Version> get() throws ExecutionException, InterruptedException {
            if (!isDone()) {
                lazy = create();
            }
            return lazy;
        }

        private List<Version> create() {
            lazy = new ArrayList<>();
            for (long vId : vIds) {
                VersionEntityInterface vei = communicationFacade.getVersionById(vId);
                Version v = new Version(vei.getId(), vei.getNumber(), new ExamFuture(examId), Version.theErrorFutureQuestionsList(), vei.getBitmap());
                v.setQuestionsFuture(new QuestionsFuture(v.getId()));
                lazy.add(v);
            }
            return lazy;
        }

        @Override
        public List<Version> get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
            return null;
        }
    }

    public class ExamFuture implements Future<Exam> {
        private long examId;
        private Exam lazy;

        public ExamFuture(long examId) {
            this.examId = examId;
            lazy = null;
        }

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
            return lazy != null;
        }

        @Override
        public Exam get() throws ExecutionException, InterruptedException {
            if (!isDone()) {
                lazy = ExamConverter.this.convert(communicationFacade.getExamById(examId));
            }
            return lazy;
        }

        @Override
        public Exam get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
            return null;
        }
    }

    public class QuestionsFuture implements Future<List<Question>> {
        private final long versionId;
        private List<Question> lazy;

        public QuestionsFuture(long versionId) {
            this.versionId = versionId;
        }

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
            return lazy != null;
        }

        @Override
        public List<Question> get() throws ExecutionException, InterruptedException {
            if (!isDone()) {
                lazy = create();
            }
            return lazy;
        }

        private List<Question> create() {
            lazy = new ArrayList<>();
            for (long qId : communicationFacade.getQuestionsByVersionId(versionId)) {
                QuestionEntityInterface qei = communicationFacade.getQuestionById(qId);
                lazy.add(new Question(qId, new VersionFuture(versionId), qei.getNum(), (int) qei.getCorrectAnswer(), qei.getLeftX(), qei.getUpY(), qei.getRightX(), qei.getBottomY()));
            }
            return lazy;
        }

        @Override
        public List<Question> get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
            return null;
        }
    }

    private class VersionFuture implements Future<Version> {
        private final long longVersionId;
        private Version lazy;

        public VersionFuture(long longVersionId) {
            this.longVersionId = longVersionId;
            lazy = null;
        }

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
            return lazy != null;
        }

        @Override
        public Version get() throws ExecutionException, InterruptedException {
            if (!isDone()) {
                VersionEntityInterface vei = communicationFacade.getVersionById(longVersionId);
                lazy = new Version(longVersionId, vei.getNumber(), new ExamFuture(vei.getExamId()), new QuestionsFuture(longVersionId), vei.getBitmap());
            }
            return lazy;
        }

        @Override
        public Version get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
            return null;
        }
    }
}
