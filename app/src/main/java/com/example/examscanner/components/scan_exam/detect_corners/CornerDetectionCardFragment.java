package com.example.examscanner.components.scan_exam.detect_corners;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.examscanner.R;
import com.example.examscanner.log.ESLogeerFactory;


public class CornerDetectionCardFragment extends Fragment {
    private static final String TAG = "ExamScanner";
    private static final String MSG_PREF = "CornerDetectionCardFragment";
    private CornerDetectionViewModel cornerDetectionViewModel;
    //    private ProgressBarHandler progressBarHandler;
    private long captureId;
    private boolean inProgress = false;
    private ProgressBar pb;
    private View root;
    private Button resolveButton;
    private OnBackPressedCallback onBackPressedCallback;


    public void setImageView(Bitmap bm) {
        if (imageView == null) {
            return;
        }
        this.imageView.setImageBitmap(bm);
    }

    private ImageView imageView;

    public CornerDetectionCardFragment(long id) {
        this.captureId = id;
    }


    @SuppressLint("LongLogTag")
    @Override
    public void onResume() {
        super.onResume();
    }

    @SuppressLint("LongLogTag")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        cornerDetectionViewModel = new ViewModelProvider(requireActivity()).get(CornerDetectionViewModel.class);
        root = inflater.inflate(R.layout.item_corner_detected_capture, container, false);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        imageView = ((ImageView) root.findViewById(R.id.imageView2_corner_detected_capture));

        imageView.setImageBitmap(
                cornerDetectionViewModel.getScannedCaptureById(captureId).getValue().getBm()
        );


        pb = ((ProgressBar) root.findViewById(R.id.progressBar2_scanning_answers));
        if (inProgress) {
            pb.setVisibility(View.VISIBLE);
        } else {
            pb.setVisibility(View.INVISIBLE);
        }
        onBackPressedCallback = new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(root).navigateUp();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), onBackPressedCallback);
        return root;
    }

