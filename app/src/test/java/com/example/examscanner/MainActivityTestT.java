package com.example.examscanner;

import android.util.Log;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.examscanner.image_processing.ImageProcessingFactory;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.robolectric.RobolectricTestRunner;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTestT {
    private static final String TAG = "MainActivityTestT";
    @Rule
    public ActivityScenarioRule<MainActivity> mainActivityScenarioRule =
            new ActivityScenarioRule<MainActivity>(MainActivity.class);
    private BaseLoaderCallback mLoaderCallback;

    @Before
    public void setUp() throws Exception {
        mainActivityScenarioRule.getScenario().onActivity(activity -> {
            mLoaderCallback = new BaseLoaderCallback(activity) {
                @Override
                public void onManagerConnected(int status) {
                    switch (status) {
                        case LoaderCallbackInterface.SUCCESS: {
                            Log.i("MainActivity", "OpenCV loaded successfully");
                        }
                        break;
                        default: {
                            super.onManagerConnected(status);
                        }
                        break;
                    }
                }
            };
        });

        mainActivityScenarioRule.getScenario().onActivity(activity -> {
            if (!OpenCVLoader.initDebug()) {
                Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
                OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, activity, mLoaderCallback);
            } else {
                Log.d(TAG, "OpenCV library found inside package. Using it!");
                mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
            }
        });
    }

    @Test
    public void testAfterLoginHomeIsDisplayed() {
//        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
        onView(withId(R.id.button_login)).perform(click());
        onView(withId(R.id.home)).check(matches(isDisplayed()));
    }
}