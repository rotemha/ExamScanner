package com.example.examscanner.communication;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.examscanner.communication.entities_interfaces.ExamEntityInterface;
import com.example.examscanner.communication.entities_interfaces.ExamineeAnswerEntityInterface;
import com.example.examscanner.communication.entities_interfaces.ExamineeSolutionsEntityInterface;
import com.example.examscanner.communication.entities_interfaces.GraderEntityInterface;
import com.example.examscanner.communication.entities_interfaces.QuestionEntityInterface;
import com.example.examscanner.communication.entities_interfaces.ScanExamSessionEntityInterface;
import com.example.examscanner.communication.entities_interfaces.SemiScannedCaptureEntityInterface;
import com.example.examscanner.communication.entities_interfaces.VersionEntityInterface;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class FacadeImplProxy implements CommunicationFacade {
    private RealFacadeImple realImpl  = RealFacadeImple.getInstance();

    @Override
    public long createExam(String courseName, String url, String year, int term, int semester,String managerId, String[] graders, long sessionId,int numberOfQuestions, int uploaded, int numOFVer) {
        return realImpl.createExam(courseName, url, year, term, semester, managerId, graders,sessionId,numberOfQuestions, uploaded,numOFVer);
    }

    @Override
    public long createSemiScannedCapture(int leftMostX, int upperMostY, int rightMostX, int rightMostY, long sessionId, Bitmap bm) {
        return realImpl.createSemiScannedCapture(leftMostX, upperMostY, rightMostX, rightMostY,  sessionId, bm);
    }



    @Override
    public long createNewScanExamSession(long examId) {
        return realImpl.createNewScanExamSession(examId);
    }

    @Override
    public SemiScannedCaptureEntityInterface getSemiScannedCapture(long id) {
        return realImpl.getSemiScannedCapture(id);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public long[] getSemiScannedCaptureBySession(long sId) {
        return realImpl.getSemiScannedCaptureBySession(sId);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public long[] getExamineeSolutionsBySession(long sId) {
        return realImpl.getExamineeSolutionsBySession(sId);
    }

    @Override
    public long addExamineeAnswer(long solutionId, long questionId, int ans, int leftX, int upY, int rightX, int botY) {
        return realImpl.addExamineeAnswer(solutionId, questionId, ans, leftX,  upY,  rightX,  botY);
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public long[] getExamineeAnswersIdsByExamineeSolutionId(long esId) {
        return realImpl.getExamineeAnswersIdsByExamineeSolutionId(esId);
    }

    @Override
    public void uploadExamineesSolutions(long[] ids) {

    }

    @Override
    public long getExamIdByScanExamSession(long sId) {
        return realImpl.getExamIdByScanExamSession(sId);
    }

    @Override
    public ExamEntityInterface getExamById(long id) {
        return realImpl.getExamById(id);
    }

    @Override
    public QuestionEntityInterface getQuestionByExamIdVerNumAndQNum(long examId, int verNum, int qNum) {
        return realImpl.getQuestionByExamIdVerNumAndQNum(examId, verNum, qNum);
    }

    @Override
    public ExamineeAnswerEntityInterface getExamineeAnswerByExamIdVerNumAndQNumAndExamineeId(long examId, int verNum, int qNum, long examineeId) {
        return null;
    }

    @Override
    public ExamEntityInterface[] getExams() {
        return realImpl.getExams();
    }

    @Override
    public void updateExam(long id, String courseName, int semester, int term, long[] versions, long sessionId, String year) {
        realImpl.updateExam(id, courseName, semester, term, versions, sessionId, year);
    }

    @Override
    public long createNewCreateExamSession() {
        return realImpl.createNewCreateExamSession();
    }

    @Override
    public ScanExamSessionEntityInterface[] getScanExamSessions() {
        return realImpl.getScanExamSessions();
    }

    @Override
    public long createQuestion(long versionId, int num, int ans, int left, int up, int right, int bottom) {
        return realImpl.createQuestion(versionId, num, ans, left, up, right, bottom);
    }

    @Override
    public long createVersion(long examId, int num, Bitmap verBm) {
        return realImpl.createVersion(examId, num, verBm);
    }

    @Override
    public VersionEntityInterface getVersionById(long vId) {
        return realImpl.getVersionById(vId);
    }

    @Override
    public QuestionEntityInterface getQuestionById(long qId) {
        return realImpl.getQuestionById(qId);
    }

    @Override
    public long insertVersionReplaceOnConflict(long examId, int num, Bitmap perfectImage) {
        return realImpl.insertVersionReplaceOnConflict(examId, num, perfectImage);
    }

    @Override
    public long insertQuestionReplaceOnConflict(long vId, int qNum, int qAns, int left, int right, int up, int bottom) {
        return realImpl.insertQuestionReplaceOnConflict(vId, qNum, qAns, left, right, up, bottom);
    }

    @Override
    public List<GraderEntityInterface> getGraders() {
        return realImpl.getGraders();
    }

    @Override
    public void createGrader(String userName, String userId) {
        realImpl.createGrader(userName,userId);
    }

    @Override
    public ExamineeAnswerEntityInterface getAnswerById(long examineeAnswersId) {
        return realImpl.getAnswerById(examineeAnswersId);
    }

    @Override
    public ExamineeSolutionsEntityInterface[] getExamineeSoultions() {
        return realImpl.getExamineeSoultions();
    }

    @Override
    public void addExamineeGrade(long solutionId, float grade) {
        realImpl.addExamineeGrade(solutionId, grade);
    }
    public void removeExamineeSolutionFromCache(long id) {
        realImpl.removeExamineeSolutionFromCache(id);
    }

    @Override
    public void deleteExamineeSolution(long id) {
        realImpl.deleteExamineeSolution(id);
    }

    @Override
    public void updateUploaded(long id) {
        realImpl.updateUploaded(id);
    }

    @Override
    public Observable<String> observeExamineeIds(long id) {
        return realImpl.observeExamineeIds(id);
    }

    @Override
    public void updateExamineeGrade(long id, float grade) {
        realImpl.updateExamineeGrade(id,grade);
    }

    @Override
    public void validateSolution(long id) {
        realImpl.validateSolution(id);
    }

    @Override
    public void approveSolution(long id) {
        realImpl.approveSolution(id);
    }

    @Override
    public void deleteExam(long id) {
        realImpl.deleteExam(id);
        }
          @Override
    public void addGraderToSolution(long id, String graderEmail) {
        realImpl.addGraderToSolution(id, graderEmail);
    }

    @Override
    public void approveSolutionAndStats(int id, float calcGrade) {
        realImpl.approveSolutionAndStats(id, calcGrade);
    }

    @Override
    public Completable observeExamDownload(long id) {
        return realImpl.observeExamDownload(id);
    }

    @Override
    public Completable observeExamUpladed(long id) {
        return realImpl.observeExamUpladed(id);
    }

    @Override
    public void updateExamineeAnswer(long solutionId, long questionId, int ans, int leftX, int upY, int rightX, int botY) {
        realImpl.updateExamineeAnswer(solutionId, questionId, ans, leftX, upY, rightX, botY);
    }

    @Override
    public VersionEntityInterface getVersionByExamIdAndNumber(long eId, int num) {
        return realImpl.getVersionByExamIdAndNumber(eId, num);
    }

    @Override
    public long addVersion(long examId, int versionNumber, Bitmap bm) {
        return realImpl.addVersion(examId,versionNumber, bm);
    }


    @Override
    public long addQuestion(long vId, int qNum, int correctAnswer, int leftX, int upY, int rightX, int bottomY) {
        return realImpl.addQuestion(vId, qNum, correctAnswer, leftX, upY, rightX, bottomY);
    }


    @Override
    public long createExamineeSolution(long sId, Bitmap bm, String examineeId, long versionId, Bitmap origBm) {
        return realImpl.createExamineeSolution(sId, bm, examineeId, versionId, origBm);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public long[] getQuestionsByVersionId(long vId) {
        return realImpl.getQuestionsByVersionId(vId);
    }

    private boolean useReal(){return false;}


}
