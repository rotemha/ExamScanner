package com.example.examscanner.repositories.session;

import com.example.examscanner.communication.CommunicationFacade;

public class SESessionsProvider {
    CommunicationFacade comFacade;
    SESessionsProvider(CommunicationFacade comFacade){
        this.comFacade = comFacade;
    }
    public long provide(long exanId){
        return comFacade.createNewScanExamSession(exanId);
    }
}
