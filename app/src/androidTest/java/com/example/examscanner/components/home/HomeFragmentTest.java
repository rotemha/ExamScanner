package com.example.examscanner.components.home;

import com.example.examscanner.R;
import com.example.examscanner.StateFullTest;
import com.example.examscanner.persistence.local.entities.Exam;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;


public class HomeFragmentTest extends StateFullTest {

    private static final int QAD_NUM_OF_QUESTIONS = 50;
    private String comp;
    private String caspl;

    @Override
    @Before
    public void setUp() {
        dbCallback = db -> {
            comp = "COMP";
            db.getExamDao().insert(new Exam(comp, 0,"2020","THE_EMPTY_URL",0,0, null,QAD_NUM_OF_QUESTIONS, null,  new String[0], 0,0,true));
            caspl = "CASPL";
            db.getExamDao().insert(new Exam(caspl, 1,"2020","THE_EMPTY_URL",0,0, null,QAD_NUM_OF_QUESTIONS, null, new String[0],0,0,true));
        };
        super.setUp();
    }

    @Test
    public void onItemClick() {
        onView(withText(containsString(comp))).perform(click());
        onView(withId(R.id.for_testing_fragment_capture_root)).check(matches(isDisplayed()));
    }
}