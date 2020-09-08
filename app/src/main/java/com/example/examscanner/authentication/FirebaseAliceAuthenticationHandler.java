package com.example.examscanner.authentication;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.Observable;
import io.reactivex.Observer;

class FirebaseAliceAuthenticationHandler implements AuthenticationHandler {
    private FirebaseAuth mAuth;


    @Override
    public Observable<FirebaseAuth> authenticate() {
        mAuth = FirebaseAuth.getInstance();
        return new Observable<FirebaseAuth>() {
            @Override
            protected void subscribeActual(Observer<? super FirebaseAuth> observer) {
                mAuth.signInWithEmailAndPassword("aliceexamscanner80@gmail.com", "Ycombinator")
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    observer.onNext(mAuth);
                                    observer.onComplete();
                                } else {
                                    observer.onError(task.getException());
                                }
                            }
                        });
            }
        };
    }

    @Override
    public Observable<String> authenticateAndReturnId() {
        mAuth = FirebaseAuth.getInstance();
        return new Observable<String>() {
            @Override
            protected void subscribeActual(Observer<? super String> observer) {
                mAuth.signInWithEmailAndPassword("aliceexamscanner80@gmail.com", "Ycombinator")
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    observer.onNext(mAuth.getUid());
                                    observer.onComplete();
                                } else {
                                    observer.onError(task.getException());
                                }
                            }
                        });
            }
        };
    }

    @Override
    public Observable<FirebaseAuth> authenticate(String userEmail, String password) {
        mAuth = FirebaseAuth.getInstance();
        return new Observable<FirebaseAuth>() {
            @Override
            protected void subscribeActual(Observer<? super FirebaseAuth> observer) {
                mAuth.signInWithEmailAndPassword(userEmail, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    observer.onNext(mAuth);
                                    observer.onComplete();
                                } else {
                                    observer.onError(task.getException());
                                }
                            }
                        });
            }
        };
    }

    @Override
    public Observable<Object> signIn(String username, String password) {
        mAuth = FirebaseAuth.getInstance();
        return new Observable<Object>() {
            @Override
            protected void subscribeActual(Observer<? super Object> observer) {
                mAuth.createUserWithEmailAndPassword("aliceexamscanner80@gmail.com", "Ycombinator")
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    observer.onNext(mAuth);
                                    observer.onComplete();
                                } else {
                                    observer.onError(task.getException());
                                }
                            }
                        });
            }
        };
    }


}
