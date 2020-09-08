package com.example.examscanner.repositories.session;

import com.example.examscanner.communication.CommunicationFacade;

public class CESessionsProvider {
    private CommunicationFacade communicationFacade;

    public CESessionsProvider(CommunicationFacade communicationFacade) {
        this.communicationFacade = communicationFacade;
    }
    public long provide(){
        return communicationFacade.createNewCreateExamSession();
    }
}
