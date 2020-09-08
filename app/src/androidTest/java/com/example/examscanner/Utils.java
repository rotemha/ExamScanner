package com.example.examscanner;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ActivityScenario;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;

public class Utils {

    private static final String TAG = "UTILS";

    public static void loadOpenCV(FragmentScenario scenraio) {
        final BaseLoaderCallback[] mLoaderCallback = new BaseLoaderCallback[1];
        scenraio.onFragment(fragment -> {
            mLoaderCallback[0] = new BaseLoaderCallback(fragment.getActivity()) {
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

        scenraio.onFragment(fragment -> {
            if (!OpenCVLoader.initDebug()) {
                Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
                OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, fragment.getActivity(), mLoaderCallback[0]);
            } else {
                Log.d(TAG, "OpenCV library found inside package. Using it!");
                mLoaderCallback[0].onManagerConnected(LoaderCallbackInterface.SUCCESS);
            }
        });
    }
//    public static void loadOpenCV(ActivityScenario<FragmentScenario.EmptyFragmentActivity> empAc) {
//        final BaseLoaderCallback[] mLoaderCallback = new BaseLoaderCallback[1];
//        empAc.onActivity(activity -> {
//            mLoaderCallback[0] = new BaseLoaderCallback(activity) {
//                @Override
//                public void onManagerConnected(int status) {
//                    switch (status) {
//                        case LoaderCallbackInterface.SUCCESS: {
//                            Log.i("MainActivity", "OpenCV loaded successfully");
//                        }
//                        break;
//                        default: {
//                            super.onManagerConnected(status);
//                        }
//                        break;
//                    }
//                }
//            };
//        });
//
//        empAc.onActivity(activity -> {
//            if (!OpenCVLoader.initDebug()) {
//                Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
//                OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, activity, mLoaderCallback[0]);
//            } else {
//                Log.d(TAG, "OpenCV library found inside package. Using it!");
//                mLoaderCallback[0].onManagerConnected(LoaderCallbackInterface.SUCCESS);
//            }
//        });
//    }
    public static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new TypeSafeMatcher<View>() {
            int currentIndex = 0;

            @Override
            public void describeTo(Description description) {
                description.appendText("with index: ");
                description.appendValue(index);
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                return matcher.matches(view) && currentIndex++ == index;
            }
        };
    }
    public static void navFromHomeToCaptureQuickAndDirty() {
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
        onView(withText(R.string.gallery_button_alt)).perform(click());
        onView(withText(R.string.start_scan_exam)).perform(click());
    }

    public static Matcher<View> currentVisChild(final Matcher<View> parentMatcher) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with first child view of type parentMatcher");
            }

            @Override
            public boolean matchesSafely(View view) {

                if (!(view.getParent() instanceof ViewGroup)) {
                    return parentMatcher.matches(view.getParent());
                }
                ViewGroup group = (ViewGroup) view.getParent();
                return parentMatcher.matches(view.getParent()) && group.getChildAt(0).equals(view);

            }
        };
    }
    public static void navFromHomeToDetecteCornersQuickAndDirty() {
        navFromHomeToCaptureQuickAndDirty();
        onView(withId(R.id.button_move_to_detect_corners)).perform(click());
    }

//    public static void navFromHomeToResolveAnswers() {
//        navFromHomeToDetecteCornersQuickAndDirty();
//        onView(withId(R.id.button_cd_nav_to_resolve_answers)).perform(click());
//    }
    public static void sleepCameraPreviewSetupTime() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepSwipingTime() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepSingleCaptureTakingTime() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void sleepSingleCaptureProcessingTime() {
        try {
            Thread.sleep(7*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void sleepMovingFromCaptureToDetectCorners() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepScreenRotationTime(){
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepRectangleTransformationTime(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepScanAnswersTime(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepCDFragmentSetupTime() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepAlertPoppingTime() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void createExamTime() {
        try {
            Thread.sleep(20*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepABit() {
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
