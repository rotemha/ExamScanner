package com.example.examscanner.repositories;

import com.example.examscanner.communication.CommunicationException;

public class RepositoryException extends RuntimeException{
    public RepositoryException(CommunicationException e) {
        super(e);
    }
    public RepositoryException() {
        super();
    }
}
