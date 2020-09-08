package com.example.examscanner.use_case_contexts_creators;

import com.example.examscanner.components.scan_exam.capture.CameraManagerStub;
import com.example.examscanner.components.scan_exam.capture.camera.CameraMangerFactory;
import com.example.examscanner.components.scan_exam.detect_corners.DCEmptyRepositoryFactory;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.corner_detected_capture.CDCRepositoryFacrory;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.ExamRepositoryFactory;
import com.example.examscanner.stubs.ExamRepositoryStub;
import com.example.examscanner.stubs.ExamStubFactory;

import static com.example.examscanner.ImageProcessorsGenerator.fakeIP;

public class Context2Setuper {

    private Exam e;
    private int someExamineeId = 123456;

    public void setup(){
        CameraMangerFactory.setStubInstance(new CameraManagerStub());
        CDCRepositoryFacrory.ONLYFORTESTINGsetTestInstance(DCEmptyRepositoryFactory.create());
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(fakeIP());
        CDCRepositoryFacrory.ONLYFORTESTINGsetTestInstance(DCEmptyRepositoryFactory.create());
        CameraMangerFactory.setStubInstance(new CameraManagerStub());
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(fakeIP());
        Repository<Exam> stubERepo  = new ExamRepositoryStub();
        e = ExamStubFactory.instance1();
        stubERepo.create(
                e
        );
        ExamRepositoryFactory.setStubInstance(stubERepo);
    }

    public Exam getExam(){
        return e;
    }

    public int getSomeVersion(){
        return ExamStubFactory.instance1_dinaBarzilayVersion;
    }

    public String getSomeExamineeId(){
        return String.valueOf(someExamineeId++);
    }
}
