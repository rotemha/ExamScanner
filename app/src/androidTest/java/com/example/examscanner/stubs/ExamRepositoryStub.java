package com.example.examscanner.stubs;

import com.example.examscanner.repositories.exam.Exam;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ExamRepositoryStub implements com.example.examscanner.repositories.Repository<com.example.examscanner.repositories.exam.Exam> {
    private List<Exam> exams = new ArrayList<>();
    @Override
    public int getId() {
        return 0;
    }

    @Override
    public Exam get(long id) {
        for (Exam e:exams) {
            if (e.getId()==id)
                return e;
        }
        throw new RuntimeException("No such exam");
    }

    @Override
    public List<Exam> get(Predicate<Exam> criteria) {
        ArrayList<Exam> ans = new ArrayList<>();
        for (Exam e:exams) {
            if(criteria.test(e))
                ans.add(e);
        }
        return ans;
    }

    @Override
    public void create(Exam exam) {
        exams.add(exam);
    }

    @Override
    public void update(Exam exam) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void removeFromCache(long id) {

    }


    public void tearDown(){
        exams = new ArrayList<>();
    }
}
