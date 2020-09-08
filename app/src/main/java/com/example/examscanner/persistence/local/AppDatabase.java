package com.example.examscanner.persistence.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.examscanner.persistence.local.daos.ExamCreationSessionDao;
import com.example.examscanner.persistence.local.daos.ExamDao;
import com.example.examscanner.persistence.local.daos.ExamineeAnswerDao;
import com.example.examscanner.persistence.local.daos.ExamineeSolutionDao;
import com.example.examscanner.persistence.local.daos.QuestionDao;
import com.example.examscanner.persistence.local.daos.SemiScannedCaptureDao;
import com.example.examscanner.persistence.local.daos.ScanExammSessionDao;
import com.example.examscanner.persistence.local.daos.VersionDao;
import com.example.examscanner.persistence.local.entities.Exam;
import com.example.examscanner.persistence.local.entities.ExamCreationSession;
import com.example.examscanner.persistence.local.entities.ScanExamSession;
import com.example.examscanner.persistence.local.entities.ExamineeAnswer;
import com.example.examscanner.persistence.local.entities.ExamineeSolution;
import com.example.examscanner.persistence.local.entities.Question;
import com.example.examscanner.persistence.local.entities.SemiScannedCapture;
import com.example.examscanner.persistence.local.entities.Version;

@Database(
        entities = {
                Exam.class,
                Version.class,
                Question.class,
                ExamineeSolution.class,
                ExamineeAnswer.class,
                ScanExamSession.class,
                SemiScannedCapture.class,
                ExamCreationSession.class
        },
        version = 6
)


@TypeConverters(GenreConverter.class)

public abstract class AppDatabase extends RoomDatabase {
    public abstract ExamDao getExamDao();
    public abstract ScanExammSessionDao getScanExamSessionDao();
    public abstract SemiScannedCaptureDao getSemiScannedCaptureDao();
    public abstract ExamineeSolutionDao getExamineeSolutionDao();
    public abstract VersionDao getVersionDao();
    public abstract QuestionDao getQuestionDao();

    public abstract ExamineeAnswerDao getExamineeAnswerDao();

    public abstract ExamCreationSessionDao getExamCreationSessionDao();
}
