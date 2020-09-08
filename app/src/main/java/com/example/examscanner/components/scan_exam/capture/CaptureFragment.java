package com.example.examscanner.components.scan_exam.capture;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.examscanner.R;
import com.example.examscanner.components.scan_exam.capture.camera.CameraManager;
import com.example.examscanner.components.scan_exam.capture.camera.CameraMangerFactory;
import com.example.examscanner.components.scan_exam.capture.camera.CameraOutputHander;
import com.example.examscanner.log.ESLogeerFactory;

import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class CaptureFragment extends Fragment {
    private static final String TAG = "ExamScanner";
    private static final String MSG_PREF = "CaptureFragment::";
    private static final String DEBUG_TAG = "DebugExamScanner";
    private CameraManager cameraManager;
    private CaptureViewModel captureViewModel;
    private final CompositeDisposable processRequestDisposableContainer = new CompositeDisposable();
    private CameraOutputHander outputHander;
    private ProgressBar pb;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    private View.OnClickListener captureClickListener;
    private AtomicInteger inProgress;
    private ImageButton imageButton;
    private View root;
    private EditText examineeEditText;
    private TextView captureProgressEditText;
    private Spinner versionSpinner;
    private String theEmptyChoice;
    private OnBackPressedCallback onBackPressedCallback;
    private Button nextStepButton;
    private TextView viewById;
    //    private Msg2BitmapMapper m2bmMapper;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_capture, container, false);
        cameraManager = new CameraMangerFactory(
                getActivity(),
                root
        ).create();
        pb = (ProgressBar) root.findViewById(R.id.progressBar_capture);
        inProgress = new AtomicInteger(0);
        captureProgressEditText = (TextView) root.findViewById(R.id.capture_processing_progress);
        theEmptyChoice = getActivity().getString(R.string.capture_the_empty_version_choice);
        onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(root).navigateUp();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), onBackPressedCallback);
        nextStepButton = (Button) root.findViewById(R.id.button_move_to_detect_corners);
        nextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CaptureFragmentDirections.ActionCaptureFragmentToCornerDetectionFragment action =
                        CaptureFragmentDirections.actionCaptureFragmentToCornerDetectionFragment();
                action.setExamId(CaptureFragmentArgs.fromBundle(getArguments()).getExamId());
                Navigation.findNavController(view).navigate(action);
            }
        });
        lockWindow();
        requestCamera();
        viewById = (TextView) root.findViewById(R.id.textView_capture_last_eid_checked);
        viewById.setText("");
        return root;
    }
    private void lockWindow(){
        pb.setVisibility(View.VISIBLE);
        onBackPressedCallback.setEnabled(false);
        nextStepButton.setEnabled(false);
        nextStepButton.setVisibility(View.INVISIBLE);
    }

    private void unlockWindow(){
        pb.setVisibility(View.INVISIBLE);
        onBackPressedCallback.setEnabled(true);
        nextStepButton.setEnabled(true);
        nextStepButton.setVisibility(
                captureViewModel.thereAreScannedCaptures()? View.VISIBLE: View.INVISIBLE
        );
    }

    @SuppressLint({"WrongViewCast", "RestrictedApi"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        captureProgressEditText.setVisibility(View.INVISIBLE);
        ((ProgressBar) getActivity().findViewById(R.id.progressBar_capture_scanning)).setVisibility(View.INVISIBLE);
        ((ProgressBar) view.findViewById(R.id.progressBar_capture)).setVisibility(View.VISIBLE);
//        processRequestDisposableContainer.add()

        Completable.fromAction(this::createViewModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onViewModelCreated, this::onViewModelCreatedError);

    }

    private void onViewModelCreatedError(Throwable throwable) {
        unlockWindow();
        handleError(MSG_PREF + "onViewModelCreatedError", throwable);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onViewModelCreated() {
        ESLogeerFactory.getInstance().log(TAG, "onViewModelCreated");
        captureViewModel.getNumOfTotalCaptures().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer totalCaptures) {
                if(captureProgressEditText == null ||getActivity().findViewById(R.id.button_move_to_detect_corners) == null) return;
                final Integer processedCaptures = captureViewModel.getNumOfProcessedCaptures().getValue();
                captureProgressEditText.setText(processedCaptures + "/" + totalCaptures);
                captureProgressEditText.setVisibility(totalCaptures > 0 ? View.VISIBLE : View.INVISIBLE);
                ((Button) getActivity().findViewById(R.id.button_move_to_detect_corners))
                        .setVisibility(totalCaptures > 0 && processedCaptures > 0 ? View.VISIBLE : View.INVISIBLE);
            }
        });
        captureViewModel.getNumOfProcessedCaptures().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer processedCaptures) {
                if(getActivity().findViewById(R.id.button_move_to_detect_corners) == null) return;
                final TextView viewById = (TextView) getActivity().findViewById(R.id.capture_processing_progress);
                viewById.setText(processedCaptures + "/" + captureViewModel.getNumOfTotalCaptures().getValue());
                ((Button) getActivity().findViewById(R.id.button_move_to_detect_corners))
                        .setVisibility(captureViewModel.thereAreScannedCaptures()||processedCaptures > 0 ? View.VISIBLE : View.INVISIBLE);
            }
        });
        outputHander =
                new CameraOutputHandlerImpl(
                        captureViewModel,
                        processRequestDisposableContainer,
                        () -> {
                            ((ProgressBar) getActivity().findViewById(R.id.progressBar_capture_scanning)).setVisibility(View.VISIBLE);
                            inProgress.set(inProgress.intValue() + 1);
                            ((EditText) getActivity().findViewById(R.id.editText_capture_examineeId)).getText().clear();
                            onVersionNumbersRetrived(captureViewModel.getVersionNumbers());
                        },
                        () -> {
                            if (inProgress.decrementAndGet() == 0) {
                                if(root== null) return;
                                final ProgressBar pb = (ProgressBar) root.findViewById(R.id.progressBar_capture_scanning);
                                if(pb == null) return;
                                pb.setVisibility(View.INVISIBLE);
                            }
                        },
                        (pref, t) -> handleError(pref, t)
                );
        captureClickListener = cameraManager.createCaptureClickListener(outputHander);
        imageButton = (ImageButton) getActivity().findViewById(R.id.capture_image_button);
        examineeEditText = (EditText) getActivity().findViewById(R.id.editText_capture_examineeId);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!captureViewModel.isHoldingValidExamineeId() || (examineeEditText.getText().length()==0)){
                    ESLogeerFactory.getInstance().log(DEBUG_TAG, "from Capture fragment "+ !captureViewModel.isHoldingValidExamineeId() + " _ " + (examineeEditText.getText().length()==0));
                    ESLogeerFactory.getInstance().log(DEBUG_TAG, "from Capture fragment 2"+ examineeEditText.getText().toString());
                    if(!consumeExamineeIdOrHandleIfInvalid(examineeEditText.getText().toString())){
                        return;
                    }
                }
                if(!captureViewModel.isHoldingVersion() && versionIsChosen()){
                    ESLogeerFactory.getInstance().log(DEBUG_TAG, "from Capture fragment version "+ !captureViewModel.isHoldingVersion() + " _ " + versionIsChosen());
                    if(!consumeVersionIdOrHandleIfInvalid(versionSpinner.getSelectedItem().toString())){
                        return;
                    }
                }

                if (isValidExamineeIdAndVersion()) {
                    captureClickListener.onClick(v);
                } else {
                    Toast.makeText(getActivity(), "Please enter examinee id and verion number", Toast.LENGTH_LONG).show();
                }
            }
        });
