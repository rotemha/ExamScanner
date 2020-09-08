package com.example.examscanner.components.create_exam.view_model.unit;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.examscanner.authentication.state.StateFactory;
import com.example.examscanner.components.create_exam.CreateExamModelView;
import com.example.examscanner.components.create_exam.view_model.CreateExamModelViewAbstractTest;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.image_processing.ScanAnswersConsumer;
import com.example.examscanner.persistence.local.AppDatabaseFactory;
import com.example.examscanner.persistence.remote.RemoteDatabaseFacadeFactory;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.ExamRepositoryFactory;
import com.example.examscanner.repositories.exam.Version;
import com.example.examscanner.repositories.grader.GraderRepoFactory;
import com.example.examscanner.stubs.BobGradersStubRepoFacroty;
import com.example.examscanner.stubs.ImageProcessorStub;
import com.example.examscanner.stubs.RemoteDatabaseStubInstance;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class CreateExamModelViewTest extends CreateExamModelViewAbstractTest {
    @Override
    @Before
    public void setUp() throws Exception {
        RemoteDatabaseFacadeFactory.setStubInstance(new RemoteDatabaseStubInstance());
        GraderRepoFactory.setStubInstance(BobGradersStubRepoFacroty.get());
        super.setUp();
    }
}