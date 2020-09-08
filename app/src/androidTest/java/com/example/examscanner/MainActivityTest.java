package com.example.examscanner;

import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class MainActivityTest extends StateFullTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testAfterLoginHomeIsDisplayed() {
        onView(withId(R.id.home)).check(matches(isDisplayed()));
    }

    @Test
    public void testCrash() {
        onView(withId(R.id.home)).check(matches(isDisplayed()));
    }
}