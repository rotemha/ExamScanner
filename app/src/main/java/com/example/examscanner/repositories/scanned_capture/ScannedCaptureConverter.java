package com.example.examscanner.repositories.scanned_capture;

import android.graphics.PointF;

import com.example.examscanner.communication.CommunicationFacade;
import com.example.examscanner.communication.entities_interfaces.ExamineeAnswerEntityInterface;
import com.example.examscanner.communication.entities_interfaces.ExamineeSolutionsEntityInterface;
import com.example.examscanner.communication.entities_interfaces.VersionEntityInterface;
import com.example.examscanner.log.ESLogeerFactory;
import com.example.examscanner.repositories.Converter;
import com.example.examscanner.repositories.exam.ExamConverter;
import com.example.examscanner.repositories.exam.Version;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class ScannedCaptureConverter implements Converter<ExamineeSolutionsEntityInterface, ScannedCapture> {
    private final String TAG = "ExamScanner";
    private CommunicationFacade comFacade;
    private OnApprovalCont approvalCont;

    public ScannedCaptureConverter(CommunicationFacade comFacade) {

        this.comFacade = comFacade;
        approvalCont = ()-> ESLogeerFactory.getInstance().log(TAG, "empty approval cont");
    }



    @Override
    public ScannedCapture convert(ExamineeSolutionsEntityInterface ei) {
        List<Answer> answers = new ArrayList<>();
        final long[] examineeAnswersIds = ei.getExamineeAnswersIds();
        for (int i = 0; i < examineeAnswersIds.length; i++) {
            answers.add(toAnswer(comFacade.getAnswerById(examineeAnswersIds[i]), ei.getBitmap().getWidth(), ei.getBitmap().getHeight()));
        }

        return new ScannedCapture(
                ei.getId(),
                ei.getBitmap(),
                answers,
                toVersionFuture(ei.getVersionId()),
                ei.getExaimneeId(),
                ei.getExamieeIdIsOccupiedByAnotherSolution(),
                ei.getIsValid(),
                g -> {comFacade.approveSolutionAndStats((int)ei.getId(), g);approvalCont.cont();}
        );
    }

    private Future<Version> toVersionFuture(long versionId) {
        return new Future<Version>() {
            private Version lazy = null;
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return lazy!=null;
            }

            @Override
            public Version get() throws ExecutionException, InterruptedException {
                if(isDone()){
                    return lazy;
                }
                VersionEntityInterface vei = comFacade.getVersionById(versionId);
                final ExamConverter examConverter = new ExamConverter(comFacade);
                lazy = new Version(
                        versionId,
                        vei.getNumber(),
                        examConverter.toExamFuture(vei.getExamId()),
                        examConverter.toFutureQuestions(versionId),
                        vei.getBitmap()

                );
                return lazy;
            }

            @Override
            public Version get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
                return null;
            }
        };
    }

    private Answer toAnswer(ExamineeAnswerEntityInterface ei, int bmWidth, int bmHeight) {
        if(ei.getAns()== Answer.CONFLICTED){
            return new ConflictedAnswer(
                    ei.getNum(),
                    new PointF(toRel(ei.getLeftX(), bmWidth),toRel(ei.getUpY(), bmHeight)),
                    new PointF(toRel(ei.getRightX(), bmWidth),toRel(ei.getBottomY(), bmHeight))
            );
        }else if(ei.getAns() == Answer.MISSING){
            return new MissingAnswer(ei.getNum());
        }else{
            return new ResolvedAnswer(
                    ei.getNum(),
                    new PointF(toRel(ei.getLeftX(), bmWidth),toRel(ei.getUpY(), bmHeight)),
                    new PointF(toRel(ei.getRightX(), bmWidth),toRel(ei.getBottomY(), bmHeight)),
                    ei.getAns()
            );
        }
    }

    private static float toRel(int param, int dimLength){
        return ((float) param)/((float)dimLength);
    }


    public void onApprove(OnApprovalCont cont) {
        this.approvalCont = cont;
    }
}
