package com.example.examscanner.components.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examscanner.R;
import com.example.examscanner.repositories.exam.Exam;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeFragment extends Fragment {
    private static final int MY_PERMISSIONS_REQUEST_READ_WRITE_FILES = 0;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private HomeViewModel homeViewModel;
    private OnPermissionsGranted  cont;
    private FloatingActionButton floatingButton;

    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        floatingButton = ((FloatingActionButton) root.findViewById(R.id.floating_button_home_create_exam));
        floatingButton.setVisibility(View.INVISIBLE);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = HomeFragmentDirections.actionNavHomeToCreateExamFragment();
                Navigation.findNavController(v).navigate(action);
            }
        });
        return root;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ((ProgressBar) view.findViewById(R.id.progressBar_home)).setVisibility(View.VISIBLE);//Always good to set some good feedback
        recyclerView = (RecyclerView) view.findViewById(R.id.exams_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        cont = ()->{
            Observable.fromCallable(this::createViewModel)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onExamsRetrival);
        };
        if(!allPermissionsGranted()){
            askPemissions();
        }else{
            cont.cont();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_WRITE_FILES: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cont.cont();
                } else {

                }
                return;
            }
        }
    }


    private void askPemissions() {
        if (!allPermissionsGranted()) {
            requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_WRITE_FILES
            );
        }
    }

    private boolean allPermissionsGranted() {
        return ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED;
    }

    private List<LiveData<Exam>> createViewModel() {
        HViewModelFactory factory = new HViewModelFactory(this.getActivity());
        homeViewModel = new ViewModelProvider(this, factory).get(HomeViewModel.class);
        return homeViewModel.getExams();
    }


    @SuppressLint("RestrictedApi")
    public void onExamsRetrival(List<LiveData<Exam>> exams) {
        mAdapter = new MyAdapter(exams, this::onItemClick, this::onAdminPageItemClick,homeViewModel.getState());
        recyclerView.setAdapter(mAdapter);
        ((ProgressBar) getActivity().findViewById(R.id.progressBar_home)).setVisibility(View.INVISIBLE);
        floatingButton.setVisibility(View.VISIBLE);
    }

    public void onItemClick(Exam e){
        Observable.fromCallable(()-> getLastSession(e.getId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sessionId->onExamSessionRetrieved(sessionId,e.getId()));
    }

    public void onAdminPageItemClick(View v ,Exam e){
        HomeFragmentDirections.ActionNavHomeToAdminPage action = HomeFragmentDirections.actionNavHomeToAdminPage();
        action.setExamId(e.getId());
        Navigation.findNavController(v).navigate(action);
    }

    private long getLastSession(long examId) {
        return homeViewModel.getLastSESession(examId);
    }

    private void onExamSessionRetrieved(long sessionId, long examId) {
//        if(sessionId<0){
        if(true){//sessions disabled
            navigateToCapture(examId, -1);
        }else{
            askIfResumeSession(examId,sessionId);
        }
    }

    private void askIfResumeSession(long examId, long sessionId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.home_would_you_like_to_resume)
                .setPositiveButton(R.string.home_dialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        navigateToCapture(examId, sessionId);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void navigateToCapture(long eId, long sessioId) {
        HomeFragmentDirections.ActionNavHomeToCaptureFragment action =
                HomeFragmentDirections.actionNavHomeToCaptureFragment();
        action.setExamId(eId);
        action.setSessionId(sessioId);
        NavHostFragment.findNavController(this).navigate(action);
    }

    public HomeViewModel getHomeViewModel() {
        return homeViewModel;
    }

    interface OnItemClick {
        public void onItemClick(Exam e);
    }

    interface onAdminPagelicked{
        public void onAdminPageClicked(View v, Exam e);
    }

    private interface OnPermissionsGranted {
        void cont();
    }
}