//    private void updateCDC() {
//        CornerDetectedCapture cdc = cornerDetectionViewModel.getScannedCaptureById(captureId).getValue();
//        ConstraintLayout upperLeft = (ConstraintLayout) getActivity().findViewById(R.id.constraintLayout_upper_left_container);
//        ConstraintLayout upperRight = (ConstraintLayout) getActivity().findViewById(R.id.constraintLayout_upper_right_container);
//        ConstraintLayout bottomRight = (ConstraintLayout) getActivity().findViewById(R.id.constraintLayout_bottom_right_container);
//        ConstraintLayout bottomLeft = (ConstraintLayout) getActivity().findViewById(R.id.constraintLayout_bottom_left_container);
//        ImageView iv = (ImageView) getActivity().findViewById(R.id.imageView2_corner_detected_capture);
//        float xRScale = ((float) cdc.getBitmap().getWidth()) / (((float) iv.getMeasuredWidth()));
//        float yRScale = ((float) cdc.getBitmap().getHeight()) / (((float) iv.getMeasuredHeight()));
//        cdc.setUpperLeft(
//                new Point(
//                        ((int)((upperLeft.getX() +upperLeft.getMeasuredWidth()/2)* xRScale)) ,
//                        ((int)((upperLeft.getY() +upperLeft.getMeasuredHeight()/2)*yRScale))
//                )
//        );
//        cdc.setUpperRight(
//                new Point(
//                        ((int)((upperRight.getX() +upperRight.getMeasuredWidth()/2)* xRScale)),
//                        ((int)((upperRight.getY() +upperRight.getMeasuredHeight()/2)*yRScale))
//                )
//        );
//        cdc.setBottomRight(
//                new Point(
//                        ((int)((bottomRight.getX() +bottomRight.getMeasuredWidth()/2)* xRScale)),
//                        ((int)((bottomRight.getY() +bottomRight.getMeasuredHeight()/2)*yRScale))
//                )
//        );
//        cdc.setBottomLeft(
//                new Point(
//                        ((int)((bottomLeft.getX() +bottomLeft.getMeasuredWidth()/2)* xRScale)),
//                        ((int)((bottomLeft.getY() +bottomLeft.getMeasuredHeight()/2)*yRScale))
//                )
//        );
//        cornerDetectionViewModel.getScannedCaptureById(captureId).setValue(cdc);
//        Log.d(TAG,MSG_PREF+ "::updateCDC");
//    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("LongLogTag")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        ImageView imageView = (ImageView) view.findViewById(R.id.imageView2_corner_detected_capture);
//        Drawable drawable = imageView.getDrawable();
//        Rect imageBounds = drawable.getBounds();
//        ConstraintLayout upperLeft = (ConstraintLayout) view.findViewById(R.id.constraintLayout_upper_left_container);
//        upperLeft.setOnTouchListener(new CornerPointOnTouchListener(imageBounds.left, imageBounds.right, imageBounds.top, imageBounds.bottom));
//        ConstraintLayout upperRight = (ConstraintLayout) view.findViewById(R.id.constraintLayout_upper_right_container);
//        upperRight.setOnTouchListener(new CornerPointOnTouchListener(imageBounds.left, imageBounds.right, imageBounds.top, imageBounds.bottom));
//        ConstraintLayout bottomRight = (ConstraintLayout) view.findViewById(R.id.constraintLayout_bottom_right_container);
//        bottomRight.setOnTouchListener(new CornerPointOnTouchListener(imageBounds.left, imageBounds.right, imageBounds.top, imageBounds.bottom));
//        ConstraintLayout bottomLeft = (ConstraintLayout) view.findViewById(R.id.constraintLayout_bottom_left_container);
//        bottomLeft.setOnTouchListener(new CornerPointOnTouchListener(imageBounds.left, imageBounds.right, imageBounds.top, imageBounds.bottom));
//        CornerDetectedCapture cdc = cornerDetectionViewModel.getScannedCaptureById(captureId).getValue();
//        ImageView captureImageView = (ImageView) view.findViewById(R.id.imageView2_corner_detected_capture);
//        ViewTreeObserver vto = captureImageView
//                .getViewTreeObserver();
//        ViewTreeObserver textViewTreeObserver = captureImageView.getViewTreeObserver();
//        textViewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            public void onGlobalLayout() {
//                new PointsLayingFactory().create(captureImageView).
//                        lay(upperLeft, upperRight, bottomRight, bottomLeft, cdc);
//            }
//        });

//        Observable.fromCallable(() -> cornerDetectionViewModel.getVersionNumbers())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::onVersionNumbersRetrived, this::onVersionNumbersRetrivedError);
        resolveButton = ((Button) view.findViewById(R.id.button_cd_resolve));
        if (cornerDetectionViewModel.getScannedCaptureById(captureId).getValue().hasMoreConflictedAnswers()) {
            resolveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CornerDetectionFragmentDirections.ActionCornerDetectionFragmentToResolveConflictedAnswersFragment action = CornerDetectionFragmentDirections.actionCornerDetectionFragmentToResolveConflictedAnswersFragment();
                    action.setScanId(cornerDetectionViewModel.getScannedCaptureById(captureId).getValue().getId());
                    Navigation.findNavController(view).navigate(action);
                }
            });
        } else {
            resolveButton.setVisibility(View.INVISIBLE);
            resolveButton.setEnabled(false);
        }
        if(cornerDetectionViewModel.getScannedCaptureById(captureId).getValue().isExamineeIdIsOccupied()){
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.examinee_id_duplication)
                    .setMessage(
                            String.format(
                                    "ExamScanner saved the the solution with examinee id: %s. Please resolve the conflict in the spreadsheet",
                                    cornerDetectionViewModel.getScannedCaptureById(captureId).getValue().getExamineeId()
                                    )
                    );
        }
        pb.setVisibility(View.INVISIBLE);
        onBackPressedCallback.setEnabled(true);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        int x= 1;
    }

