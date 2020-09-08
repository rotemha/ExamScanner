package com.example.examscanner.stubs;

import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.RepositoryException;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;

import java.util.List;
import java.util.function.Predicate;

public class FaultyScannedCaptureRepo implements Repository<ScannedCapture> {
    int numOfErrors = 1;
    Repository<ScannedCapture> real = new ScannedCaptureRepositoryFactory().create();
    @Override
    public int getId() {
        return 0;
    }

    @Override
    public ScannedCapture get(long id) {
        return real.get(id);
    }

    @Override
    public List<ScannedCapture> get(Predicate<ScannedCapture> criteria) {
        return real.get(criteria);
    }

    @Override
    public void create(ScannedCapture scannedCapture) throws RepositoryException {
        if(numOfErrors-->0){
            throw new RepositoryException();
        }else{
            real.create(scannedCapture);
        }
    }

    @Override
    public void update(ScannedCapture scannedCapture) {
        real.update(scannedCapture);
    }

    @Override
    public void delete(int id) {
        real.delete(id);
    }

    @Override
    public void removeFromCache(long id) {
        real.removeFromCache(id);
    }
}
