package com.example.examscanner.components.scan_exam;

import android.os.Bundle;
import android.util.Log;

import androidx.test.espresso.core.internal.deps.guava.collect.Lists;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.examscanner.MainActivity;
import com.example.examscanner.authentication.UIAuthenticationHandlerFactory;
import com.example.examscanner.communication.CommunicationFacadeFactory;
import com.example.examscanner.communication.RealFacadeImple;
import com.example.examscanner.persistence.local.AppDatabase;
import com.example.examscanner.persistence.local.AppDatabaseFactory;
import com.example.examscanner.persistence.local.files_management.FilesManagerFactory;
import com.example.examscanner.persistence.remote.FirebaseDatabaseFactory;
import com.example.examscanner.persistence.remote.RemoteDatabaseFacadeFactory;
import com.example.examscanner.repositories.corner_detected_capture.CDCRepositoryFacrory;
import com.example.examscanner.repositories.corner_detected_capture.CornerDetectedCapture;
import com.example.examscanner.repositories.exam.ExamRepositoryFactory;
import com.example.examscanner.repositories.grader.GraderRepoFactory;
import com.example.examscanner.repositories.scanned_capture.ScannedCapture;
import com.example.examscanner.repositories.scanned_capture.ScannedCaptureRepositoryFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public abstract class AbstractComponentInstrumentedTest {
    private final String DEBUG_TAG = "DebugExamScanner";
    protected AppDatabase db;
    protected DBCallback dbCallback = (theDb -> {
    });
    protected Runnable setupCallback = ()->{};
    public static boolean USINIG_REAL_DB = false;


    public static Bundle getBundle(){
        MainActivity.testMode=true;
        Bundle bundle = new Bundle();
        return bundle;
    }
    @Rule
    public ActivityScenarioRule<MainActivity> mainActivityScenarioRule =
            new ActivityScenarioRule<MainActivity>(MainActivity.class,getBundle());

    @Before
    public void setUp() {
        AppDatabaseFactory.setTestMode();
        UIAuthenticationHandlerFactory.setTestMode();
        if(!USINIG_REAL_DB)FirebaseDatabaseFactory.setTestMode();
        db = AppDatabaseFactory.getInstance();
        dbCallback.call(db);
        setupCallback.run();
    }

    @After
    public void tearDown() {
        try {
            MainActivity.testMode=false;
            AppDatabaseFactory.tearDownDb();
            RemoteDatabaseFacadeFactory.tearDown();
            CommunicationFacadeFactory.tearDown();
            RemoteDatabaseFacadeFactory.tearDown();
            FilesManagerFactory.tearDown();
            ExamRepositoryFactory.tearDown();
            CDCRepositoryFacrory.tearDown();
            ScannedCaptureRepositoryFactory.tearDown();
            GraderRepoFactory.tearDown();
            UIAuthenticationHandlerFactory.tearDown();
        }catch (Exception e ){
            Log.d(DEBUG_TAG, "failed to teardown", e);
        }
    }

    protected interface DBCallback {
        public void call(AppDatabase db);
    }
}
