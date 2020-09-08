package com.example.examscanner.persistence.remote;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.internal.operators.completable.CompletableDefer;

public class MsgSender {
    public Completable send(String toRef, String msg) {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabaseFactory.get();
        database.setPersistenceEnabled(true);
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(toRef);
        return Completable.fromCallable(() -> {
            final Task<Void> task = myRef.setValue(msg);
            Tasks.await(task);
            if(task.isSuccessful()) {
                return Completable.complete();
            } else {
                return Completable.error(new IllegalStateException("Task not successful", task.getException()));
            }
        });
    }
}
