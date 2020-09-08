package com.example.examscanner.authentication.state;

public interface StateHolder {
    static StateHolder getDefaultHolder() {
        return new StateHolder() {
            @Override
            public void setState(State s) {

            }
        };
    }

    public void setState(State s);
}
