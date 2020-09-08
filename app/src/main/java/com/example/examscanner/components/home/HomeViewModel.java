package com.example.examscanner.components.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.examscanner.authentication.state.State;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.session.ScanExamSession;


import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private Repository<Exam> examRepository;
    private Repository<ScanExamSession> sesRepo;
    private State state;

    public HomeViewModel(Repository<Exam> examRepository, Repository<ScanExamSession> sesRepo, State state) {
         this.examRepository = examRepository;
        this.sesRepo = sesRepo;
        this.state = state;
    }

    public List<LiveData<Exam>> getExams() {
        List<LiveData<Exam>>  mExams = new ArrayList<>();
        for (Exam e :examRepository.get(e ->
                e.getManagerId().equals(state.getId()) || e.getGraders().stream()
                        .anyMatch(g -> g.getId().equals(state.getId())))) {
            mExams.add(new MutableLiveData<>(e));
        }
        return mExams;
    }



    public long getLastSESession(long examId) {
        List<ScanExamSession> ses = sesRepo.get(s->s.examId()==examId);
        if(ses.size()==0)return -1;
        long last = -1;
        for (ScanExamSession s:ses) {
            last = Math.max(last,s.getId());
        }
        return last;
    }

    public State getState() {
        return state;
    }


}