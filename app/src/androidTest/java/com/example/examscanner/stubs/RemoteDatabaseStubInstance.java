package com.example.examscanner.stubs;

import com.example.examscanner.persistence.remote.RemoteDatabaseFacade;
import com.example.examscanner.persistence.remote.entities.Exam;
import com.example.examscanner.persistence.remote.entities.ExamineeSolution;
import com.example.examscanner.persistence.remote.entities.Grader;
import com.example.examscanner.persistence.remote.entities.Question;
import com.example.examscanner.persistence.remote.entities.Version;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class RemoteDatabaseStubInstance implements RemoteDatabaseFacade {

    @Override
    public Observable<String> createExam(String courseName, String url, String year, int term, int semester, String mangerId, String[] gradersIdentifiers, boolean seal, long sessionId, int numberOfQuestions, int uploaded, int numOfVe) {
        return Observable.fromCallable(()->{return "null";});
    }


    @Override
    public Observable<String> addVersion(int versionNumber , String examId, String bitmap ) {
        return Observable.fromCallable(()->{return "null";});
    }

    @Override
    public Observable<List<Grader>> getGraders() {
        return Observable.fromCallable(()->{return new ArrayList<Grader>();});
    }

    @Override
    public Observable<List<Exam>> getExams() {
        return Observable.fromCallable(()->{return new ArrayList<>();});
    }

    @Override
    public Observable<List<Version>> getVersions() {
        return Observable.fromCallable(()->{return new ArrayList<>();});
    }

    @Override
    public Observable<List<Question>> getQuestions() {
        return Observable.fromCallable(()->{return new ArrayList<>();});
    }

    @Override
    public Observable<String> createGrader(String email, String userId) {
        return Observable.fromCallable(()->{return "";});
    }

    @Override
    public Observable<String> createVersion(int num, String remoteExamId, String bitmapPath) {
        return Observable.fromCallable(()->{return "";});
    }

    @Override
    public Observable<String> createQuestion(String remoteVersionId, int num, int ans, int left, int up, int right, int bottom) {
        return Observable.fromCallable(()->{return "";});
    }

    @Override
    public Observable<List<ExamineeSolution>> getExamineeSolutions() {
        return Observable.fromCallable(()->{return new ArrayList<>();});
    }

    @Override
    public Observable<String> onlineInsertExamineeSolution(String examineeId, String versionId, boolean isValid) {
        return Observable.fromCallable(()->{return "";});
    }



    @Override
    public void offlineInsertAnswerIntoExamineeSolution(String examineeId, int questionNum, int ans) {

    }

    @Override
    public void offlineDeleteExamineeSolution(String solutionId, String examineeId, String remoteExamId) {

    }

    @Override
    public void offlineInsertGradeIntoExamineeSolution(String examineeId, float grade) {

    }



    @Override
    public Observable<String> offlineInsertExamineeSolutionTransaction(String examineeId, String versionId, int[][] answers, float grade, String bitmapUrl,String origBitmapUrl, boolean isValid) {
        return Observable.fromCallable(()->{return "";});
    }

    @Override
    public void offlineUpdateAnswerIntoExamineeSolution(String examineeId, int questionNum, int ans) {

    }

    @Override
    public void updateUploaded(String remoteId) {

    }

    @Override
    public Observable<String> observeExamineeIds(String remoteId) {
        return Observable.fromCallable(()->{return "adfgjkndsfgljksndfgjndsfgkjsdnfg";});
    }

    @Override
    public Observable<String> insertExamineeIDOrReturnNull(String remoteId, String examineeId) {
        return Observable.fromCallable(()->"COOLNESSNESS");
    }

    @Override
    public void offlineUpdateExamineeGrade(String remoteId,String remotr, float grade) {

    }

    @Override
    public void validateSolution(String remoteId) {

    }

    @Override
    public void deleteExam(String examId) {

    }

    @Override
    public void setSolutionBitmapUrl(String url, String remoteId) {

    }

    @Override
    public void setOriginialBitmapUrl(String url, String remoteId) {

    }

    @Override
    public void offilneInsertExamineeSolutionGrade(String remoteId, float grade) {

    }

    @Override
    public void insertReserevedExamineeId(String remoteId, String reservedExamineeId) {

    }

    @Override
    public void deleteVersion(String remoteVersionId) {

    }

    @Override
    public void deleteQuestion(String remoteId) {

    }

    @Override
    public void offilneInsertExamineeSolutionGrader(String remoteId, String graderEmail) {

    }

    @Override
    public void updateTotalAndAverageTransaction(String examRemoteId, Float grade) {

    }


    @Override
    public void addGraderIfAbsent(String email, String uId) {

    }

    @Override
    public Observable<List<Exam>> getExamsOfGrader(String userId) {
        return null;
    }
}
