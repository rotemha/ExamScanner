package com.example.examscanner.persistence.remote;

import com.example.examscanner.persistence.remote.entities.Exam;
import com.example.examscanner.persistence.remote.entities.ExamineeSolution;
import com.example.examscanner.persistence.remote.entities.Grader;
import com.example.examscanner.persistence.remote.entities.Question;
import com.example.examscanner.persistence.remote.entities.Version;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface RemoteDatabaseFacade {
    public Observable<String> createExam(String courseName, String url, String year, int term, int semester, String mangerId, String[] gradersIdentifiers, boolean seal , long sessionId, int numberOfQuestions, int uploaded, int numOfVersions);
    public Observable<String> addVersion(int num, String remoteExamId, String bitmapPath);
    public Observable<List<Grader>> getGraders();
    public Observable<List<Exam>> getExams();
    public Observable<List<Version>> getVersions();
    public Observable<List<Question>> getQuestions();
    public Observable<String> createGrader(String email, String userId);
    public Observable<String> createVersion(int num, String remoteExamId, String bitmapPath);
    public Observable<String> createQuestion(String remoteVersionId, int num, int ans, int left, int up, int right, int bottom);
    public Observable<List<ExamineeSolution>> getExamineeSolutions();
    public Observable<String> onlineInsertExamineeSolution(String examineeId, String versionId, boolean isValid);
    public void offlineInsertAnswerIntoExamineeSolution(String examineeId, int questionNum, int ans);
    public void offlineDeleteExamineeSolution(String solutionId, String examineeId, String remoteExamId);
    public void offlineInsertGradeIntoExamineeSolution(String examineeId, float grade);
    public Observable<String> offlineInsertExamineeSolutionTransaction(String examineeId, String versionId, int[][] answers, float grade, String bitmapUrl, String origBitmapUrl,boolean isValid);
    public void addGraderIfAbsent(String email, String uId) ;
    public Observable<List<Exam>> getExamsOfGrader(String userId);
    public void offlineUpdateAnswerIntoExamineeSolution(String examineeId, int questionNum, int ans);
    public void updateUploaded(String remoteId);
    Observable<String> observeExamineeIds(String remoteId);
    Observable<String> insertExamineeIDOrReturnNull(String remoteId, String examineeId);
    void offlineUpdateExamineeGrade(String remoteId, String examRemoteId, float grade);
    public void validateSolution(String remoteId);
    public void deleteExam(String examId);
    public void setSolutionBitmapUrl(String url, String remoteId);
    public void setOriginialBitmapUrl(String url, String remoteId);
    void offilneInsertExamineeSolutionGrade(String remoteId, float grade);
    void insertReserevedExamineeId(String remoteId, String reservedExamineeId);
    void deleteVersion(String remoteVersionId);
    void deleteQuestion(String remoteId);

    void offilneInsertExamineeSolutionGrader(String remoteId, String graderEmail);
    void updateTotalAndAverageTransaction(String examRemoteId, Float grade);
}
