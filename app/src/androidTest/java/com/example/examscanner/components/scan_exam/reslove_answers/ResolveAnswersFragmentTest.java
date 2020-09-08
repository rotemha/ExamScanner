package com.example.examscanner.components.scan_exam.reslove_answers;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.examscanner.R;
import com.example.examscanner.StateFullTest;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.examscanner.ImageProcessorsGenerator.nullIP;

@Ignore("NOT USING")
@RunWith(AndroidJUnit4.class)
public class ResolveAnswersFragmentTest extends StateFullTest {
    private ImageProcessingFacade imageProcessor;
    private Repository<ScannedCapture> repo;
    @Before
    public void setUp() {
        ScannedCaptureRepositoryFactory.ONLYFORTESTINGsetTestInstance(SCEmptyRepositoryFactory.create());
        imageProcessor = nullIP();
        repo = new ScannedCaptureRepositoryFactory().create();
    }
    @Test
    public void test34CHecked4Conflicted15MissingVisible() {
        repo.create(ScannedCapturesInstancesFactory.instance1(repo));
        repo.create(ScannedCapturesInstancesFactory.instance1(repo));
        FragmentScenario.launchInContainer(ResolveAnswersFragment.class);
        onView(withText("Checked: 34")).check(matches(isDisplayed()));
        onView(withText("Conflicted: 4")).check(matches(isDisplayed()));
        onView(withText("Missing: 15")).check(matches(isDisplayed()));
    }

    @Test
    public void testSwiping() {
        repo.create(ScannedCapturesInstancesFactory.instance1(repo));
        repo.create(ScannedCapturesInstancesFactory.instance1(repo));
        FragmentScenario.launchInContainer(ResolveAnswersFragment.class);
        onView(withText("1/2")).check(matches(isDisplayed()));
        onView(withId(R.id.viewPager2_scanned_captures)).perform(ViewActions.swipeLeft());
        onView(withText("2/2")).check(matches(isDisplayed()));
        onView(withId(R.id.viewPager2_scanned_captures)).perform(ViewActions.swipeRight());
        onView(withText("1/2")).check(matches(isDisplayed()));
    }
}