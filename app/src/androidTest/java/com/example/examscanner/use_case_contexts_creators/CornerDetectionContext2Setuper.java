package com.example.examscanner.use_case_contexts_creators;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.util.Log;

import com.example.examscanner.authentication.state.State;
import com.example.examscanner.authentication.state.StateFactory;
import com.example.examscanner.authentication.state.StateHolder;
import com.example.examscanner.components.create_exam.CreateExamModelView;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.components.scan_exam.capture.Capture;
import com.example.examscanner.components.scan_exam.detect_corners.DCEmptyRepositoryFactory;
import com.example.examscanner.components.scan_exam.reslove_answers.SCEmptyRepositoryFactory;
import com.example.examscanner.image_processing.DetectCornersConsumer;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.image_processing.ImageProcessor;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.corner_detected_capture.CDCRepositoryFacrory;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCapture;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.ExamRepositoryFactory;
import com.example.examscanner.repositories.exam.Version;
import com.example.examscanner.repositories.grader.GraderRepoFactory;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;
import com.example.examscanner.repositories.session.SESessionProviderFactory;
import com.example.examscanner.stubs.ExamRepositoryStub;

import org.opencv.android.OpenCVLoader;

import java.util.List;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.assertEquals;

public class CornerDetectionContext2Setuper {
    private int someExamineeId = 123456;
    private Capture capture;
    private String TAG = "ExamScanner";;
    public static boolean stubExamRepo = true;

    public CornerDetectionContext2Setuper(State state, Bitmap comp191_v1_ins_1) {
        this.state = state;

        this.theCDCBitmap = comp191_v1_ins_1;
    }

    public CornerDetectionContext2Setuper(Bitmap cdcBitmap, State s) {
        this.theCDCBitmap = cdcBitmap;
        this.state = s;
    }

    public CornerDetectionContext2Setuper(Bitmap cdcBitmap, Bitmap examBitmap, State s) {
        this.theCDCBitmap = cdcBitmap;
        createExamBitmap = examBitmap;
        this.state = s;
    }

    public CornerDetectionContext2Setuper() {
    }

    public CornerDetectionContext2Setuper(State s) {
        this.state =s;
    }

    public CornerDetectionContext2Setuper(State state, Bitmap comp191_v1_ins_9, Bitmap comp191_v1_ins_in1) {
        this.state= state;
        this.theCDCBitmap = comp191_v1_ins_9;
        createExamBitmap = comp191_v1_ins_in1;
    }

    public CornerDetectionContext2Setuper(State state, ImageProcessingFacade alignmentSubIp) {
        this.state =state;
        imageProcessingFacadeStub = alignmentSubIp;
    }

    public void setPDF(boolean PDF) {
        this.PDF = PDF;
    }
    private boolean PDF = false;
    protected Repository<Exam> examRepository;
    protected ImageProcessingFacade imageProcessor;
    //    protected Repository<CornerDetectedCapture> cdcRepo;
    protected Repository<ScannedCapture> scRepo;
    protected long scanExamSession;
    protected Exam e;
    protected int dinaBarzilayVersion;
    protected CreateExamModelView ceModelView;
    protected final CornerDetectedCapture[] cdCaptures = new CornerDetectedCapture[1];
    protected Bitmap createExamBitmap;
    private int i;
    protected State state;
    private Bitmap theCDCBitmap;

    private static ImageProcessingFacade imageProcessingFacadeStub;

    public CornerDetectionContext2Setuper(Bitmap testJpg1, Bitmap testJpg11, int i) {
        this.theCDCBitmap = testJpg1;
        createExamBitmap = testJpg11;
        this.i = i;
    }

    public CornerDetectionContext2Setuper(State s, Bitmap testJpg1, Bitmap testJpg11, int i) {
        this.state=s;
        this.theCDCBitmap = testJpg1;
        createExamBitmap = testJpg11;
        this.i = i;
    }

