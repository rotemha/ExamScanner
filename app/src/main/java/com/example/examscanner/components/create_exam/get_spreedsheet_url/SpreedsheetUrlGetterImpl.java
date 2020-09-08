package com.example.examscanner.components.create_exam.get_spreedsheet_url;

import android.content.Intent;
import android.net.Uri;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

class SpreedsheetUrlGetterImpl implements SpreedsheetUrlGetter {
    @Override
    public void get(Fragment activity, int pickfileRequestCode) {
        Intent intent = new Intent(Intent. ACTION_OPEN_DOCUMENT );
        intent.setType("application/vnd.google-apps.spreadsheet");
        activity.startActivityForResult(intent, pickfileRequestCode);
    }

    @Override
    public String accessUrl(Intent data, FragmentActivity activity) {
        return data.getData().getPath();
    }
}
