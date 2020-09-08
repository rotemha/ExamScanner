package com.example.examscanner.repositories.session;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.examscanner.communication.CommunicationFacade;
import com.example.examscanner.communication.entities_interfaces.ScanExamSessionEntityInterface;
import com.example.examscanner.repositories.Converter;
import com.example.examscanner.repositories.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

class SESessionRepository implements Repository<ScanExamSession> {

    private static SESessionRepository instance;
    private Converter<ScanExamSessionEntityInterface, ScanExamSession> converter;

    static SESessionRepository getInstance(CommunicationFacade comFacade) {
        if(instance==null){
            instance = new SESessionRepository(comFacade);
        }
        return instance;
    }

    private CommunicationFacade comFacade;

    public SESessionRepository(CommunicationFacade comFacade) {
        this.converter = new Converter<ScanExamSessionEntityInterface, ScanExamSession>() {
            @Override
            public ScanExamSession convert(ScanExamSessionEntityInterface ei) {
                return new ScanExamSession(ei.getId(),ei.getExamId());
            }
        };
        this.comFacade = comFacade;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public ScanExamSession get(long id) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<ScanExamSession> get(Predicate<ScanExamSession> criteria) {
        List<ScanExamSession> ans = new ArrayList<>();
        for (ScanExamSessionEntityInterface ei : comFacade.getScanExamSessions()) {
            ScanExamSession s = converter.convert(ei);
            if (criteria.test(s))
                ans.add(s);
        }
        return ans;
    }

    @Override
    public void create(ScanExamSession scanExamSession) {

    }

    @Override
    public void update(ScanExamSession scanExamSession) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void removeFromCache(long id) {
        throw new RuntimeException("not implemented");
    }

}
