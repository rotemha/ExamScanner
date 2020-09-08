package com.example.examscanner.components.create_exam.intergration_with_ip;

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
    public void testOnCreatedExamItAddsToTheHomeAdapterRealIP() {
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(null);
        testOnCreatedExamItAddsToTheHomeAdapter();
    }

    @Test
    public void testOn2CreatedExamItAddsToTheHomeAdapterRealIP() {
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(null);
        testOn2CreatedExamItAddsToTheHomeAdapter();
    }
}