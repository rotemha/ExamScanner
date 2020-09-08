package com.example.examscanner.communication.tasks;

class TaskException extends RuntimeException {
    public TaskException(Throwable throwable) {
        super(throwable);
    }
}
