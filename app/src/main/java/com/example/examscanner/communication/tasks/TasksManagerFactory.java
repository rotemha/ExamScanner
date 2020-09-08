package com.example.examscanner.communication.tasks;

public class TasksManagerFactory {
    private static TasksManager instance;
    public static TasksManager get(){
        if(instance == null){
            instance = new TaskManagerImpl();
        }
        return instance;
    }
}
