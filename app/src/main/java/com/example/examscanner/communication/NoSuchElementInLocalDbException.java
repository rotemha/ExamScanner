package com.example.examscanner.communication;

class NoSuchElementInLocalDbException extends RuntimeException {
    public NoSuchElementInLocalDbException(String format) {
        super(format);
    }
}
