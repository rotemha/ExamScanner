package com.example.examscanner.components.scan_exam.capture;

import com.example.examscanner.StateFullTest;

import org.junit.Before;
import org.junit.Ignore;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.examscanner.Utils.navFromHomeToCaptureQuickAndDirty;

@Ignore("Empty")
public class CaptureFragmentStatefull extends StateFullTest {
    @Override
    @Before
    public void setUp() {
        super.setUp();
        navFromHomeToCaptureQuickAndDirty();
    }
}
