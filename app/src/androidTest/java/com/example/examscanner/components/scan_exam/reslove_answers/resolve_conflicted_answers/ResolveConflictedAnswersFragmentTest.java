package com.example.examscanner.components.scan_exam.reslove_answers.resolve_conflicted_answers;

import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;

import com.example.examscanner.R;
import com.example.examscanner.components.scan_exam.reslove_answers.ResolveAnswersFragment;
import com.example.examscanner.components.scan_exam.reslove_answers.SCEmptyRepositoryFactory;
import com.example.examscanner.components.scan_exam.reslove_answers.ScannedCapturesInstancesFactory;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.examscanner.ImageProcessorsGenerator.nullIP;
import static com.example.examscanner.Utils.currentVisChild;
import static org.hamcrest.Matchers.allOf;


@Ignore("Not using")
public class ResolveConflictedAnswersFragmentTest {
    private ImageProcessingFacade imageProcessor;
    private Repository<ScannedCapture> repo;
    @Before
    public void setUp() {
        ScannedCaptureRepositoryFactory.ONLYFORTESTINGsetTestInstance(SCEmptyRepositoryFactory.create());
        imageProcessor = nullIP();
        ImageProcessingFactory.ONLYFORTESTINGsetTestInstance(imageProcessor);
        repo = new ScannedCaptureRepositoryFactory().create();
    }

    @Test
    public void testAllButtonsAndHintAreVisible() {
        repo.create(ScannedCapturesInstancesFactory.instance1(repo));
        repo.create(ScannedCapturesInstancesFactory.instance1(repo));
        FragmentScenario.launch(ResolveAnswersFragment.class);
        Bundle b = new Bundle();
        b.putInt("scanId",0);
        FragmentScenario.launchInContainer(ResolveConflictedAnswersFragment.class, b);
        onView(withText("1")).check(matches(isDisplayed()));
        onView(withText("2")).check(matches(isDisplayed()));
        onView(withText("3")).check(matches(isDisplayed()));
        onView(withText("4")).check(matches(isDisplayed()));
        onView(withText("5")).check(matches(isDisplayed()));
//        onView(withText("6")).check(matches(isDisplayed()));
//        onView(withText("7")).check(matches(isDisplayed()));
        onView(withText("No Answer")).check(matches(isDisplayed()));
    }

    @Test
    @Ignore
    public void testResolutionFeedback() {
        repo.create(ScannedCapturesInstancesFactory.instance1(repo));
        repo.create(ScannedCapturesInstancesFactory.instance1(repo));
        FragmentScenario.launch(ResolveAnswersFragment.class);
        Bundle b = new Bundle();
        b.putInt("scanId",0);
        FragmentScenario.launchInContainer(ResolveConflictedAnswersFragment.class, b);
        onView(withText("3")).perform(click());
        onView(withId(R.id.textView_resolution)).check(matches(withText("3")));
        onView(withText("2")).perform(click());
        onView(withId(R.id.textView_resolution)).check(matches(withText("2")));
        onView(withText("7")).perform(click());
        onView(withId(R.id.textView_resolution)).check(matches(withText("7")));
        onView(withText("1")).perform(click());
        onView(withId(R.id.textView_resolution)).check(matches(withText("1")));
        onView(withText("4")).perform(click());
        onView(withId(R.id.textView_resolution)).check(matches(withText("4")));
        onView(withText("6")).perform(click());
        onView(withId(R.id.textView_resolution)).check(matches(withText("6")));
        onView(withText("No Answer")).perform(click());
        onView(withId(R.id.textView_resolution)).check(matches(withText("No Answer")));
        onView(withText("5")).perform(click());
        onView(withId(R.id.textView_resolution)).check(matches(withText("5")));
    }

    @Test
    @Ignore
    public void testViewHoldersAreUpdatingIndepentendtly() {
        repo.create(ScannedCapturesInstancesFactory.instance1(repo));
        repo.create(ScannedCapturesInstancesFactory.instance1(repo));
        FragmentScenario.launch(ResolveAnswersFragment.class);
        Bundle b = new Bundle();
        b.putInt("scanId",0);
        FragmentScenario.launchInContainer(ResolveConflictedAnswersFragment.class, b);
        onView(withText("3")).perform(click());
        onView(allOf(withId(R.id.textView_resolution), isDescendantOfA(currentVisChild(withId(R.id.viewPager2_conflicted_answers))))).check(matches(withText("3")));
        onView(withId(R.id.viewPager2_conflicted_answers)).perform(swipeLeft());
        onView(withText("7")).perform(click());
        onView(allOf(withId(R.id.textView_resolution), isDescendantOfA(currentVisChild(withId(R.id.viewPager2_conflicted_answers))))).check(matches(withText("7")));
        onView(withId(R.id.viewPager2_conflicted_answers)).perform(swipeRight());
    }
}