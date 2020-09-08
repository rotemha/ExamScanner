package com.example.examscanner.repositories.grader;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.examscanner.communication.CommunicationFacade;
import com.example.examscanner.communication.CommunicationFacadeFactory;
import com.example.examscanner.communication.entities_interfaces.GraderEntityInterface;
import com.example.examscanner.repositories.Converter;
import com.example.examscanner.repositories.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class GraderRepository implements Repository<Grader> {
    private CommunicationFacade comFacade;
    private static Repository<Grader> instance;
    private static final String TAG = "GraderRepository";
    private Converter<GraderEntityInterface, Grader> converter;

    public GraderRepository(CommunicationFacade comFacade, Converter<GraderEntityInterface, Grader> converter) {
        this.comFacade = comFacade;
        this.converter = converter;
    }

    public static Repository<Grader> getInstance() {
        if (instance == null) {
            instance = new GraderRepository(
                    new CommunicationFacadeFactory().create(),
                    new GraderConverter()
            );
            return instance;
        } else {
            return instance;
        }
    }

    static void tearDown() {
        instance = null;
    }

    static void setInstance(Repository<Grader> graderRepository) {
        instance = graderRepository;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public Grader get(long id) {
        //TODO - implement comFacade get Grader
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<Grader> get(Predicate<Grader> criteria) {
        List<Grader> ans = new ArrayList<>();
        for (GraderEntityInterface gei :
                comFacade.getGraders()) {
            Grader curr = converter.convert(gei);
            if(criteria.test(curr))
                ans.add(curr);
        }
        return ans;
    }

    @Override
    public void create(Grader grader) {
        comFacade.createGrader(grader.getUserEmail(), grader.getId());
    }

    @Override
    public void update(Grader grader) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void removeFromCache(long id) {
        throw new RuntimeException("not impplenented");
    }


}
