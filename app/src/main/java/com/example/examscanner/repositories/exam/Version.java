package com.example.examscanner.repositories.exam;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Version {
    private static final String TAG = "ExamScanner";
    private static final String MSG_PREF = "Version";
    private Future<Exam> exam;
    private long id;
    private int num;
    private Future<List<Question>> fQuestions;
    private List<Question> newQuestions;
    private Bitmap perfectImage;
//    private boolean doResolveFutures;


    public Version(long id,int num, Future<Exam> e, Future<List<Question>> fQuestions, Bitmap perfectImage) {
        this.id=id;
        this.num = num;
        this.exam = e;
        newQuestions = new ArrayList<>();
        this.fQuestions = fQuestions;
        this.perfectImage = perfectImage;
//        doResolveFutures = true;
    }

    public Question getQuestionByNumber(int i){
        for(Question q : getQuestions()){
            if(q.getNum() == i)
                return q;
        }
        throw new NoSuchQuestion();
    }

    public int getNum() {
        return num;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public Exam getExam() {
        try {
            return exam.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new MyAsserstionError("Exam future should be accessible");
        }
    }

    public void addQuestion(Question question) {
        newQuestions.add(question);
    }

    public List<Question> getQuestions() {
        List<Question> ans = new ArrayList<>();
        ans.addAll(newQuestions);
        ans.addAll(accessFQuestions());
        return ans;
    }

    private List<Question> accessFQuestions() {
//        if(!doResolveFutures){
//            return new ArrayList<>();
//        }
        try {
            return fQuestions.get();
        } catch (ExecutionException e) {
            Log.d(TAG,MSG_PREF+ " accessFQuestions");
            e.printStackTrace();
            throw new RuntimeException("Problem with future");
        } catch (InterruptedException e) {
            Log.d(TAG,MSG_PREF+ " accessFQuestions");
            e.printStackTrace();
            throw new RuntimeException("Problem with future");
        }
    }

    public static Future<List<Question>> theEmptyFutureQuestionsList(){
        return new Future<List<Question>>() {
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
            public List<Question> get() throws ExecutionException, InterruptedException {
                return new ArrayList<>();
            }

            @Override
            public List<Question> get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
                return null;
            }
        };
    }

    public static Future<List<Question>> theErrorFutureQuestionsList() {
        return new Future<List<Question>>() {
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
            public List<Question> get() throws ExecutionException, InterruptedException {
                throw new RuntimeException("Bug in exam scanner. Probably Questions future was not set");
            }

            @Override
            public List<Question> get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
                return null;
            }
        };
    }

    public void setQuestionsFuture(Future<List<Question>> questionsFuture) {
        this.fQuestions = questionsFuture;
    }

    public Bitmap getPerfectImage() {
        return perfectImage;
    }

    public float[] getRealtiveLefts() {
        float[] ans = new float[getQuestions().size()];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = scale(getQuestionByNumber(i+1).getLeft(), getPerfectImage().getWidth());
        }
        return ans;
    }

    public float[] getRealtiveUps() {
        float[] ans = new float[getQuestions().size()];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = scale(getQuestionByNumber(i+1).getUp(), getPerfectImage().getHeight());
        }
        return ans;
    }

    private float scale(int orig, int dim) {
        float fOrig = orig;
        float fDim = dim;
        return fOrig/fDim;
    }



    public class NoSuchQuestion extends RuntimeException {}

    @Override
    public boolean equals(@Nullable Object obj) {
        if(!(obj instanceof Version)){
            return false;
        }
        Version other = (Version) obj;
        boolean ans = true;
        ans&= getNum()==other.getNum();
        List<Question> otherQuestions = other.getQuestions();
        ans&= otherQuestions.size()==getQuestions().size();
        for (Question q:
                getQuestions()) {
            ans&= otherQuestions.contains(q);
        }
        return ans;
    }

    public void quziEagerLoad() {
        for(Question question: getQuestions()){}
    }


    private class MyAsserstionError extends RuntimeException {

        public MyAsserstionError(String msg) {
            super(msg);
        }
    }
    public static Future<Exam> toFuture(Exam examCreated) {
        return new Future<Exam>() {
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
            public Exam get() throws ExecutionException, InterruptedException {
                return examCreated;
            }

            @Override
            public Exam get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
                return null;
            }
        };
    }
}
