package com.example.examscanner.communication.tasks;

import io.reactivex.Completable;

public interface TasksManager {
    public void post(Completable comp, String id, String dec);
    public Task get(String id);
}
