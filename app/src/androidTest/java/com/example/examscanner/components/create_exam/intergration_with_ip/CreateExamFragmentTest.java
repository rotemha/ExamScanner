package com.example.examscanner.components.create_exam.intergration_with_ip;

import android.content.Intent;
import android.graphics.Bitmap;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.testing.FragmentScenario;

import com.example.examscanner.R;
import com.example.examscanner.Utils;
import com.example.examscanner.components.create_exam.CreateExamFragment;
import com.example.examscanner.components.create_exam.CreateExamFragmentAbstractTest;
import com.example.examscanner.components.create_exam.get_version_file.VersionImageGetter;
import com.example.examscanner.components.create_exam.get_version_file.VersionImageGetterFactory;
import com.example.examscanner.components.create_exam.view_model.CreateExamModelViewAbstractTest;
import com.example.examscanner.components.scan_exam.AbstractComponentInstrumentedTest;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.image_processing.ImageProcessingFactory;

import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.examscanner.ImageProcessorsGenerator.fakeIP;
import static com.example.examscanner.Utils.loadOpenCV;
import static org.hamcrest.core.IsNot.not;

public class CreateExamFragmentTest extends CreateExamFragmentAbstractTest {


    @Test
    public void testAddVersionRealIP() {
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(null);
        scenraio = FragmentScenario.launchInContainer(CreateExamFragment.class);
        loadOpenCV(scenraio);
        testAddVersion();
    }
}
