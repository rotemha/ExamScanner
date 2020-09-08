package com.example.examscanner.persistence.local.entities;

import com.example.examscanner.persistence.local.daos.DaoAbstractTest;
import com.example.examscanner.persistence.local.entities.relations.ExamineeSolutionWithExamineeAnswers;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class QuestionExamineeSolutionCrossResTest extends DaoAbstractTest {


    private static final String TAG = "QuestionExamineeSolutionCrossResTest";
    private static final int QAD_NUM_OF_QUESTIONS = 50;
    private static final String DONT_KNOW_EXAMINEE_ID = "DONT_KNOW_EXAMINEE_ID";

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testGettingExamineeAnswers() {
        int bmId =0;
        long creationSessionId = db.getExamCreationSessionDao().insert(new ExamCreationSession());
        long examId = db.getExamDao().insert(new Exam("COMP A",0,"2020", "url", 0,creationSessionId, null,QAD_NUM_OF_QUESTIONS, null, new String[0], 0,1, true));
        long sid = db.getScanExamSessionDao().insert(new ScanExamSession(examId));
        long verId = db.getVersionDao().insert(new Version(0, examId, null, false, true, true));
        long esId = db.getExamineeSolutionDao().insert(new ExamineeSolution(DONT_KNOW_EXAMINEE_ID ,sid, verId, null, true, true, true));
        long qId1 = db.getQuestionDao().insert(new Question(1,verId, 3,0,1,2,3, null,false, true));
        long qId2 = db.getQuestionDao().insert(new Question(2,verId, 4,0,1,2,3, null,false, true));
        db.getExamineeAnswerDao().insert(new ExamineeAnswer(qId1,esId, 3,0,0,0,0));
        db.getExamineeAnswerDao().insert(new ExamineeAnswer(qId2,esId, 3,0,0,0,0));
        List<ExamineeSolutionWithExamineeAnswers> eas = db.getExamineeSolutionDao().getExamineeSolutionWithExamineeAnswers();
        List<ExamineeAnswer> theAnswers = null;
        for (ExamineeSolutionWithExamineeAnswers curr:eas) {
            if(curr.getExamineeSolution().getId()==esId){
                theAnswers = curr.getExamineeAnswers();
            }
        }
        assertTrue(theAnswers.size()==2);
    }
}