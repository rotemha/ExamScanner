package com.example.examscanner.components.scan_exam.detect_corners;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCapture;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;

import java.util.ArrayList;
import java.util.List;

public class CornerDetectionCapturesAdapter extends FragmentStateAdapter {

    private static final String TAG = "ExamScanner";
    private ArrayList<CornerDetectionCardData> cards;
    private MutableLiveData<Integer> mPosition;//Based 0!!!!
    private MutableLiveData<Integer> mItemCount;
    private ViewPager2 viewPager2;
    private int size;



    public CornerDetectionCapturesAdapter(FragmentActivity activity, @NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<MutableLiveData<ScannedCapture>> scannedCaptures, ViewPager2 viewPager2) {
        super(fragmentManager, lifecycle);
        cards = new ArrayList<>();
        for (MutableLiveData<ScannedCapture> mSc: scannedCaptures) {
            CornerDetectionCardFragment f = new CornerDetectionCardFragment(mSc.getValue().getId());
            cards.add(
                    new CornerDetectionCardData(
                            mSc.getValue().getId(),
                            f
                    )
            );
            mSc.observe(activity, new Observer<ScannedCapture>() {
                @Override
                public void onChanged(ScannedCapture scannedCapture) {
                    if(mSc.getValue().hasUpdatedFeedbackImage()){
                        f.setImageView(mSc.getValue().getBm());
                    }
                    if(!mSc.getValue().hasMoreConflictedAnswers()){
                        f.hideResolveButton();
                    }
                }
            });
        }
        this.viewPager2 = viewPager2;
        this.mPosition = new MutableLiveData<>(0);
        this.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                CornerDetectionCapturesAdapter.this.mPosition.setValue(position);
            }
        });
        mItemCount = new MutableLiveData<>(getItemCount());
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return cards.get(position).getCdcFragCard();
    }


    public void handleProcessFinish(long cdcId) {
        CornerDetectionCardData card = getCardByCDCid(cdcId);
        card.getCdcFragCard().onProcessingFinished();
        int removingPosition = cards.indexOf(card);
        cards.remove(removingPosition);
        notifyItemRemoved(removingPosition);
        mPosition.setValue(viewPager2.getCurrentItem());
        mItemCount.setValue(mItemCount.getValue()-1);
    }

    @Override
    public long getItemId(int position) {
        return cards.get(position).getId();
    }

    @Override
    public boolean containsItem(long id) {
        boolean ans = false;
        for (CornerDetectionCardData card:cards) {
            ans|=card.getId()==id;
        }
        return ans;
    }

    private CornerDetectionCardData getCardByCDCid(long cdcId) {
        for (CornerDetectionCardData card:cards) {
            if(card.getId()==cdcId)
                return card;
        }
        return null;
    }


    @Override
    public int getItemCount() {
        return cards.size();
    }

    public LiveData<Integer> getmItemCount() {
        return mItemCount;
    }

    public LiveData<Integer> getPosition() {
        return mPosition;
    }

    public static String captureId() {
        return "captureId";
    }

    public long getCDCaptureIdInPosition(int postition) {
        return cards.get(postition).cdcId;
    }

    public void notifyProcessBegun(int position) {
        cards.get(position).getCdcFragCard().onProcessingBegun();
    }

    public boolean noMoreItems() {
        return cards.size()==0;
    }


    private class CornerDetectionCardData {
        private CornerDetectionCardFragment cdcFragCard;
        private long cdcId;

        public CornerDetectionCardData(long cdcId, CornerDetectionCardFragment cdcFragCard) {
            this.cdcId = cdcId;
            this.cdcFragCard = cdcFragCard;
        }

        public long getId() {
            return cdcId;
        }

        public CornerDetectionCardFragment getCdcFragCard() {
            return cdcFragCard;
        }
    }

//    public class ProgressBarHandler implements com.example.examscanner.components.scan_exam.detect_corners.ProgressBarHandler {
//        private View view;
//
//        public boolean isInProgress() {
//            return inProgress;
//        }
//
//        public boolean inProgress = false;
//
//
//        @Override
//        public void onProcessingBegun() {
//            inProgress =true;
//            ((ProgressBar) view.findViewById(R.id.progressBar2_scanning_answers)).setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        public void onProcessingFinished() {
//            inProgress =false;
//            ((ProgressBar) view.findViewById(R.id.progressBar2_scanning_answers)).setVisibility(View.INVISIBLE);
//        }
//
//        @Override
//        public void setContextView(View view) {
//            this.view = view;
//        }
//
//    }


}
