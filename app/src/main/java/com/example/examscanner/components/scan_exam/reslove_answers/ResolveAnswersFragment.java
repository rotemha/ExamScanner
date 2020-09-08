package com.example.examscanner.components.scan_exam.reslove_answers;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.example.examscanner.R;
import com.example.examscanner.log.ESLogeerFactory;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ResolveAnswersFragment extends Fragment {
    public static final String TAG ="ExamScanner";
    private static final String MSG_PREF = "ResolveAnswersFragment";
    private ResolveAnswersViewModel resolveAnswersViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_resolve_answers, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Completable.fromAction(()->{
            resolveAnswersViewModel = new ViewModelProvider(
                    this.getActivity(),
                    new RAViewModelFactory(getActivity())
            ).get(ResolveAnswersViewModel.class);
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::onModelViewCreated, this::onModelViewCreatedFailure);
    }

    private void onModelViewCreated() {
        ViewPager2 viewPager = (ViewPager2)getActivity().findViewById(R.id.viewPager2_scanned_captures);
        ScannedCapturesAdapter scannedCapturesAdapter = new ScannedCapturesAdapter(getActivity(), resolveAnswersViewModel.getScannedCaptures(), viewPager);
        viewPager.setAdapter(scannedCapturesAdapter);
        scannedCapturesAdapter.getPosition().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                ((TextView)getActivity().findViewById(R.id.textView_ra_progress_feedback)).setText(
                        integer+"/"+resolveAnswersViewModel.getScannedCaptures().size()
                );
            }
        });
        ((Button)getActivity().findViewById(R.id.button_ra_resolve_conflicts)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ResolveAnswersFragmentDirections.ActionFragmentResolveAnswersToResolveConflictedAnswersFragment action = ResolveAnswersFragmentDirections.actionFragmentResolveAnswersToResolveConflictedAnswersFragment();
//                action.setScanId(scannedCapturesAdapter.getCurrentScanId());
//                Navigation.findNavController(view).navigate(action);
            }
        });
    }

    private void onModelViewCreatedFailure(Throwable throwable) {
        ESLogeerFactory.getInstance().log(TAG, MSG_PREF, throwable);
    }
}
