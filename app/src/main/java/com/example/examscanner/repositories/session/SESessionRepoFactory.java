package com.example.examscanner.repositories.session;

import com.example.examscanner.communication.CommunicationFacadeFactory;

public class SESessionRepoFactory {
    public SESessionRepository create(){
        return SESessionRepository.getInstance(new CommunicationFacadeFactory().create());
    }
}
