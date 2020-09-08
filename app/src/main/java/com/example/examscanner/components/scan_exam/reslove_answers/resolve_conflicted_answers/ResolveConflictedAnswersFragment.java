package com.example.examscanner.components.scan_exam.reslove_answers.resolve_conflicted_answers;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.example.examscanner.R;
import com.example.examscanner.components.scan_exam.detect_corners.CornerDetectionFragmentDirections;
import com.example.examscanner.components.scan_exam.detect_corners.CornerDetectionUseCaseException;
import com.example.examscanner.components.scan_exam.detect_corners.CornerDetectionViewModel;
import com.example.examscanner.components.scan_exam.reslove_answers.ResolveAnswersViewModel;
import com.example.examscanner.components.scan_exam.reslove_answers.RAViewModelFactory;
import com.example.examscanner.log.ESLogeerFactory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ResolveConflictedAnswersFragment extends Fragment {
    private static final String TAG = "ExamScanner";
    private static final String MSH_PREF = "ResolveConflictedAnswersFragment::";
    private CornerDetectionViewModel cdViewModel;
    private long scanId;
    private ProgressBar pb;
    private View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        scanId = ResolveConflictedAnswersFragmentArgs.fromBundle(getArguments()).getScanId();
        RAViewModelFactory factory = new RAViewModelFactory(getActivity());
        root = inflater.inflate(R.layout.fragment_resolve_one_scane, container, false);
        cdViewModel = new ViewModelProvider(requireActivity(), factory).get(CornerDetectionViewModel.class);
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(root).navigateUp();
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ViewPager2 viewPager = (ViewPager2) view.findViewById(R.id.viewPager2_conflicted_answers);
        ConflictedAnswersAdapter conflictedAnswersAdapter = null;
        try {
            conflictedAnswersAdapter=
                    new ConflictedAnswersAdapter(getActivity(), cdViewModel.getScannedCaptureById(scanId), viewPager);
        } catch (CornerDetectionUseCaseException e){
            handleError("ResolveConflictedAnswersFragment::onViewCreated", e);
            return;
        }
        viewPager.setAdapter(conflictedAnswersAdapter);
        conflictedAnswersAdapter.getPosition().observe(getActivity(), new Observer<Integer>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(Integer integer) {
                ((TextView) view.findViewById(R.id.textView_ca_current_position_feedback)).setText(
                        integer + "/" + cdViewModel.getScannedCaptureById(scanId).getValue().getConflictedAmount()
                );
            }
        });
        pb = ((ProgressBar) view.findViewById(R.id.progressBar_ra));
        pb.setVisibility(View.INVISIBLE);
        for (Button b : getChoiceButtons(view)) {
            ConflictedAnswersAdapter finalConflictedAnswersAdapter = conflictedAnswersAdapter;
            b.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View view) {
                    Choice c = getChoice(b);
                    finalConflictedAnswersAdapter.getCurrentCAResolutionSubscriber().onResolution(c);
                    waitABitAndSwipeLeft(viewPager, finalConflictedAnswersAdapter);
                    if(!cdViewModel.getScannedCaptureById(scanId).getValue().hasMoreConflictedAnswers()){
                        pb.setVisibility(View.VISIBLE);
                        Completable.fromAction(()->cdViewModel.commitResolutions(scanId))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(()->Navigation.findNavController(view).navigateUp(), t ->handleError(MSH_PREF+" onClickedResolve",t));
                    }
                }
            });
        }
    }

    @SuppressLint("CheckResult")
    private void waitABitAndSwipeLeft(ViewPager2 viewPager, ConflictedAnswersAdapter conflictedAnswersAdapter) {
        Observable.fromCallable(() -> {
                    Thread.sleep(100);
                    return "done";
                }
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        viewPager.setCurrentItem(conflictedAnswersAdapter.getPosition().getValue());
                    }
                });
    }




    private Choice getChoice(Button b) {
        switch (b.getId()) {
            case R.id.button_answer_1:
                return Choice.ONE;
            case R.id.button_answer_2:
                return Choice.TWO;
            case R.id.button_answer_3:
                return Choice.THREE;
            case R.id.button_answer_4:
                return Choice.FOUR;
            case R.id.button_answer_5:
                return Choice.FIVE;
//            case R.id.button_answer_6:
//                return Choice.SIX;
//            case R.id.button_answer_7:
//                return Choice.SEVEN;
            case R.id.button_no_answer:
                return Choice.NO_ANSWER;
            default:
                return Choice.NO_ANSWER;
        }
    }

    private List<Button> getChoiceButtons(View view) {
        List<Button> ans = new ArrayList<>();
        ans.add(view.findViewById(R.id.button_answer_1));
        ans.add(view.findViewById(R.id.button_answer_2));
        ans.add(view.findViewById(R.id.button_answer_3));
        ans.add(view.findViewById(R.id.button_answer_4));
        ans.add(view.findViewById(R.id.button_answer_5));
//        ans.add(view.findViewById(R.id.button_answer_6));
//        ans.add(view.findViewById(R.id.button_answer_7));
        ans.add(view.findViewById(R.id.button_no_answer));
        return ans;

    }

    private void handleError(String errorPerefix, Throwable t) {
        ESLogeerFactory.getInstance().log(TAG, errorPerefix, t);
        new androidx.appcompat.app.AlertDialog.Builder(getActivity())
                .setTitle("An error occured")
                .setMessage(String.format(
                        "Please capture screen and inform the software development team.\nError content:\n" +
                                "Tag: %s\n" +
                                "Error prefix: %s\n" +
                                "%s",
                        TAG,
                        errorPerefix,
                        t.toString()
                ))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Navigation.findNavController(root).navigate(
                                ResolveConflictedAnswersFragmentDirections.actionResolveConflictedAnswersFragmentToNavHome()
                        );
                    }
                })
                .show();
        t.printStackTrace();
    }
}
