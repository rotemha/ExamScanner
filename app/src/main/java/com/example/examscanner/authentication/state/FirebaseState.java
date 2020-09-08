package com.example.examscanner.authentication.state;

import com.google.firebase.auth.FirebaseAuth;

class FirebaseState implements State<FirebaseAuth> {
    private FirebaseAuth stateContent;

    public FirebaseState(FirebaseAuth stateContent) {
        this.stateContent = stateContent;
    }

    @Override
    public void login(StateHolder holder, FirebaseAuth stateContent) {}

    @Override
    public void logout(StateHolder holder) {
        stateContent.signOut();
        final FirebaseAnonymousState s = new FirebaseAnonymousState();
        holder.setState(s);
        StateFactory.setState(s);
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public String getId() {
        return stateContent.getUid();
    }

    @Override
    public FirebaseAuth getContent() {
        return null;
    }

    @Override
    public String getUserEmail() {
        return stateContent.getCurrentUser().getEmail();
    }

}
