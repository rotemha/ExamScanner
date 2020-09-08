package com.example.examscanner.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.examscanner.R;
import com.example.examscanner.persistence.local.AppDatabaseFactory;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        Button button = (Button)root.findViewById(R.id.button_scan_exam);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                GalleryFragmentDirections.ActionNavGalleryToCaptureFragment2 action = GalleryFragmentDirections.actionNavGalleryToCaptureFragment2();
//                action.setSessionId(-1);
//                Navigation.findNavController(view).navigate(action);
//                startActivity(new Intent(getActivity(), ScanExamActivity.class));
            }
        });

        ((Button)root.findViewById(R.id.button_resolve_answers)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Navigation.findNavController(view).navigate(R.id.action_nav_gallery_to_fragment_resolve_answers);
//                startActivity(new Intent(getActivity(), ScanExamActivity.class));
            }
        });
        ((Button)root.findViewById(R.id.button_clear_all_tables)).setOnClickListener(
                v -> AppDatabaseFactory.getInstance().clearAllTables()
        );
        ((Button)root.findViewById(R.id.button_galley_logout)).setOnClickListener(this::logout);
        ((Button)root.findViewById(R.id.button_crash)).setOnClickListener(v -> {throw new RuntimeException("Force Crash");});
        return root;
    }

    private void logout(View view) {
        AuthUI.getInstance()
                .signOut(this.getContext())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }
}