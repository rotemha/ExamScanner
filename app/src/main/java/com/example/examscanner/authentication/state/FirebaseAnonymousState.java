package com.example.examscanner.authentication.state;

import com.google.firebase.auth.FirebaseAuth;

class FirebaseAnonymousState implements State<FirebaseAuth> {
    @Override
    public void login(StateHolder holder, FirebaseAuth stateContent) {
        State theNewState = new FirebaseState(stateContent);
        holder.setState(theNewState);
        StateFactory.setState(theNewState);
    }

    @Override
    public void logout(StateHolder holder) {
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public FirebaseAuth getContent() {
        return null;
    }

    @Override
    public String getUserEmail() {
        return null;
    }


}
