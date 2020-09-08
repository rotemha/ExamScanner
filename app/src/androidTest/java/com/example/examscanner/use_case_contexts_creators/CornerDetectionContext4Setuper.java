package com.example.examscanner.use_case_contexts_creators;

import com.example.examscanner.authentication.state.State;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.components.scan_exam.capture.Capture;
import com.example.examscanner.components.scan_exam.capture.CaptureViewModel;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class CornerDetectionContext4Setuper extends CornerDetectionContext2Setuper {
    private CaptureViewModel captureViewModel;
    public CornerDetectionContext4Setuper(State state) {
        super(state);
    }

    @Override
    public void setup() {
        super.setup();
        captureViewModel = new CaptureViewModel(
                getSCRepo(),
                getImageProcessor(),
                -1,
                getTheExam(),
                state
        );
        captureViewModel.setVersion(getVersionNum());
        captureViewModel.setExamineeId(getSomeExamineeId());
        captureViewModel.consumeCapture(
                new Capture(
                        BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_1(),
                        captureViewModel.getCurrentExamineeId().getValue(),
                        captureViewModel.getCurrentVersion().getValue()
                )
        );
        Completable.fromAction(
                ()->captureViewModel.processCapture()
        ).subscribeOn(Schedulers.io()).blockingAwait();
        try {
            Thread.sleep(5*1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String getSomeExamineeId() {
        return "123/456";
    }
}
