package com.example.examscanner.repositories.corner_detected_capture;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.examscanner.communication.CommunicationFacade;
import com.example.examscanner.communication.CommunicationFacadeFactory;
import com.example.examscanner.communication.entities_interfaces.SemiScannedCaptureEntityInterface;
import com.example.examscanner.repositories.Converter;
import com.example.examscanner.repositories.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CDCRepository implements Repository<CornerDetectedCapture> {
    private int currAvailableId;
    private static CDCRepository instance;
    private List<CornerDetectedCapture> data = new ArrayList<>();
    private CommunicationFacade comFacade;
    private Converter<SemiScannedCaptureEntityInterface, CornerDetectedCapture> converter;

    public static CDCRepository getInstance() {
        if (instance == null) {
            instance = new CDCRepository(
                    new CommunicationFacadeFactory().create(),
                    new CDCConverter()
            );
            return instance;
        } else {
            return instance;
        }
    }

    private CDCRepository(CommunicationFacade comFacade, Converter<SemiScannedCaptureEntityInterface, CornerDetectedCapture> converter) {
        this.comFacade = comFacade;
        this.converter = converter;
    }

    static void tearDown() {
        instance=null;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public CornerDetectedCapture get(long id) {
        Long lid = new Long(id);
        return converter.convert(comFacade.getSemiScannedCapture(lid));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<CornerDetectedCapture> get(Predicate<CornerDetectedCapture> criteria) {
        List<CornerDetectedCapture> ans = new ArrayList<>();
        for (CornerDetectedCapture cdc : data) {
            if (criteria.test(cdc)) ans.add(cdc);
        }
        return ans;
    }

    @Override
    public void create(CornerDetectedCapture cornerDetectedCapture) {
        long id = comFacade.createSemiScannedCapture(
                cornerDetectedCapture.getLeftMostX(),
                cornerDetectedCapture.getUpperMostY(),
                cornerDetectedCapture.getRightMostX(),
                cornerDetectedCapture.getBottomMostY(),
                cornerDetectedCapture.getSession(),
                cornerDetectedCapture.getBitmap()
        );
        cornerDetectedCapture.setId(id);
        data.add(cornerDetectedCapture);
    }

    @Override
    public void update(CornerDetectedCapture cornerDetectedCapture) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void removeFromCache(long id) {

    }

//    @Override
//    public int genId() {
//        int ans = currAvailableId;
//        currAvailableId++;
//        return ans;
//    }
}
