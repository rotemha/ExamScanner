package com.example.examscanner.communication;

public class CommunicationException extends RuntimeException {
    public CommunicationException(Throwable cause) {
        super(cause);
    }

    public CommunicationException(String s) {
        super(s);
    }
}
