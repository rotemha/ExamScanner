package com.example.examscanner.authentication.state;

public interface State<T> {
    public void login(StateHolder holder, T stateContent);
    public void logout(StateHolder holder);
    public boolean isAuthenticated();
    public String getId();
    T getContent();

    String getUserEmail();

}
