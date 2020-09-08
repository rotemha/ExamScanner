package com.example.examscanner.communication.tasks;

import java.util.HashMap;

import io.reactivex.Completable;
import io.reactivex.Observable;

class TaskManagerImpl implements TasksManager {

    private HashMap<String,Task> map = new HashMap<>();
    @Override
    public void post(Completable comp, String id, String desc) {
        map.put(id, new Task(comp,desc));
    }

    @Override
    public Task get(String id) {
        if(map.containsKey(id)){
            return map.get(id);
        }else{
            return new VeporizedTask(Completable.fromObservable(Observable.empty()), "Veporized");
        }
    }
}