//    private void onVersionNumbersRetrived(int[] versionNumbers) {
//        String[] versionStrings = new String[versionNumbers.length + 1];
//        String theEmptyChoice = getActivity().getString(R.string.detect_corners_the_empty_version_choice);
//        versionStrings[0] = theEmptyChoice;
//        for (int i = 1; i < versionNumbers.length + 1; i++) {
//            versionStrings[i] = new String(Integer.toString(versionNumbers[i - 1]));
//        }
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, versionStrings);
//
//        Spinner spinner = (Spinner) root.findViewById(R.id.spinner_detect_corners_version_num);
//        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String choice = (String) parent.getSelectedItem();
//                if (choice.equals(theEmptyChoice))
//                    return;
//                Integer intChoice = Integer.parseInt(choice);
//                cornerDetectionViewModel.setVersion(captureId, intChoice);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//    }
//
//    private void onVersionNumbersRetrivedError(Throwable throwable) {
//        Log.d(TAG, MSG_PREF + " onVersionNumbersRetrivedError", throwable);
//        throwable.printStackTrace();
//    }


    public void onProcessingBegun() {
        inProgress = true;
        if (pb == null) return;
        pb.setVisibility(View.VISIBLE);
    }

    //
    public void onProcessingFinished() {
        inProgress = false;
        if (pb == null) return;
        pb.setVisibility(View.INVISIBLE);
    }

    public void hideResolveButton() {
        if (resolveButton == null) return;
        ;
        resolveButton.setVisibility(View.INVISIBLE);
        resolveButton.setEnabled(false);
    }

    //
