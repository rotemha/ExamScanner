package com.example.examscanner.components.create_exam;

import android.content.Intent;
import android.graphics.Bitmap;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.testing.FragmentScenario;

import com.example.examscanner.R;
import com.example.examscanner.Utils;
import com.example.examscanner.components.create_exam.get_version_file.VersionImageGetter;
import com.example.examscanner.components.create_exam.get_version_file.VersionImageGetterFactory;
import com.example.examscanner.components.scan_exam.AbstractComponentInstrumentedTest;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.repositories.grader.Grader;
import com.example.examscanner.repositories.grader.GraderRepoFactory;

import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

public abstract class CreateExamFragmentAbstractTest extends AbstractComponentInstrumentedTest {

    protected FragmentScenario<CreateExamFragment> scenraio;

    @Override
    public void setUp() {
        super.setUp();
        VersionImageGetterFactory.setStubInstance(new VersionImageGetter() {
            @Override
            public void get(Fragment fragment, int pickfileRequestCode) {}

            @Override
            public Bitmap accessBitmap(Intent data, FragmentActivity activity) {
                return BitmapsInstancesFactoryAndroidTest.getTestJpg1Marked();
            }
        });
    }

    protected void testAddVersion() {
        onView(withId(R.id.textView_number_of_versions_added)).check(matches(withText("0")));
        onView(withId(R.id.button_create_exam_add_version)).check(matches(not(isEnabled())));
        onView(withId(R.id.button_create_exam_upload_version_image)).perform(click());
        scenraio.onFragment(f ->
                f.onActivityResult(0,0,null)
        );
        onView(withId(R.id.button_create_exam_add_version)).check(matches(not(isEnabled())));
        onView(withId(R.id.editText_create_exam_version_number)).perform(replaceText("20"));
        onView(withId(R.id.editText_create_exam_num_of_questions)).perform(replaceText("50"));
        onView(withId(R.id.button_create_exam_add_version)).perform(click());
        Utils.sleepScanAnswersTime();
        Utils.sleepScanAnswersTime();
        onView(withId(R.id.textView_number_of_versions_added)).check(matches(withText("1")));
    }
}
