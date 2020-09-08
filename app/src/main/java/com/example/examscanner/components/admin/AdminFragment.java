package com.example.examscanner.components.admin;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.examscanner.R;
import com.example.examscanner.components.scan_exam.capture.CaptureFragmentDirections;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AdminFragment extends Fragment {

    private static final String TAG = "ExamScanner";
    private Button delete;
    private ProgressBar pb;
    private AdminViewModel viewModel;
    private View root;
    private OnBackPressedCallback onBackPressedCallback;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_admin_page, container, false);
        onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(root).navigateUp();
            }
        };
        delete = (Button) root.findViewById(R.id.button_admin_page_delete);
        pb = (ProgressBar) root.findViewById(R.id.progressBar_admin);
        delete.setVisibility(View.INVISIBLE);
        pb.setVisibility(View.VISIBLE);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Completable.fromAction(this::createViewModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onModelViewCreated, this::onModelViewCreatedError);
    }

    private void onModelViewCreatedError(Throwable throwable) {
        handleError("onModelViewCreatedError", throwable);
    }

    private void onModelViewCreated() {
        delete.setOnClickListener(this::delete);
        pb.setVisibility(View.INVISIBLE);
        delete.setVisibility(View.VISIBLE);
    }

    @SuppressLint("CheckResult")
    private void delete(View view) {
        pb.setVisibility(View.VISIBLE);
        Completable.fromAction(() -> viewModel.delete())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {new AlertDialog.Builder(getActivity())
                                .setTitle(R.string.admin_exam_deleted)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Navigation.findNavController(root).navigate(AdminFragmentDirections.actionAdminPageToNavHome());
                                    }
                                })
                                .show();},
                        t -> handleError("delete", t)
                );
    }

    private void createViewModel() {
        AdminViewModelFactory factory = new AdminViewModelFactory(AdminFragmentArgs.fromBundle(getArguments()).getExamId());
        viewModel = new ViewModelProvider(this, factory).get(AdminViewModel.class);
    }

    private void handleError(String errorPerefix, Throwable t) {
        Log.d(TAG, errorPerefix, t);
        try {
            new AlertDialog.Builder(getActivity())
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
                                    AdminFragmentDirections.actionAdminPageToNavHome()
                            );
                        }
                    })
                    .show();
        } catch (Exception e) {
            Log.d(TAG, "Espresso issues");
        }
        t.printStackTrace();
    }
}