    public void setup() {
        OpenCVLoader.initDebug();
        if(imageProcessingFacadeStub == null){
            ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(new ImageProcessor(getApplicationContext()));
        }else{
            ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(imageProcessingFacadeStub);
        }
        if(stubExamRepo){
            examRepository = new ExamRepositoryStub();
            ExamRepositoryFactory.setStubInstance(examRepository);
        }else{
            examRepository = new ExamRepositoryFactory().create();
        }
        imageProcessor = new ImageProcessingFactory().create();
        ceModelView = new CreateExamModelView(
                new ExamRepositoryFactory().create(),
                new GraderRepoFactory().create(),
                imageProcessor,
                state,
                0
        );

        dinaBarzilayVersion = 496351;
        if(i!=0){
            ceModelView.holdNumOfQuestions(String.valueOf(i));
        }else{
            ceModelView.holdNumOfQuestions("50");
        }
        int numOfQuestions = ceModelView.getExam().getNumOfQuestions();
        if (createExamBitmap == null) {
            createExamBitmap = getOriginalVersionBitmap();
        }
        ceModelView.holdVersionBitmap(createExamBitmap);
        ceModelView.holdVersionNumber(dinaBarzilayVersion);
        ceModelView.addVersion();
        ceModelView.holdExamUrl("https://docs.google.com/spreadsheets/d/1AoTx5aw0t4e_KvjUpHUmvAkd6Ywj9_Mqd5sluJQfcVA/edit?usp=sharing");
        ceModelView.create("testAddVersion()_courseName", "A", "Fall", "2020");
        List<Exam> exams = examRepository.get((e) -> true);
        e = exams.get(0);

        Log.d(TAG, String.format("TEST:contextSetup, examid is %d was created succefully", e.getId()));


//        CDCRepositoryFacrory.ONLYFORTESTINGsetTestInstance(DCEmptyRepositoryFactory.create());
        if(scRepo ==null){
            scRepo = SCEmptyRepositoryFactory.create();
        }
        ScannedCaptureRepositoryFactory.ONLYFORTESTINGsetTestInstance(scRepo);
        //   ScannedCaptureRepositoryFactory.ONLYFORTESTINGsetTestInstance(scRepo);
//        cdcRepo = new CDCRepositoryFacrory().create();
        scanExamSession = new SESessionProviderFactory().create().provide(e.getId());
        if (theCDCBitmap == null) {
            theCDCBitmap = BitmapsInstancesFactoryAndroidTest.getComp191_V1_PDF_Auth_No_Flash();
        }
//        imageProcessor.detectCorners(theCDCBitmap, new DetectCornersConsumer() {
//            @Override
//            public void consume(PointF upperLeft, PointF upperRight, PointF bottomLeft, PointF bottomRight) {
//                cdCaptures[0] = new CornerDetectedCapture(theCDCBitmap, upperLeft, upperRight,bottomRight,bottomLeft, scanExamSession);
//                cdcRepo.create(cdCaptures[0]);
//            }
//        });
        e.setNumOfQuestions(50);
//        getCDC().setVersion(e.getVersionByNum(dinaBarzilayVersion));
        capture = new Capture(
                theCDCBitmap,
                getSomeExamineeId(),
                getVersion()
        );
    }

    private Bitmap getOriginalVersionBitmap() {
        if (PDF) {
            if(createExamBitmap != null){
                return createExamBitmap;
            }
            return BitmapsInstancesFactoryAndroidTest.getComp191_V1_pdf_ins_in1();
        } else {
            return BitmapsInstancesFactoryAndroidTest.getComp191_v1_JPG_ANS_2();
        }
    }

    public ImageProcessingFacade getImageProcessor() {
        return imageProcessor;
    }

//    public Repository<CornerDetectedCapture> getCDCRepo() {
//        return cdcRepo;
//    }

    public Repository<ScannedCapture> getSCRepo() {
        return scRepo;
    }

    public Exam getTheExam() {
        return e;
    }

    public int getDinaBarzilayVersion() {
        return dinaBarzilayVersion;
    }

    public void tearDown() {
        imageProcessingFacadeStub = null;
//        ((ExamRepositoryStub)examRepository).tearDown();
    }

//    public CornerDetectedCapture getCDC() {
//        return cdCaptures[0];
//    }

    public int getVersionNum() {
        return dinaBarzilayVersion;
    }

    public Version getVersion() {
        return getTheExam().getVersionByNum(getVersionNum());
    }

    public Bitmap getOrigVersionImage() {
        return createExamBitmap;
    }


    public int getSomeVersion() {
        return getDinaBarzilayVersion();
    }

    public String getSomeExamineeId() {
        return String.valueOf(someExamineeId++);
    }

    //    public Bitmap getAuthBitmap() {
//        return BitmapsInstancesFactoryAndroidTest.getComp191_V1_PDF_Auth_No_Flash();
//    }
    public Capture getCapture() {
        return capture;
    }

    public static void setIOStub(ImageProcessingFacade imageProcessingFacade) {
        imageProcessingFacadeStub = imageProcessingFacade;
    }

    public void setSCRepo(Repository<ScannedCapture> repository) {
        scRepo=repository;
    }

    public State getState() {
        return state;
    }
}
