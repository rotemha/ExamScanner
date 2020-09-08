package com.example.examscanner;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(
        {
                com.example.examscanner.components.create_exam.view_model.integration_with_remote_db.CreateExamUpdatesGraderTest.class,
                com.example.examscanner.components.admin.AdminViewModelTest.class,
                com.example.examscanner.repositories.scanned_capture.integration_with_remote_db.CreateTransaction.class,
                com.example.examscanner.components.create_exam.view_model.unit.CreateExamModelViewTest.class,
                com.example.examscanner.repositories.exam.ExamIsCreatedTest.class,
                com.example.examscanner.components.integrations.ScannedCaptureInserted_ExamineeSolutionIsStored.class,
                com.example.examscanner.communication.CommunicationFacadeTest.class,
                com.example.examscanner.core_algorithm.SmallCoreAlgorithmPDFTest.class,
                com.example.examscanner.core_algorithm.SmallCoreAlgorithmJPGTest.class,
        }
)

public class StableTestSuite {
}

// Runs all unit tests.


