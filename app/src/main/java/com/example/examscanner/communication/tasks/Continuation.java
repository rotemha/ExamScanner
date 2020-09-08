package com.example.examscanner.communication.tasks;

public interface Continuation {
    public void cont();
    public static Continuation VolatilityException(){
        return new Continuation() {
            @Override
            public void cont() {
                throw new VolatilityExceptiom();
            }
        };
    }

    public static Continuation ShouldNotHappenException(){
        return new Continuation() {
            @Override
            public void cont() {
                throw new ShouldNotHappenException();
            }
        };
    }
}
