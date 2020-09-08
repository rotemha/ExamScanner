package com.example.examscanner.components.scan_exam.detect_corners;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.widget.ViewPager2;

import com.example.examscanner.R;
import com.example.examscanner.log.ESLogeerFactory;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCapture;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

import static android.view.View.VISIBLE;

public class CornerDetectionFragment extends Fragment {
    private static boolean DEBUG = false;
    private static final String TAG = "ExamScanner";
    private static final String MSG_PREF = "CornerDetectionFragment::";
    private CornerDetectionViewModel cornerDetectionViewModel;
    private CornerDetectionCapturesAdapter cornerDetectionCapturesAdapter;
    private ViewPager2 viewPager;
    private final CompositeDisposable processRequestDisposableContainer = new CompositeDisposable();
    private View root;
    private OnBackPressedCallback onBackPressedCallback;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_corner_detection, container, false);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        onBackPressedCallback = new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {
                clearViewModel();
                Navigation.findNavController(root).navigateUp();
                ESLogeerFactory.getInstance().logmem();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), onBackPressedCallback);
        ESLogeerFactory.getInstance().logmem();
        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        setHasOptionsMenu(true);

        requireActivity()
                .getOnBackPressedDispatcher()
                .addCallback(new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        ESLogeerFactory.getInstance().log(TAG, "CornerDetectionFragment::onAttach");
                    }
                });
    {

        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        ESLogeerFactory.getInstance().log(TAG, "CornerDetectionFragment::onOptionsItemSelected");
        if (item.getItemId() == android.R.id.home) {
            ESLogeerFactory.getInstance().log(TAG, "hope it's overriding the back pressed button");
            clearViewModel();
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
            return true; // must return true to consume it here;

        }
        return super.onOptionsItemSelected(item);
    }

    private void clearViewModel() {
        ESLogeerFactory.getInstance().log(TAG, "CornerDetectionFragment::clearViewModel");
        if(cornerDetectionViewModel!=null){
            cornerDetectionViewModel.clear();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((ProgressBar) view.findViewById(R.id.progressBar_detect_corners)).setVisibility(VISIBLE);
        Completable.fromAction(this::createViewModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onViewModelCreated, this::onViewModelCreatedError);
        viewPager = (ViewPager2) view.findViewById(R.id.viewPager2_corner_detected_captures);
//        ((Button) view.findViewById(R.id.button_cd_nav_to_resolve_answers)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavDirections action =CornerDetectionFragmentDirections.actionCornerDetectionFragmentToFragmentResolveAnswers();
//                Navigation.findNavController(view).navigate(action);
//            }
//        });
        final Button approveButton = (Button) view.findViewById(R.id.button_cd_approve);
        final Button retakeButton = (Button) view.findViewById(R.id.button_cd_retake);
        approveButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                int adapterBasedOPosition = cornerDetectionCapturesAdapter.getPosition().getValue();
                long cdcId = cornerDetectionCapturesAdapter.getCDCaptureIdInPosition(adapterBasedOPosition);
                ScannedCapture sc = cornerDetectionViewModel.getScannedCaptureById(cdcId).getValue();
                cornerDetectionCapturesAdapter.notifyProcessBegun(adapterBasedOPosition);
                processRequestDisposableContainer.add(generateApprovalCompletable(sc));
                ESLogeerFactory.getInstance().logmem();
//                waitABitAndSwipeLeft(viewPager, cornerDetectionCapturesAdapter);
            }
        });
        retakeButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                int adapterBasedOPosition = cornerDetectionCapturesAdapter.getPosition().getValue();
                long cdcId = cornerDetectionCapturesAdapter.getCDCaptureIdInPosition(adapterBasedOPosition);
                ScannedCapture sc = cornerDetectionViewModel.getScannedCaptureById(cdcId).getValue();
                cornerDetectionCapturesAdapter.notifyProcessBegun(adapterBasedOPosition);
                String examineeId = sc.getExamineeId();
                long versionId = sc.getVersion().getId();
                Completable.fromAction(() -> cornerDetectionViewModel.delete(sc)).subscribeOn(Schedulers.io()).subscribe(()->{}, t ->{
                    ESLogeerFactory.getInstance().log(TAG, "delete scanned capture", t);});
                clearViewModel();
                ESLogeerFactory.getInstance().logmem();
//                waitABitAndSwipeLeft(viewPager, cornerDetectionCapturesAdapter);
                CornerDetectionFragmentDirections.ActionCornerDetectionFragmentToCaptureFragment2 action = CornerDetectionFragmentDirections.actionCornerDetectionFragmentToCaptureFragment2();
                action.setExamId(cornerDetectionViewModel.getExamId());
                action.setExamineeId(examineeId);
                action.setVersionId(versionId);
                Navigation.findNavController(view).navigate(action);
            }
        });

    }

    private void askToChooseVersion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.detect_corners_please_choose_version)
                .setPositiveButton(R.string.detect_corners_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void onViewModelCreated() {
        cornerDetectionCapturesAdapter =
                new CornerDetectionCapturesAdapter(
                        getActivity(),
                        getActivity().getSupportFragmentManager(),
                        getActivity().getLifecycle(),
                        cornerDetectionViewModel.getPreProcessedCDCs(), viewPager);
        viewPager.setAdapter(cornerDetectionCapturesAdapter);

        cornerDetectionCapturesAdapter.getPosition().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                ((TextView) getActivity().findViewById(R.id.textView_cd_current_position)).setText(
                        (integer + 1) + "/" + cornerDetectionCapturesAdapter.getmItemCount().getValue()
                );
            }
        });
        cornerDetectionCapturesAdapter.getmItemCount().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                ((TextView) getActivity().findViewById(R.id.textView_cd_current_position)).setText(
                        (cornerDetectionCapturesAdapter.getPosition().getValue() + 1) + "/" + integer
                );
            }
        });

