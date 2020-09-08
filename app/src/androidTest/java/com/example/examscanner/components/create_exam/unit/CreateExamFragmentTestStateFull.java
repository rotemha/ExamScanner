package com.example.examscanner.components.create_exam.unit;

import android.content.Intent;
import android.graphics.Bitmap;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.examscanner.R;
import com.example.examscanner.StateFullTest;
import com.example.examscanner.Utils;
import com.example.examscanner.components.create_exam.CreateExamFragmentAbstractTestStateFull;
import com.example.examscanner.components.create_exam.get_version_file.VersionImageGetter;
import com.example.examscanner.components.create_exam.get_version_file.VersionImageGetterFactory;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.image_processing.ImageProcessingFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.examscanner.ImageProcessorsGenerator.fakeIP;
import static org.hamcrest.Matchers.containsString;

public class CreateExamFragmentTestStateFull extends CreateExamFragmentAbstractTestStateFull {



    @Test
    public void testOnCreatedExamItAddsToTheHomeAdapterStubIP() {
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(fakeIP());
        testOnCreatedExamItAddsToTheHomeAdapter();
    }

    @Test
    public void testOn2CreatedExamItAddsToTheHomeAdapterStubIP() {
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(fakeIP());
        testOn2CreatedExamItAddsToTheHomeAdapter();
    }

    @Test
    public void testOn2CreatedExamItAddsToTheHomeAdapterRealIP() {
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(null);
        VersionImageGetterFactory.setStubInstance(new VersionImageGetter() {
            @Override
            public void get(Fragment fragment, int pickfileRequestCode) {
                f = fragment;
            }

            @Override
            public Bitmap accessBitmap(Intent data, FragmentActivity activity) {
                return BitmapsInstancesFactoryAndroidTest.getComp191_V1_pdf_ins_in1();
            }
        });
        testOn2CreatedExamItAddsToTheHomeAdapter();
    }

    @Test
    public void testOnCreatedExamWithAGraderItAddsToTheGraderHomeapater() {
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(fakeIP());
        testOnCreatedExamItAddsToTheHomeAdapter();
    }
}