package com.example.examscanner.stubs;

import androidx.test.espresso.core.internal.deps.guava.collect.Lists;

import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.grader.Grader;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.example.examscanner.components.create_exam.CreateExamFragmentAbstractTestStateFull.BOB_ID;

public class BobGradersStubRepoFacroty {
    public static Repository<Grader> get() {
        return new Repository<Grader>() {
            private List<Grader> graders = new ArrayList<Grader>(){{
                add(new Grader("bobexamscanner80@gmail.com", BOB_ID));
            }};
            @Override
            public int getId() {
                throw new NotImplementedException();
            }

            @Override
            public Grader get(long id) {
                throw new NotImplementedException();
            }

            @Override
            public List<Grader> get(Predicate<Grader> criteria) {
                return graders;
            }

            @Override
            public void create(Grader grader) {
                throw new NotImplementedException();
            }

            @Override
            public void update(Grader grader) {
                throw new NotImplementedException();
            }

            @Override
            public void delete(int id) {
                throw new NotImplementedException();
            }

            @Override
            public void removeFromCache(long id) {

            }

        };
    }

    public static class NotImplementedException extends RuntimeException {}
}
