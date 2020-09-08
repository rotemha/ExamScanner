package com.example.examscanner.components.create_exam;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;


import com.example.examscanner.R;
import com.example.examscanner.components.create_exam.get_version_file.VersionImageGetter;
import com.example.examscanner.components.create_exam.get_version_file.VersionImageGetterFactory;
import com.example.examscanner.log.ESLogeerFactory;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class CreateExamFragment extends Fragment {
    public static final int PICKFILE_REQUEST_CODE = 0;
    private static final int MY_PERMISSIONS_REQUEST_READ_WRITE_FILES = 1;
    private static final int PICKSPREADSHEET_REQUEST_CODE = 2;
    private static final String DEBUG_TAG = "DebugExamScanner";
    private static String MSG_PREF = "CreateExamFragment::";
    private static String TAG = "ExamScanner";
    private CreateExamModelView viewModel;
    private VersionImageGetter versionImageGetter;
    private View root;
    private EditText versionNumEdit;
    //    private SpreedsheetUrlGetter spreedsheetUrlGetter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_create_exam, container, false);
        versionImageGetter = new VersionImageGetterFactory().create();
//        spreedsheetUrlGetter = SpreedsheetUrlGetterFactory.get();
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(root).navigateUp();
            }
        });
        return root;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ProgressBar pb = ((ProgressBar) view.findViewById(R.id.progressBar_create_exam));
        pb.setVisibility(View.VISIBLE);
        ((Button) getActivity().findViewById(R.id.button_create_exam_add_version)).setEnabled(false);
        ((Button) getActivity().findViewById(R.id.button_create_exam_add_greader)).setEnabled(false);
        ((Button) getActivity().findViewById(R.id.button_create_exam_choose_spreadsheet)).setEnabled(false);
        Completable.fromAction(this::createModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onModelCreated, this::onModelCreatedError);

        final Button createExamButton = (Button) view.findViewById(R.id.button_create_exam_create);
        createExamButton.setEnabled(false);
        createExamButton.setOnClickListener(new CreateClickListener());
        ((Button) view.findViewById(R.id.button_create_exam_upload_version_image)).setOnClickListener(this::onChooseVersionPdfClick);
        ((Button) view.findViewById(R.id.button_create_exam_choose_spreadsheet)).setOnClickListener(this::onChooseSpreadsheet);
        versionNumEdit = ((EditText) view.findViewById(R.id.editText_create_exam_version_number));
