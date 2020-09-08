package com.example.examscanner;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(
        {
//                com.example.examscanner.MainActivityTest.class,
//                com.example.examscanner.components.scan_exam.capture_and_detect_corners.unit.CaptureAndDetectCornersIntegrationTest.class,
                com.example.examscanner.components.scan_exam.detect_corners_and_resolve_ans.unit.DetectCornersAndResolveAnswersTest.class,
        }
)

public class UIStableTestSuite {
}

// Runs all unit tests.


