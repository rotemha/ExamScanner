package com.example.examscanner.repositories.exam;

import android.Manifest;

import androidx.test.rule.GrantPermissionRule;

import com.example.examscanner.communication.ContextProvider;
import com.example.examscanner.repositories.Repository;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static java.lang.Thread.sleep;
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;

import java.util.List;

public class ExamIsCreatedTest extends ExamRepositoryTest {
    Repository<Exam> out;
    private Exam theExam;

    @Rule
    public GrantPermissionRule write = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    @Rule
    public GrantPermissionRule rule = GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE);

    @Override
    public void setUp() throws Exception {
        super.setUp();
        ContextProvider.set(getInstrumentation().getContext());
        out = new ExamRepositoryFactory().create();
        theExam = ExamInstancesGenerator.getInstance1();
        out.create(theExam);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testVersionBitmapIsNotNull() {
        List<Exam> exams = out.get(e->true);
        assert exams.size()==1;
        Exam theQueriedExam = exams.get(0);
        List<Version> versions = theQueriedExam.getVersions();
        assert versions.size()==1;
        Version v = versions.get(0);
        assertNotNull(v.getPerfectImage());
        try {
            sleep(10*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