//        versionNumEdit.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(s.length()==0){
//                    return;
//                }
//                viewModel.holdVersionNumber(Integer.valueOf(s.toString()));
//                refreshAddVersionButton();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
        versionNumEdit.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    ESLogeerFactory.getInstance().log(DEBUG_TAG, "versionNumEdit.setOnKeyListener");
                    consumeVersionNumIfNotEmpty();
                    refreshAddVersionButton();
                    return true;
                }
                return false;
            }
        });
        versionNumEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ESLogeerFactory.getInstance().log(DEBUG_TAG, "versionNumEdit.setOnFocusChangeListener");
                if(!hasFocus){
                    ESLogeerFactory.getInstance().log(DEBUG_TAG, "versionNumEdit.setOnFocusChangeListener && !hasFocus");
                    consumeVersionNumIfNotEmpty();
                }
            }
        });
        ((EditText) view.findViewById(R.id.editText_create_exam_spreadsheet_url)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.holdExamUrl(s.toString());
                refreshSpreadsheetButton();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        ((EditText) view.findViewById(R.id.editText_create_exam_grader_address)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.holdGraderIdentifier(s.toString());
                refreshAddGraderButton();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        ((Button) view.findViewById(R.id.button_create_exam_add_version)).setOnClickListener(this::onAddVersion);
        ((Button) view.findViewById(R.id.button_create_exam_add_greader)).setOnClickListener(this::onAddGrader);
        askPemissionsLoop();
    }

    protected void consumeVersionNumIfNotEmpty() {
        if(viewModel.getCurrentVersionNumber()!=null || versionNumEdit.getText() == null || versionNumEdit.getText().toString().length() ==0){
            return;
        }
        final String examineeID = versionNumEdit.getText().toString();
        viewModel.holdVersionNumber(Integer.valueOf(examineeID));
    }

    private void refreshSpreadsheetButton() {
        ((Button) getActivity().findViewById(R.id.button_create_exam_choose_spreadsheet)).setEnabled(
                viewModel.hasExamUrl()
        );
    }

    private void refreshAddGraderButton() {
        ((Button) getActivity().findViewById(R.id.button_create_exam_add_greader)).setEnabled(
                viewModel.hasGraderIdnetifier()
        );
    }

    private void askPemissionsLoop() {
        if (!allPermissionsGranted()) {
            requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_WRITE_FILES
            );
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
                } else {

                }
                return;
            }
        }
    }

    private boolean allPermissionsGranted() {
        return ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED;
    }

    private void refreshCreateExamButton() {
        ((Button) getActivity().findViewById(R.id.button_create_exam_create)).setEnabled(
                viewModel.getAddedVersions().getValue() > 0 &&
                        ((TextView) getActivity().findViewById(R.id.editText_create_exam_course_name)).getText() != null &&
                        ((TextView) getActivity().findViewById(R.id.editText_create_exam_year)).getText() != null &&
                        ((RadioGroup) getActivity().findViewById(R.id.radioGroup_semester)).getCheckedRadioButtonId() != -1 &&
                        viewModel.hasExamUrl() &&
                        ((RadioGroup) getActivity().findViewById(R.id.radioGroup_term)).getCheckedRadioButtonId() != -1

        );
    }

    private void refreshAddVersionButton() {
        ((Button) getActivity().findViewById(R.id.button_create_exam_add_version)).setEnabled(
                viewModel.getCurrentVersionNumber() != null && viewModel.getCurrentVersionBitmap() != null &&
                        viewModel.getNumOfQuestions() != 0
        );
    }

    private void onChooseSpreadsheet(View view) {
        Toast.makeText(
                getActivity(),
                String.format("url %s added succefully", viewModel.getExamUrl()),
                Toast.LENGTH_SHORT
        );
        refreshCreateExamButton();
    }

    public void onChooseVersionPdfClick(View v) {
        versionImageGetter.get(this, PICKFILE_REQUEST_CODE);
    }

    public void onAddVersion(View v) {
        ((ProgressBar) getActivity().findViewById(R.id.progressBar_create_exam)).setVisibility(View.VISIBLE);
        viewModel.holdNumOfQuestions(((EditText) (getActivity()
                .findViewById(R.id.editText_create_exam_num_of_questions))).getText().toString());
        Completable.fromAction(() -> viewModel.addVersion())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onVersionAdded, this::onVersionAddedFailed);
    }

    private void onAddGrader(View view) {
        ((ProgressBar) getActivity().findViewById(R.id.progressBar_create_exam)).setVisibility(View.VISIBLE);
//        viewModel.holdGraderIdentifier(((EditText) getActivity().findViewById(R.id.editText_create_exam_grader_address)).getText().toString());
        Completable.fromAction(() -> viewModel.addGrader())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGraderAdded, this::onGraderAddedFailed);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICKFILE_REQUEST_CODE) {
            //create pdf document object from bytes
            ((ProgressBar) getActivity().findViewById(R.id.progressBar_create_exam)).setVisibility(View.VISIBLE);
            Bitmap bitmap = versionImageGetter.accessBitmap(data, getActivity());
            viewModel.holdVersionBitmap(bitmap);
            ((ImageView) getActivity().findViewById(R.id.imageView_create_exam_curr_version_img)).setImageBitmap(bitmap);
            ((ProgressBar) getActivity().findViewById(R.id.progressBar_create_exam)).setVisibility(View.INVISIBLE);
            consumeVersionNumIfNotEmpty();
            refreshAddVersionButton();
        }
    }

    private void onVersionAddedFailed(Throwable throwable) {
        ESLogeerFactory.getInstance().log(TAG, MSG_PREF, throwable);
        throwable.printStackTrace();
        ((ProgressBar) getActivity().findViewById(R.id.progressBar_create_exam)).setVisibility(View.INVISIBLE);
    }

    private interface Continuation { void cont();}
    private void onVersionAdded() {
        Continuation successCont = ()->{
            ((ImageView) getActivity().findViewById(R.id.imageView_create_exam_curr_version_img)).clearAnimation();
            ((EditText) getActivity().findViewById(R.id.editText_create_exam_version_number)).getText().clear();
            viewModel.holdVersionNumber(null);
            viewModel.holdVersionBitmap(null);
            ((ImageView) getActivity().findViewById(R.id.imageView_create_exam_curr_version_img)).setImageResource(0);
            refreshAddVersionButton();
            viewModel.incNumOfVersions();
            refreshCreateExamButton();
        };
        ((ProgressBar) this.getActivity().findViewById(R.id.progressBar_create_exam)).setVisibility(View.INVISIBLE);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View customLayout = getActivity().getLayoutInflater().inflate(R.layout.scan_answers_feedback, null);
        builder.setView(customLayout);
        ((ImageView)customLayout.findViewById(R.id.imageView_scan_answers_feedback)).setImageBitmap(viewModel.getVersionFeedbackImag());
        builder.setTitle(R.string.create_exam_dialog_version_scanned_result)
                .setPositiveButton(R.string.create_exam_version_scanned_dialog_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        successCont.cont();
                    }
                })
                .setNegativeButton(R.string.create_exam_version_scanned_dialog_try_again, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((EditText) getActivity().findViewById(R.id.editText_create_exam_version_number)).getText().clear();
                        viewModel.holdVersionBitmap(null);
                        refreshAddVersionButton();
                        versionImageGetter.get(CreateExamFragment.this, PICKFILE_REQUEST_CODE);
                    }
                });
        try {
            AlertDialog dialog = builder.create();
            dialog.show();
        }catch (Exception e){
            ESLogeerFactory.getInstance().log(TAG, "PROBABLY espresso wtupid stuff "+MSG_PREF, e);
            successCont.cont();
        }
    }
