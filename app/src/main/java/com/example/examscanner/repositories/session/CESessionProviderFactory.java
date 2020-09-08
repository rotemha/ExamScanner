package com.example.examscanner.repositories.session;

import com.example.examscanner.communication.CommunicationFacadeFactory;

public class CESessionProviderFactory {
    public CESessionsProvider create(){
        return new CESessionsProvider(new CommunicationFacadeFactory().create());
    }
}
