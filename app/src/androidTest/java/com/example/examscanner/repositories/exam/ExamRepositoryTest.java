package com.example.examscanner.repositories.exam;

import com.example.examscanner.communication.CommunicationFacadeFactory;
import com.example.examscanner.components.scan_exam.detect_corners.RemoteFilesManagerStub;
import com.example.examscanner.persistence.local.AppDatabaseFactory;
import com.example.examscanner.persistence.remote.RemoteDatabaseFacadeFactory;
import com.example.examscanner.persistence.remote.files_management.RemoteFilesManagerFactory;
import com.example.examscanner.repositories.corner_detected_capture.CDCRepositoryFacrory;
import com.example.examscanner.repositories.grader.GraderRepoFactory;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;
import com.example.examscanner.stubs.RemoteDatabaseStubInstance;

import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.*;

public abstract class ExamRepositoryTest {

    @Before
    public void setUp() throws Exception {
        RemoteDatabaseFacadeFactory.setStubInstance(new RemoteDatabaseStubInstance());
        RemoteFilesManagerFactory.setStubInstabce(new RemoteFilesManagerStub());
        AppDatabaseFactory.setTestMode();
    }

    @After
    public void tearDown() throws Exception {
        AppDatabaseFactory.tearDownDb();
        RemoteDatabaseFacadeFactory.tearDown();
        CommunicationFacadeFactory.tearDown();
        RemoteFilesManagerFactory.tearDown();
        ExamRepositoryFactory.tearDown();
        CDCRepositoryFacrory.tearDown();
        ScannedCaptureRepositoryFactory.tearDown();
        GraderRepoFactory.tearDown();
    }
}