//    public void showAlertDialogButtonClicked(View view) {
//        // create an alert builder
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle(R.string.create_exam_dialog_version_scanned_succesffuly);
//        // set the custom layout
//        final View customLayout = getLayoutInflater().inflate(R.layout.create_exam_add_version_feedback, null);
//        builder.setView(customLayout);
//        // add a button
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // send data from the AlertDialog to the Activity
//                EditText editText = customLayout.findViewById(R.id.editText);
//                sendDialogDataToActivity(editText.getText().toString());
//            }
//        });
//        // create and show the alert dialog
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }
//    // do something with the data coming from the AlertDialog
//    private void sendDialogDataToActivity(String data) {
//        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
//    }

    private void onGraderAdded() {
        ((TextView) getActivity().findViewById(R.id.editText_create_exam_grader_address)).setText("");
        ((TextView) getActivity().findViewById(R.id.textView_create_exam_added_grader_feedback)).setText(String.format("added %s", viewModel.getCurrentGrader()));
        viewModel.holdGraderIdentifier(null);
        ((ProgressBar) getActivity().findViewById(R.id.progressBar_create_exam)).setVisibility(View.INVISIBLE);
    }

    private void onGraderAddedFailed(Throwable throwable) {
        ESLogeerFactory.getInstance().log(TAG, MSG_PREF, throwable);
        CharSequence text = String.format("Failed adding grader, are you sure %s is a correct username?", viewModel.getCurrentGrader());
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(getActivity(), text, duration).show();
    }

    private void onModelCreatedError(Throwable throwable) {
        handleError(MSG_PREF+" onModelCreatedError", throwable);
    }

    private void handleError(String errorPerefix, Throwable t){
        ESLogeerFactory.getInstance().log(TAG, errorPerefix, t);
        try {
            new AlertDialog.Builder(getActivity())
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
                                    CreateExamFragmentDirections.actionCreateExamFragmentToNavHome()
                            );
                        }
                    })
                    .show();
        }catch (Exception e){
            ESLogeerFactory.getInstance().log(TAG, "Espressoissues", t);
        }
        t.printStackTrace();
    }


    private void createModel() {
        CEViewModelFactory factory = new CEViewModelFactory(this.getActivity());
        viewModel = new ViewModelProvider(this, factory).get(CreateExamModelView.class);
    }

    private void onModelCreated() {
        ProgressBar pb = getActivity().findViewById(R.id.progressBar_create_exam);
        pb.setVisibility(View.INVISIBLE);
        viewModel.getAddedVersions().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                ((TextView) getActivity().findViewById(R.id.textView_number_of_versions_added)).setText(integer.toString());
            }
        });
    }

    public void onExamCreated() {
        getActivity().findViewById(R.id.progressBar_create_exam).setVisibility(View.INVISIBLE);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.create_exam_dialog_exam_is_beeing_uploaded)
                .setPositiveButton(R.string.create_exam_dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavHostFragment.findNavController(CreateExamFragment.this).popBackStack();
                    }
                });
        AlertDialog dialog = builder.create();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        dialog.show();
    }


    private void onExamCreatedError(Throwable throwable) {
        handleError(MSG_PREF + "onExamCreatedError", throwable);
    }

    private class CreateClickListener implements View.OnClickListener {
        private final EditText courseName;
        private final RadioGroup semester;
        private final RadioGroup term;
        private final EditText year;
        private ProgressBar pb;
        private TextWatcher textWatcher;
        private RadioGroup.OnCheckedChangeListener radioButtonListner;

        public CreateClickListener() {
            this.courseName = getActivity().findViewById(R.id.editText_create_exam_course_name);
            this.semester = getActivity().findViewById(R.id.radioGroup_semester);
            this.term = getActivity().findViewById(R.id.radioGroup_term);
            this.year = getActivity().findViewById(R.id.editText_create_exam_year);
            pb = getActivity().findViewById(R.id.progressBar_create_exam);
            radioButtonListner = new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    refreshAddVersionButton();
                }
            };
            textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    refreshCreateExamButton();
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            };
            courseName.addTextChangedListener(textWatcher);
            year.addTextChangedListener(textWatcher);
            semester.setOnCheckedChangeListener(radioButtonListner);
            term.setOnCheckedChangeListener(radioButtonListner);
        }


        @SuppressLint("CheckResult")
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onClick(View v) {
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            String termString = getSelectedRadioButtonString(term);
            String semesterString = getSelectedRadioButtonString(semester);
            pb.setVisibility(View.VISIBLE);
            Completable.fromAction(() -> create(courseName.getText().toString(), termString, semesterString, year.getText().toString()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(CreateExamFragment.this::onExamCreated, CreateExamFragment.this::onExamCreatedError);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void create(String courseName, String term, String semester, String year) {
            viewModel.create(courseName, term, semester, year);
        }

        @NotNull
        private String getSelectedRadioButtonString(RadioGroup rg) {
            return ((RadioButton) CreateExamFragment.this.getActivity().findViewById(rg.getCheckedRadioButtonId())).getText().toString();
        }
    }


}
