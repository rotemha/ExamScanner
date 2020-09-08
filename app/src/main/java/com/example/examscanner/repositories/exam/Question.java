package com.example.examscanner.repositories.exam;

import androidx.annotation.Nullable;

import com.example.examscanner.repositories.exam.Version;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Question {
    private long id;
    private Future<Version> version;
    private int num;
    private int ans;
    private int left;
    private int up;
    private int bottom;
    private int right;

    public Question(long qId, Future<Version> v, int ansNum, int selection, int left, int up, int right, int bottom) {
        id = qId;
        this.version = v;
        this.num = ansNum;
        this.ans = selection;
        this.left=left;
        this.up = up;
        this.right = right;
        this.bottom = bottom;
    }

    public int getLeft() {
        return left;
    }

    public int getUp() {
        return up;
    }

    public int getBottom() {
        return bottom;
    }

    public int getRight() {
        return right;
    }

    public Question(long versionId, int ansNum, int selection, int left, int up, int right, int bottom) {
        this.num = ansNum;
        this.ans = selection;
        this.left=left;
        this.up = up;
        this.right = right;
        this.bottom = bottom;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getVersionId() {
        return getVersion().getId();
    }

    private Version getVersion() {
        try {
            return version.get();
        } catch (ExecutionException |InterruptedException  e) {
            e.printStackTrace();
            throw new MyAssersionError("Future version shouls be accesible");
        }
    }

    public void setVersionId(long versionId) {

    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getAns() {
        return ans;
    }

    public void setAns(int ans) {
        this.ans = ans;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(!(obj instanceof  Question)){
            return false;
        }
        Question other = (Question)obj;
        boolean ans =true;
        ans&= getNum()==other.getNum();
        ans&= getAns()==other.getAns();
        ans&= getUp() == other.getUp();
        ans&= getBottom() == other.getBottom();
        ans&= getRight() == other.getRight();
        return ans;
    }


    private class MyAssersionError extends RuntimeException {

        public MyAssersionError(String msg) {
            super(msg);
        }
    }
    public static Future<Version> toFuture(Version v) {
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
}
