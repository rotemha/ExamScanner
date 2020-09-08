package com.example.examscanner.authentication.state;

public class StateFactory {
    private static State instance;
    private static StateHolder stateHolder = new StateHolder() {
        @Override
        public void setState(State s) {
            StateFactory.setState(s);
        }
    };
    public static State get(){
        if (instance == null){
            instance = new FirebaseAnonymousState();
        }
        return instance;
    }
    static void setState(State s){
        instance = s;
    }

    public static StateHolder getStateHolder(){
        return stateHolder;
    }

    public static void tearDown() {
        instance = null;

    }
}