//        captureViewModel.getCurrentExamineeId().observe(getActivity(), new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                imageButton.setEnabled(isValidExamineeIdAndVersion());
//            }
//        });
//        captureViewModel.getCurrentVersion().observe(getActivity(), new Observer<Version>() {
//            @Override
//            public void onChanged(Version v) {
//                imageButton.setEnabled(isValidExamineeIdAndVersion());
//            }
//        });
//        Observable.fromCallable(() -> captureViewModel.getVersionNumbers())

//        examineeEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                final String examineeID = s.toString();
//                if(!captureViewModel.isUniqueExamineeId(examineeID)){
//                    handleNotUniqueExamineeId(examineeID);
//                }else{
//                    captureViewModel.setExamineeId(examineeID);
//                }
//            }
//            @Override
//            public void afterTextChanged(Editable s) {}
//        });
//        examineeEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
//                        actionId == EditorInfo.IME_ACTION_DONE ||
//                        event != null &&
//                                event.getAction() == KeyEvent.ACTION_DOWN &&
//                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
//                    if (event == null || !event.isShiftPressed()) {
//                        final String examineeID = examineeEditText.getText().toString();
//                        if(!captureViewModel.isUniqueExamineeId(examineeID)){
//                            handleNotUniqueExamineeId(examineeID);
//                        }else{
//                            captureViewModel.setExamineeId(examineeID);
//                        }
//                        return true; // consume.
//                    }
//                }
//                return false; // pass on to other listeners.
//            }
//        });
//        examineeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                // When focus is lost check that the text field has valid values.
//
//                if (!hasFocus) {
//                    final String examineeID = examineeEditText.getText().toString();
//                    if (!captureViewModel.isUniqueExamineeId(examineeID)) {
//                        handleNotUniqueExamineeId(examineeID);
//                    } else {
//                        captureViewModel.setExamineeId(examineeID);
//                    }
//                }
//            }
//        });
        examineeEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    ESLogeerFactory.getInstance().log(DEBUG_TAG, "done predd on examinee id edittext");
                    final String examineeID = examineeEditText.getText().toString();
                    consumeExamineeIdOrHandleIfInvalid(examineeID);
                    return true;
                }
                return false;
            }
        });
        String examineeId = CaptureFragmentArgs.fromBundle(getArguments()).getExamineeId();
