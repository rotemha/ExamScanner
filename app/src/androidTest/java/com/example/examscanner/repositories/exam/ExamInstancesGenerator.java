package com.example.examscanner.repositories.exam;

import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;

import java.util.ArrayList;

public class ExamInstancesGenerator {

    private static final int QAD_NUM_OF_QUESTIONS = 50;

    public static Exam getInstance1() {
        Exam e = new Exam(
                "FAKE_MANAGER",
                -1,
                Exam.theEmptyFutureVersionsList(),
                new ArrayList<>(),
                "Instance1_courseName",
                0,
                0,
                0,
                "1984",
                QAD_NUM_OF_QUESTIONS,
                0,
                ExamineeIdsSocket.getEmpty(),
                true,
                Exam.DownloadCompletable.getEmpty(),
                Exam.DownloadCompletable.getEmpty()
        );
        final Version v = new Version(
                -1,
                1,
                Version.toFuture(e),
                Version.theEmptyFutureQuestionsList(),
                BitmapsInstancesFactoryAndroidTest.getComp191_v1_JPG_ANS_2()
        );
        e.addVersion(
                v
        );
        Question q = new Question(
                -1,
                Question.toFuture(v),
                1,
                4,
                1,
                1,
                1,
                1
        );
        v.addQuestion(q);
        return e;
    }
}
