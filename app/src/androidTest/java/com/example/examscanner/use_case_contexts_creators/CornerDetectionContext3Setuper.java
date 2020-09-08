package com.example.examscanner.use_case_contexts_creators;

import android.util.Log;

import com.example.examscanner.authentication.state.State;
import com.example.examscanner.image_processing.ScanAnswersConsumer;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

import static java.lang.Thread.sleep;

public class CornerDetectionContext3Setuper extends CornerDetectionContext2Setuper {
    private static final String DONT_KNOW_EAMINEE_ID = "DONT_KNOW_EAMINEE_ID";
    private static final String QAD_GRADER_EMAIL = "QAD_GRADER_EMAIL";
    private ScannedCapture sc;
    String DEBUG_TAG="DebugExamScanner";

    public CornerDetectionContext3Setuper(State s) {
        super(s);
    }

    @Override
    public void setup() {
        stubExamRepo = false;
        super.setup();
//        ScannedCaptureRepositoryFactory.tearDown();
//        scRepo = new ScannedCaptureRepositoryFactory().create();
        getCapture().setBitmap(
                imageProcessor.align(
                        getCapture().getBitmap(),
                        getVersion().getPerfectImage()
                )
        );
        imageProcessor.scanAnswers(
                getCapture().getBitmap(),
                getTheExam().getNumOfQuestions(),
                new ScanAnswersConsumer() {
                    @Override
                    public void consume(int numOfAnswersDetected, int[] answersIds, float[] lefts, float[] tops, float[] rights, float[] bottoms, int[] selections) {
                        sc = new ScannedCapture(-1, getCapture().getBitmap(), getCapture().getBitmap(), getTheExam().getNumOfQuestions(), numOfAnswersDetected, answersIds, lefts, tops, rights, bottoms, selections, getCapture().getVersion(), DONT_KNOW_EAMINEE_ID, QAD_GRADER_EMAIL);
                        Log.d(DEBUG_TAG, "CornerDetectionContext3Setuper: creating sc");
                        getSCRepo().create(sc);
                        try {
                            sleep(30*1000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        Log.d(DEBUG_TAG, "CornerDetectionContext3Setuper: approving sc");
                        Completable.fromAction(() -> sc.approve()).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).blockingAwait();
                    }
                },
                getVersion().getRealtiveLefts(),
                getVersion().getRealtiveUps()
        );
    }

    public ScannedCapture getSc() {
        return sc;
    }
}
