package com.example.examscanner.components.create_exam;

import androidx.fragment.app.Fragment;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.examscanner.R;
import com.example.examscanner.Utils;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.StringContains.containsString;

public class CreateExamUtils {
    public static void createExam(String comp, String year, String verNum, String aGraderAdress, ActivityScenarioRule activityScenarioRule, Fragment f) {
        onView(withId(R.id.editText_create_exam_course_name)).perform(replaceText(comp));
        onView(withId(R.id.radioButton_create_exam_term_b)).perform(click());
        onView(withId(R.id.radioButton_create_exam_semester_fall)).perform(click());
        onView(withId(R.id.editText_create_exam_year)).perform(replaceText(year));
        onView(withId(R.id.editText_create_exam_version_number)).perform(replaceText(verNum));
        onView(withId(R.id.button_create_exam_upload_version_image)).perform(click());
        activityScenarioRule.getScenario().onActivity(a->f.onActivityResult(0,0,null));
        onView(withId(R.id.button_create_exam_add_version)).perform(click());
        Utils.sleepScanAnswersTime();
        onView(withId(R.id.editText_create_exam_grader_address)).perform(replaceText(aGraderAdress));
        onView(withId(R.id.textView_number_of_versions_added)).check(matches(withText("1")));
        onView(withId(R.id.button_create_exam_upload_version_image)).perform(click());
        onView(withId(R.id.editText_create_exam_version_number)).perform(replaceText("20"));
        onView(withId(R.id.button_create_exam_add_version)).perform(click());
        onView(withId(R.id.button_create_exam_create)).perform(click());
        Utils.sleepAlertPoppingTime();
        onView(withText(R.string.create_exam_dialog_ok)).perform(click());
    }

    public static void checkExamExists(String comp, String year) {
        onView(withText(containsString(comp))).check(matches(isDisplayed()));
        onView(withText(containsString(year))).check(matches(isDisplayed()));
    }
}
