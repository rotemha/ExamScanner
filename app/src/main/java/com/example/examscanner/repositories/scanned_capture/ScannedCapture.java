package com.example.examscanner.repositories.scanned_capture;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.Version;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;


public class ScannedCapture {
    private List<Answer> answers;
    private Bitmap bm;
    private boolean valid;
    private GetGradeAndApprove approvalCallback;

    public Bitmap getOrigBitmap() {
        return origBitmap;
    }

    private Bitmap origBitmap;
    private boolean isExamieeIdIsOccupiedByAnotherSolution;
    private int id;
    private Future<Version> v;
    private String examineeId;
    private boolean hasUpdatedFeedbackImage;
    private String graderEmail;


    public ScannedCapture(int id, Bitmap bm, Bitmap origBitmap, int numOfTotalAnswers, int numOfAnswersDetected, int[] answersIds, float[] lefts, float[] tops, float[] rights, float[] bottoms, int[] selections, Version v, String examineeId, String graderEmail) {
        this.bm=bm;
        this.origBitmap = origBitmap;
        this.answers = new ArrayList<>();
        for (int i = 0; i <numOfAnswersDetected ; i++) {
            if(selections[i] >0){
                answers.add(new ResolvedAnswer(answersIds[i], new PointF(lefts[i],tops[i]), new PointF(rights[i],bottoms[i]), selections[i]));
            }else{
                answers.add(new ConflictedAnswer(answersIds[i] , new PointF(lefts[i],tops[i]), new PointF(rights[i],bottoms[i])));
            }
        }

        for (int ansId = 1; ansId <= numOfTotalAnswers; ansId++) {
            if(!in(ansId,answersIds)){
                answers.add(new MissingAnswer(ansId));
            }
        }
        this.id = id;
        this.v =genResolvedFuture(v);
        this.examineeId = examineeId;
        valid = false;
        this.graderEmail = graderEmail;
    }



    public ScannedCapture(long id, Bitmap bitmap, List<Answer> answers, Future<Version> fV, String exaimneeId, boolean isExamieeIdIsOccupiedByAnotherSolution, boolean isValid, GetGradeAndApprove approvalCallback) {
        this.id = (int)id;
        this.bm = bitmap;
        this.answers = answers;
        v= fV;
        this.examineeId =exaimneeId;
        this.isExamieeIdIsOccupiedByAnotherSolution = isExamieeIdIsOccupiedByAnotherSolution;
        valid = isValid;
        this.approvalCallback = approvalCallback;
    }

    private boolean in(int item, int[] arr){
        for (int i = 0; i <arr.length ; i++) {
            if (arr[i]==item)return true;
        }
        return false;
    }

    public List<ConflictedAnswer> getConflictedAnswers(){
        List<ConflictedAnswer> ans = new ArrayList<>();
        for (Answer a: getAnswers()) {
            a.addMe(ans);
        }
        return ans;
    }

    private Answer getZeroBasedAnswer(int index){
        return getAnswers().get(index);
    }