//        cornerDetectionViewModel.getNumberOfScannedCaptures().observe(getActivity(), new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer integer) {
//                final TextView processProgress = (TextView) getActivity().findViewById(R.id.textView_cd_processing_progress);
//                final Integer inScanning = cornerDetectionViewModel.getNumberOfCDCaptures().getValue();
//                processProgress.setText(
//                        integer + "/" + inScanning
//                );
//                processProgress.setVisibility(inScanning > 0 ? VISIBLE : View.INVISIBLE);
//                ((Button) getActivity().findViewById(R.id.button_cd_nav_to_resolve_answers))
//                        .setVisibility(inScanning > 0 && integer>0 ? VISIBLE : View.INVISIBLE);
//            }
//        });
        ((ProgressBar) getActivity().findViewById(R.id.progressBar_detect_corners)).setVisibility(View.INVISIBLE);
        onBackPressedCallback.setEnabled(true);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    private boolean currentCardIsReadyForProcessing(CornerDetectedCapture cdc) {
        return cdc.hasVersion() && cornerDetectionCapturesAdapter.getItemId(cornerDetectionCapturesAdapter.getPosition().getValue()) == cdc.getId();
    }

    private void onViewModelCreatedError(Throwable throwable) {
        handleError(MSG_PREF+ "onViewModelCreatedError", throwable);
    }

    private void createViewModel() {
        CDViewModelFactory factory =
                new CDViewModelFactory(
                        getActivity(),
                        CornerDetectionFragmentArgs.fromBundle(getArguments()).getExamId()
                );
        cornerDetectionViewModel = new ViewModelProvider(requireActivity(), factory).get(CornerDetectionViewModel.class);
        cornerDetectionViewModel.refresh();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NotNull
    private DisposableCompletableObserver generateApprovalCompletable(ScannedCapture sc) {
        return Completable.fromCallable(() -> {
//            cornerDetectionViewModel.align(cdc);
//            /* TODO : replace -1 with version num */
//            cornerDetectionViewModel.scanAnswers(cdc);
            ESLogeerFactory.getInstance().log(TAG, String.format("approving %d scanned capture", sc.getId()), new Exception());
            cornerDetectionViewModel.approve(sc);
 //           cornerDetectionViewModel.remove(sc);
            return "Done";
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
//                        cornerDetectionViewModel.postProcessTransformAndScanAnswers(cdc.getId());
                        cornerDetectionCapturesAdapter.handleProcessFinish(sc.getId());
                        if (cornerDetectionCapturesAdapter.noMoreItems()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder
                                    .setTitle(R.string.done_batch)
                                    .setPositiveButton(R.string.stage_back_to_home, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            NavDirections action =CornerDetectionFragmentDirections.actionCornerDetectionFragmentToNavHome();
                                            Navigation.findNavController(root).navigate(action);
                                        }
                                    }).setNegativeButton(R.string.stage_start_another_batch, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    CornerDetectionFragmentDirections.ActionCornerDetectionFragmentToCaptureFragment2 action =CornerDetectionFragmentDirections.actionCornerDetectionFragmentToCaptureFragment2();
                                    action.setExamId(cornerDetectionViewModel.getExamId());
                                    Navigation.findNavController(root).navigate(action);
                                }
                            });
                            builder.create().show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        handleError(MSG_PREF + "::generateCaptureScanningCompletable", e);
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void waitABitAndSwipeLeft(ViewPager2 viewPager, CornerDetectionCapturesAdapter adapter) {
        Observable.fromCallable(() -> {
                    Thread.sleep(200);
                    return "done";
                }
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        viewPager.setCurrentItem(adapter.getPosition().getValue() + 1);
                    }
                });
    }

    private void handleError(String errorPerefix, Throwable t){
        onBackPressedCallback.setEnabled(true);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        ESLogeerFactory.getInstance().log(TAG, errorPerefix, t);
        new androidx.appcompat.app.AlertDialog.Builder(getActivity())
                .setTitle("An error occured")
                .setMessage(String.format(
                        "Please capture screen and inform the software development team.\nError content:\n" +
                                "Tag: %s\n"+
                                "Error prefix: %s\n"+
                                "%s",
                        TAG,
                        errorPerefix,
                        t.toString()
                ))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Navigation.findNavController(root).navigate(
                                CornerDetectionFragmentDirections.actionCornerDetectionFragmentToNavHome()
                        );
                    }
                })
                .show();
        t.printStackTrace();
    }


}
