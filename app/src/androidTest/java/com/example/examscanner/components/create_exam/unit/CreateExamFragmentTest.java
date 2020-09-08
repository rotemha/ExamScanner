package com.example.examscanner.components.create_exam.unit;

import androidx.fragment.app.testing.FragmentScenario;

import com.example.examscanner.components.create_exam.CreateExamFragment;
import com.example.examscanner.components.create_exam.CreateExamFragmentAbstractTest;
import com.example.examscanner.components.create_exam.get_version_file.VersionImageGetterFactory;
import com.example.examscanner.image_processing.ImageProcessingFactory;

import org.junit.Test;

import static com.example.examscanner.ImageProcessorsGenerator.fakeIP;
import static com.example.examscanner.Utils.loadOpenCV;

public class CreateExamFragmentTest extends CreateExamFragmentAbstractTest {
    @Test
    public void testAddVersionStubIP() {
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(fakeIP());
        scenraio = FragmentScenario.launchInContainer(CreateExamFragment.class);
        loadOpenCV(scenraio);
        VersionImageGetterFactory.setStubInstance(null);
        testAddVersion();
    }
}