    private Answer getNaturalBasedAnswer(int id) throws NoSuchAnswerException {
        for (Answer a:getAnswers()) {
            if (a.getAnsNum()==id) return a;
        }
        throw new NoSuchAnswerException("Asked for: "+ id+" but there are only "+getAnswers().size()+" answers");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int amountOf(Predicate<Answer> pred){
        int ans = 0;
        for (Answer a: getAnswers())
            if(pred.test(a))ans++;
        return ans;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int getCheckedAmount() {
        return amountOf(a -> a.isResolved());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int getConflictedAmount() {
        return amountOf(a -> a.isConflicted());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int getMissingAmount() {
        return amountOf(a -> a.isMissing());
    }
    public Bitmap getBm() {
        return bm;
    }

    public int getId() {
        return id;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void resolve(ConflictedAnswer ca, ResolvedConflictedAnswer resolve) {
        getAnswers().removeIf((answer -> answer.getAnsNum()==ca.getAnsNum()));
        getAnswers().add(resolve);
    }

    public void commitResolutions() {
        List<Answer> newAnswers = new ArrayList<>();
        for (Answer a:getAnswers()) {
//            if(a instanceof ResolvedConflictedAnswer)
//                ((ResolvedConflictedAnswer) a).updateBeenConflictedThenResolved();
            newAnswers.add(a.commitResolution());
        }
        setAnswers(newAnswers);
    }

    private void setAnswers(List<Answer> newAnswers) {
        answers=newAnswers;
    }

    public Answer getAnswerByNum(int i) {
        for (Answer a:getAnswers()) {
            if(a.getAnsNum()==i){
                return a;
            }
        }
        throw new NoAnswerWithTheGivenNum();
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public List<ResolvedAnswer> getResolvedAnswers() {
        List<ResolvedAnswer> ans = new ArrayList<>();
        for (Answer a:getAnswers()) {
            if(a.isResolved())
                ans.add((ResolvedAnswer)a);
        }
        return ans;
    }

    public Version getVersion() {
        try {
            return v.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new MyAssersionError("Version future should be accesible");
        }
    }

    public String getExamineeId() {
        return examineeId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAssocaitedWith(Exam exam) {
        boolean ans  = false;
        for (Version v:
             exam.getVersions()) {
            ans|=v.getId() == getVersion().getId();
        }
        return ans;
    }

    public boolean hasMoreConflictedAnswers() {
        for (ConflictedAnswer ca:getConflictedAnswers()) {
            if(! (ca instanceof ResolvedConflictedAnswer)){
                return true;
            }
        }
        return false;
    }

    public boolean hasUpdatedFeedbackImage() {
        if(hasUpdatedFeedbackImage){
            hasUpdatedFeedbackImage = false;
            return true;
        }
        return hasUpdatedFeedbackImage;
    }

    public float[] getRelLefts() {
        float[] ans = new float[getAnswers().size()];
        for (int i = 1; i <=getAnswers().size() ; i++) {
            ans[i-1] = getAnswerByNum(i).getLeft();
        }
        return ans;
    }

    public float[] getRelTops() {
        float[] ans = new float[getAnswers().size()];
        for (int i = 1; i <=getAnswers().size() ; i++) {
            ans[i-1] = getAnswerByNum(i).getUp();
        }
        return ans;
    }

    public int[] getSelections() {
        int[] ans = new int[getAnswers().size()];
        for (int i = 1; i <=getAnswers().size() ; i++) {
            ans[i-1] = getAnswerByNum(i).getSelection();
        }
        return ans;
    }
    public int[] getIds() {
        int[] ans = new int[getAnswers().size()];
        for (int i = 1; i <=getAnswers().size() ; i++) {
            ans[i-1] = i;
        }
        return ans;
    }

    public void setBitmap(Bitmap feedbackImage) {
        hasUpdatedFeedbackImage = true;
        bm= feedbackImage;
    }

    public boolean isExamineeIdIsOccupied() {
        return isExamieeIdIsOccupiedByAnotherSolution;
    }

    public float calcGrade() {
        float grade = 0;
        float questioneScore = (float)100 / (float)getVersion().getExam().getNumOfQuestions();
        for (Answer a:getAnswers()) {
            if(a.isCorrect(getVersion().getQuestionByNumber(a.getAnsNum()))){
                grade+=questioneScore;
            }
        }
        return grade;
    }

    public boolean isValid() {
        return valid;
    }

    public void approve() {
        if(!isValid()){
            throw new RuntimeException("Cannot approve in valid scanned capture");
        }
        approvalCallback.approve(calcGrade());
    }

    public void setApprovalCallback(GetGradeAndApprove approvalCallback) {
        this.approvalCallback = approvalCallback;
    }

    public void setValid() {
        valid =true;
    }

    public String getGraderEmail() {
        return this.graderEmail;
    }


    private class NoSuchAnswerException extends Exception{

        public NoSuchAnswerException(String message) {
            super(message);
        }


    }
    @NonNull
    @Override
    public String toString() {
        StringBuilder sBuilder = new StringBuilder();
        for (int i = 0; i < getAnswers().size(); i++) {
            try{
                Answer a = getAnswerByNum(i+1);
                sBuilder.append(a.toString());
                sBuilder.append(", ");
            }catch (NoAnswerWithTheGivenNum e){
                sBuilder.append(String.format("?, "));
            }
        }
        return sBuilder.toString();
    }
    private static Future<Version> genResolvedFuture(Version v) {
        return new Future<Version>() {
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
                return true;
            }

            @Override
            public Version get() throws ExecutionException, InterruptedException {
                return v;
            }

            @Override
            public Version get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
                return null;
            }
        };
    }

    private class MyAssersionError extends RuntimeException {
        public MyAssersionError(String msg) {
            super(msg);
        }
    }

    public interface ApprovalCallback{
        public void approve();
    }
    public interface GetGradeAndApprove {
        void approve(float grade);
    }
}
