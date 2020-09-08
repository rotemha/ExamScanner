package com.example.examscanner.repositories.session;

import com.example.examscanner.communication.CommunicationFacadeFactory;

public class SESessionProviderFactory {
    public SESessionsProvider create(){
        return new SESessionsProvider(new CommunicationFacadeFactory().create());
    }
}