//        long version =  CaptureFragmentArgs.fromBundle(getArguments()).getVersionId();
//        spinner.set
//        if(examineeId!= null && version != -1){
        if (examineeId != null && !examineeId.equals("null")) {
            captureViewModel.unconsumeExamineeId(examineeId);
            ((EditText) getActivity().findViewById(R.id.editText_capture_examineeId)).
                    setText(examineeId);
        }
        pb.setVisibility(View.VISIBLE);
        captureViewModel.getLastExamineeId().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                updateLastCheckedExaminee(s);
            }
        });
        updateLastCheckedExaminee(captureViewModel.getLastExamineeId().getValue());
        Observable.fromCallable(() -> captureViewModel.getVersionNumbers())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onVersionNumbersRetrived, this::onVersionNumbersRetrivedError);
    }

    protected void updateLastCheckedExaminee(String s) {
        if(s == null || s.equals("") ){
            return;
        }else{
            viewById.setText(String.format("Last examinee checked: %s", s));
        }
    }

    private boolean consumeVersionIdOrHandleIfInvalid(String toString) {
        if (toString.equals(theEmptyChoice)){
            Toast.makeText(getActivity(), "Please choose version", Toast.LENGTH_LONG).show();
            return false;
        }
        Integer intChoice = Integer.parseInt(toString);
        captureViewModel.setVersion(intChoice);
        return true;
    }

    private boolean versionIsChosen() {
        return versionSpinner.getSelectedItem() !=null && !((String)versionSpinner.getSelectedItem()).equals(theEmptyChoice);
    }

    //returns true iff valid examinee id
    protected boolean consumeExamineeIdOrHandleIfInvalid(String examineeID) {
        if (!captureViewModel.isUniqueExamineeId(examineeID)) {
            handleNotUniqueExamineeId(examineeID);
            return false;
        } else {
            captureViewModel.setExamineeId(examineeID);
            return true;
        }
    }

    private void handleNotUniqueExamineeId(String examineeID) {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.examinee_id_duplication)
                .setMessage(String.format("Someone already checked %s", examineeID))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final EditText editText = (EditText) getActivity().findViewById(R.id.editText_capture_examineeId);
                        editText.getText().clear();
                        editText.requestFocus();

                    }
                }).show();
    }

    private boolean isValidExamineeIdAndVersion() {
        return captureViewModel.isValidVersion() && captureViewModel.isValidExamineeId();
    }

    private void createViewModel() {
        CaptureViewModelFactory factory = new CaptureViewModelFactory(
                getActivity(),
                CaptureFragmentArgs.fromBundle(getArguments()).getSessionId(),
                CaptureFragmentArgs.fromBundle(getArguments()).getExamId()
        );
        captureViewModel = ViewModelProviders.of(this, factory).get(CaptureViewModel.class);
        captureViewModel.refresh();
    }

    private void requestCamera() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA)) {
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

            }
        } else {
            cameraManager.setUp();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            cameraManager.setUp();
        }
        int x = 1;
        x += 1;
        System.out.println(x);
    }

    @Override
    public void onDestroyView() {
        ESLogeerFactory.getInstance().log(TAG, "CaptureFragment::onDestroy");
        cameraManager.onDestroy();
        super.onDestroyView();
    }



    @Override
    public void onPause() {
        ESLogeerFactory.getInstance().log(TAG, "CaptureFragment::onPause");
        super.onPause();
//        cameraManager.onPause();
    }

    private class ProgressBarOnClickListenerDecorator implements View.OnClickListener {
        private ProgressBar pb;
        private View.OnClickListener complicatedCalculation;

        public ProgressBarOnClickListenerDecorator(ProgressBar pb, View.OnClickListener complicatedCalculation) {
            this.pb = pb;
            this.complicatedCalculation = complicatedCalculation;
        }

        @Override
        public void onClick(View v) {
            complicatedCalculation.onClick(v);
        }
    }

    private void onVersionNumbersRetrived(int[] versionNumbers) {
        String[] versionStrings = new String[versionNumbers.length + 1];
        versionStrings[0] = theEmptyChoice;
        for (int i = 1; i < versionNumbers.length + 1; i++) {
            versionStrings[i] = new String(Integer.toString(versionNumbers[i - 1]));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, versionStrings);

        if(root == null || (Spinner) root.findViewById(R.id.spinner_capture_version_num) == null){
            return;
        }
        versionSpinner = (Spinner) root.findViewById(R.id.spinner_capture_version_num);
        versionSpinner.setAdapter(adapter);
        versionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String choice = (String) parent.getSelectedItem();
                if (choice.equals(theEmptyChoice))
                    return;
                Integer intChoice = Integer.parseInt(choice);
                captureViewModel.setVersion(intChoice);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        unlockWindow();
        ESLogeerFactory.getInstance().logmem();
    }

    private void onVersionNumbersRetrivedError(Throwable throwable) {
        unlockWindow();
        handleError(MSG_PREF + " onVersionNumbersRetrivedError", throwable);
    }

    private void handleError(String errorPerefix, Throwable t) {
        ESLogeerFactory.getInstance().log(TAG, errorPerefix, t);
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
                                    CaptureFragmentDirections.actionCaptureFragmentToNavHome()
                            );
                        }
                    })
                    .show();
        } catch (Exception e) {
            ESLogeerFactory.getInstance().log(TAG, "Espresso issues",e);
        }
        t.printStackTrace();
    }

}
