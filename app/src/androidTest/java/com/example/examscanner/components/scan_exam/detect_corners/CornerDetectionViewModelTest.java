package com.example.examscanner.components.scan_exam.detect_corners;

import com.example.examscanner.components.scan_exam.AbstractComponentInstrumentedTest;
import com.example.examscanner.persistence.remote.RemoteDatabaseFacadeFactory;
import com.example.examscanner.persistence.remote.files_management.RemoteFilesManagerFactory;
import com.example.examscanner.repositories.exam.Version;
import com.example.examscanner.stubs.RemoteDatabaseStubInstance;
import com.example.examscanner.use_case_contexts_creators.CornerDetectionContext1Setuper;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

@Ignore("NOT USING")
public class CornerDetectionViewModelTest extends AbstractComponentInstrumentedTest {
    private CornerDetectionContext1Setuper useCaseContext;
    private CornerDetectionViewModel out;

    @Before
    public void setUp() {
        RemoteDatabaseFacadeFactory.setStubInstance(new RemoteDatabaseStubInstance());
        RemoteFilesManagerFactory.setStubInstabce(new RemoteFilesManagerStub());
        super.setUp();
        useCaseContext = new CornerDetectionContext1Setuper();
        useCaseContext.setup();
        out = new CornerDetectionViewModel(
                useCaseContext.getImageProcessor(),
//                useCaseContext.getCDCRepo(),
                useCaseContext.getSCRepo(),
                useCaseContext.getTheExam()
        );
    }

    @Override
    public void tearDown(){
        useCaseContext.tearDown();
        super.tearDown();
    }

    @Test
    public void getVersionNumbers() {
        assertArrayEquals(
                useCaseContext.getTheExam().getVersions().stream().mapToInt(Version::getNum).toArray(),
                new int[1]//TODO - transfer to Capture frag
        );
    }
}