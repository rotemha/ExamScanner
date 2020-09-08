package com.example.examscanner.components.scan_exam.capture;

import com.example.examscanner.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.examscanner.Utils.sleepSingleCaptureProcessingTime;

public class CaptureUtils {
    public static void assertUserSeeProgress(int processed, int outOf) {
        onView(withText(xOutOfY(processed,outOf))).check(matches(isDisplayed()));
    }
    public static void takeTwoCaptures(){
        onView(withId(R.id.capture_image_button)).perform(click());
        sleepSingleCaptureProcessingTime();
        onView(withId(R.id.capture_image_button)).perform(click());
        sleepSingleCaptureProcessingTime();
    }
    private static String xOutOfY(int x, int y){
        return Integer.toString(x) +"/"+Integer.toString(y);
    }
}
