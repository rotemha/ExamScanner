package com.example.examscanner.repositories.corner_detected_capture;

import com.example.examscanner.communication.entities_interfaces.SemiScannedCaptureEntityInterface;
import com.example.examscanner.repositories.Converter;

public class CDCConverter implements Converter<SemiScannedCaptureEntityInterface, CornerDetectedCapture> {


    @Override
    public CornerDetectedCapture convert(SemiScannedCaptureEntityInterface semiScannedCapture) {
        CornerDetectedCapture ans = new CornerDetectedCapture(
                semiScannedCapture.getBitmap(),
                semiScannedCapture.getLeftMostX(),
                semiScannedCapture.getUpperMostY(),
                semiScannedCapture.getRightMostX(),
                semiScannedCapture.getBottomMosty(),
                semiScannedCapture.getSessionId()
        );
        ans.setId(semiScannedCapture.getId());
        return ans;
    }
}
