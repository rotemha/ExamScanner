package com.example.examscanner.components.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examscanner.R;
import com.example.examscanner.authentication.state.State;
import com.example.examscanner.log.ESLogeerFactory;
import com.example.examscanner.repositories.exam.Exam;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<LiveData<Exam>> mExams;
    private HomeFragment.OnItemClick onItemClick;
    private HomeFragment.onAdminPagelicked onAdminPagelicked;
    private State state;

    private static final String TAG = "MyAdapter";

    public MyAdapter(List<LiveData<Exam>> exams, HomeFragment.OnItemClick onItemClick, HomeFragment.onAdminPagelicked onAdminPagelicked, State state) {
        this.mExams = exams;
        this.onItemClick = onItemClick;
        this.onAdminPagelicked = onAdminPagelicked;
        this.state = state;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exam_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ESLogeerFactory.getInstance().log(TAG, "onBindViewHolder: called");
        Exam e = mExams.get(position).getValue();
        holder.examName.setText(e.toString());
        if (e.getManagerId().equals(state.getId())) {
            holder.edit.setVisibility(View.VISIBLE);
            holder.edit.setOnClickListener(v -> {
                if (e.isUploaded()) {
                    onAdminPagelicked.onAdminPageClicked(v, e);
                }
            });
        } else {
            holder.edit.setVisibility(View.INVISIBLE);
        }
        if (!e.isUploaded()) {
            holder.pb.setVisibility(View.VISIBLE);
            holder.itemView.setOnClickListener(v->{});
            e.observeUpload()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> {
                                holder.pb.setVisibility(View.INVISIBLE);
                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        onItemClick.onItemClick(e);
                                    }
                                });
                            },
                            t -> ESLogeerFactory.getInstance().log(TAG, "BUG IN Exam:observeDownload", t)
                    );
        } else if (!e.isDownloaded()) {
            holder.pb.setVisibility(View.VISIBLE);
            holder.itemView.setOnClickListener(v->{});
            e.observeDownload()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> {
                                holder.pb.setVisibility(View.INVISIBLE);
                                holder.itemView.setOnClickListener(v->onItemClick.onItemClick(e));
                            },
                            t -> ESLogeerFactory.getInstance().log(TAG, "BUG IN Exam:observeDownload", t)
                    );

        } else {
            holder.pb.setVisibility(View.INVISIBLE);
            holder.itemView.setOnClickListener(v->onItemClick.onItemClick(e));
        }

    }

    @Override
    public int getItemCount() {
        return mExams.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView examName;
        ConstraintLayout parentLayout;
        Button edit;
        ProgressBar pb;

        public MyViewHolder(ConstraintLayout v) {
            super(v);
            examName = v.findViewById(R.id.exam_name);
            parentLayout = v;
            edit = v.findViewById(R.id.button_home_admin);
            pb = v.findViewById(R.id.progressBar_home_exam_is_uploded);
        }
    }


}
