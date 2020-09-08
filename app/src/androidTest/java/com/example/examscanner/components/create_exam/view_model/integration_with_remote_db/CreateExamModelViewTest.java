package com.example.examscanner.components.create_exam.view_model.integration_with_remote_db;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.examscanner.components.create_exam.view_model.CreateExamModelViewAbstractTest;
import com.example.examscanner.persistence.remote.FirebaseDatabaseFactory;
import com.example.examscanner.persistence.remote.RemoteDatabaseFacadeFactory;
import com.example.examscanner.repositories.grader.Grader;
import com.example.examscanner.repositories.grader.GraderRepoFactory;

import org.junit.Before;
import org.junit.runner.RunWith;

import static com.example.examscanner.components.create_exam.CreateExamFragmentAbstractTestStateFull.BOB_ID;

@RunWith(AndroidJUnit4.class)
public class CreateExamModelViewTest extends CreateExamModelViewAbstractTest {
    @Override
    @Before
    public void setUp() throws Exception {
        FirebaseDatabaseFactory.setTestMode();
        super.setUp();
        new GraderRepoFactory().create().create(new Grader("bobexamscanner80@gmail.com",BOB_ID));
    }
}