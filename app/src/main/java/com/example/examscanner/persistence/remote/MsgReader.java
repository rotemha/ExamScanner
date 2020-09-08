package com.example.examscanner.persistence.remote;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.reactivestreams.Subscriber;

import io.reactivex.Observable;
import io.reactivex.Observer;

class MsgReader {

    public Observable<String> read(String theRef) {
        FirebaseDatabase database = FirebaseDatabaseFactory.get();
        DatabaseReference myRef = database.getReference(theRef);
        return new Observable<String>() {
            @Override
            protected void subscribeActual(Observer<? super String> observer) {
                myRef.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        observer.onNext(dataSnapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        observer.onError(new Exception(databaseError.getMessage()));
                    }
                });
            }
        };

    }
}