//    private class CornerPointOnTouchListener implements View.OnTouchListener {
//        float dX, dY;
//        private int absoluteLeft;
//        private int absoluteRight;
//        private int absoluteTop;
//        private int absoluteBottom;
//
//        public CornerPointOnTouchListener(int absoluteLeft, int absoluteRight, int absoluteTop, int absoluteBottom) {
//            this.absoluteLeft = absoluteLeft;
//            this.absoluteRight = absoluteRight;
//            this.absoluteTop = absoluteTop;
//            this.absoluteBottom = absoluteBottom;
//        }
//
//        @SuppressLint("LongLogTag")
//        @Override
//        public boolean onTouch(View view, MotionEvent motionEvent) {
//            switch (motionEvent.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    dX = motionEvent.getRawX();
//                    dY = motionEvent.getRawY();
//                    view.getParent().requestDisallowInterceptTouchEvent(true);
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    Log.d(TAG, "x: " + motionEvent.getRawX() + " y: " + view.getY());
//                    if (inBounaries(view.getX(), view.getY())) {
//                        view.animate()
//                                .x(view.getX() + (motionEvent.getRawX() - dX))
//                                .y(view.getY() + (motionEvent.getRawY() - dY))
//                                .setDuration(0)
//                                .start();
//                    }
//                    dX = motionEvent.getRawX();
//                    dY = motionEvent.getRawY();
//                    break;
//                default:
//                    updateCDC();
//                    return false;
//            }
//            return true;
//        }
//
//        private boolean inBounaries(float rawX, float rawY) {
//            return true;
////            Log.d(TAG, "L: "+absoluteLeft+" rawX: "+rawX+" R:"+absoluteRight+" T: "+absoluteTop+" rawY: "+" B: "+absoluteBottom);
////            return absoluteLeft <= rawX && rawX<= absoluteRight && absoluteTop<= rawY && rawY <=absoluteBottom;
//        }
//    }
//
//
//    private abstract class VersionNumberWatcher implements TextWatcher {
//
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            onVersionReceived(Integer.valueOf(s.toString()));
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//
//        }
//
//        public abstract void onVersionReceived(int verNum);
//    }
//
//    private class PointsLayingFactory {
//        public PointsLayingStrategy create(ImageView iv) {
//            if (iv.getScaleType() == ImageView.ScaleType.FIT_XY) {
//                return new AdjustingPointsLayingStrategy(iv);
//            } else {
//                return new NotAdjustingPointsLayingStrategy(iv);
//            }
//        }
//    }
//
//    private abstract class PointsLayingStrategy {
//        protected ImageView iv;
//
//        public PointsLayingStrategy(ImageView iv) {
//            this.iv = iv;
//        }
//
//        public abstract void lay(View upperLeft, View upperRight, View bottomRight, View bottomLeft, CornerDetectedCapture cdc);
//
//        protected void layPoint(View v, int x, int y) {
//            v.animate().x(x - v.getMeasuredWidth() / 2).y(y - v.getMeasuredHeight() / 2).setDuration(0).start();
//        }
//    }
//
//    private class NotAdjustingPointsLayingStrategy extends PointsLayingStrategy {
//        public NotAdjustingPointsLayingStrategy(ImageView iv) {
//            super(iv);
//        }
//
//        @Override
//        public void lay(View upperLeft, View upperRight, View bottomRight, View bottomLeft, CornerDetectedCapture cdc) {
//            float xScale = ((float) iv.getMeasuredWidth()) / ((float) cdc.getBitmap().getWidth());
//            int delta = (iv.getMeasuredHeight() - cdc.getBitmap().getHeight()) / 2;
//            layPoint(upperRight, (int) (cdc.getUpperLeft().x * xScale), (int) (cdc.getUpperLeft().y + delta));
//            layPoint(upperLeft, (int) (cdc.getUpperRight().x * xScale), (int) (cdc.getUpperRight().y + delta));
//            layPoint(bottomRight, (int) (cdc.getBottomRight().x * xScale), (int) (cdc.getBottomRight().y + delta));
//            layPoint(bottomLeft, (int) (cdc.getBottomLeft().x * xScale), (int) (cdc.getBottomLeft().y + delta));
//        }
//    }
//
//    private class AdjustingPointsLayingStrategy extends PointsLayingStrategy {
//        public AdjustingPointsLayingStrategy(ImageView iv) {
//            super(iv);
//        }
//
//        @Override
//        public void lay(View upperLeft, View upperRight, View bottomRight, View bottomLeft, CornerDetectedCapture cdc) {
//            float xScale = (((float) iv.getMeasuredWidth()) / ((float) cdc.getBitmap().getWidth()));
//            float yScale = (((float) iv.getMeasuredHeight()) / ((float) cdc.getBitmap().getHeight()));
////            layPoint(upperRight, (int)(cdc.getUpperLeft().x*xScale), (int)(cdc.getUpperLeft().y*yScale));
////            layPoint(upperLeft, (int)(cdc.getUpperRight().x*xScale), (int)(cdc.getUpperRight().y*yScale));
////            layPoint(bottomLeft, (int)(cdc.getBottomLeft().x*xScale), (int)(cdc.getBottomLeft().y*yScale));
////            upperRight.setVisibility(View.INVISIBLE);
////            upperLeft.setVisibility(View.INVISIBLE);
////            bottomLeft.setVisibility(View.INVISIBLE);
//            layPoint(bottomRight, (int) (cdc.getBottomRight().x * xScale), (int) (cdc.getBottomRight().y * yScale));
//            layPoint(upperRight, (int) (cdc.getUpperRight().x * xScale), (int) (cdc.getUpperRight().y * yScale));
//            layPoint(upperLeft, (int) (cdc.getUpperLeft().x * xScale), (int) (cdc.getUpperLeft().y * yScale));
//            layPoint(bottomLeft, (int) (cdc.getBottomLeft().x * xScale), (int) (cdc.getBottomLeft().y * yScale));
//
//        }
//    }
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
                                CornerDetectionFragmentDirections.actionCornerDetectionFragmentToNavHome()
                        );
                    }
                })
                .show();
        t.printStackTrace();
    }
}
