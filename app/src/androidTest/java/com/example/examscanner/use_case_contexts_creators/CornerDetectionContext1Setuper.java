package com.example.examscanner.use_case_contexts_creators;

import android.graphics.PointF;

import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.components.scan_exam.detect_corners.DCEmptyRepositoryFactory;
import com.example.examscanner.components.scan_exam.reslove_answers.SCEmptyRepositoryFactory;
import com.example.examscanner.image_processing.DetectCornersConsumer;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.corner_detected_capture.CDCRepositoryFacrory;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCapture;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.ExamRepositoryFactory;
import com.example.examscanner.repositories.exam.ExamineeIdsSocket;
import com.example.examscanner.repositories.exam.Version;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;
import com.example.examscanner.repositories.session.SESessionProviderFactory;

import java.util.ArrayList;

import static com.example.examscanner.ImageProcessorsGenerator.fakeIP;

public class CornerDetectionContext1Setuper {
    private static final int QAD_NUM_OF_QUESTIONS = 50;
    protected Repository<Exam> examRepository;
    protected ImageProcessingFacade imageProcessor;
    protected Repository<CornerDetectedCapture> cdcRepo;
    protected Repository<ScannedCapture> scRepo;
    protected long scanExamSession;
    protected Exam e;
    protected int dinaBarzilayVersion;
    protected int theDevilVersion;
//    private String uId;

    public void setup(){
            e = new Exam(null,-1,Exam.theEmptyFutureVersionsList(),new ArrayList<>(),"theTestExamCourseName",0,0,0,"2020",QAD_NUM_OF_QUESTIONS, 0, ExamineeIdsSocket.getEmpty(),true, Exam.DownloadCompletable.getEmpty(),Exam.DownloadCompletable.getEmpty());
        examRepository = new ExamRepositoryFactory().create();
        dinaBarzilayVersion = 496351;
        e.addVersion(new Version(-1, dinaBarzilayVersion,Version.toFuture(e),Version.theEmptyFutureQuestionsList(), BitmapsInstancesFactoryAndroidTest.getTestJpg1()));
        theDevilVersion = 666;
        e.addVersion(new Version(-1, theDevilVersion,Version.toFuture(e),Version.theEmptyFutureQuestionsList(), BitmapsInstancesFactoryAndroidTest.getTestJpg1()));
        examRepository.create(e);
        CDCRepositoryFacrory.ONLYFORTESTINGsetTestInstance(DCEmptyRepositoryFactory.create());
        scRepo = SCEmptyRepositoryFactory.create();
        ScannedCaptureRepositoryFactory.ONLYFORTESTINGsetTestInstance(scRepo);
        imageProcessor = fakeIP();
        cdcRepo = new CDCRepositoryFacrory().create();
        scanExamSession = new SESessionProviderFactory().create().provide(e.getId());
        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg1(), new DetectCornersConsumer() {
            @Override
            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                cdcRepo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked(), upperLeft, upperRight,bottomRight,bottomLeft, scanExamSession));
            }
        });
        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg2(), new DetectCornersConsumer() {
            @Override
            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                cdcRepo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked(), upperLeft, upperRight,bottomRight,bottomLeft, scanExamSession));
            }
        });
        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestJpg3(), new DetectCornersConsumer() {
            @Override
            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                cdcRepo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked(), upperLeft, upperRight,bottomRight,bottomLeft, scanExamSession));
            }
        });
    }

    public ImageProcessingFacade getImageProcessor() {
        return imageProcessor;
    }

    public Repository<CornerDetectedCapture> getCDCRepo() {
        return cdcRepo;
    }

    public Repository<ScannedCapture> getSCRepo() {
        return scRepo;
    }

    public Exam getTheExam() {
        return e;
    }

    public int getDinaBarzilayVersion() {
        return dinaBarzilayVersion;
    }

    public int getTheDevilVersion() {
        return theDevilVersion;
    }

    public void tearDown() {
//        ((ExamRepositoryStub)examRepository).tearDown();
    }
    public void addAuthPic1(){
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(null);
        imageProcessor =new ImageProcessingFactory().create();
        imageProcessor.detectCorners(BitmapsInstancesFactoryAndroidTest.getTestAuthPic1Marked(), new DetectCornersConsumer() {
            @Override
            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
                cdcRepo.create(new CornerDetectedCapture(BitmapsInstancesFactoryAndroidTest.getTestAuthPic1Marked(), upperLeft, upperRight,bottomRight,bottomLeft, scanExamSession));
            }
        });
    }
}
