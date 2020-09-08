package com.example.examscanner.components.admin;

import androidx.room.Room;

import com.example.examscanner.R;
import com.example.examscanner.StateFullTest;
import com.example.examscanner.Utils;
import com.example.examscanner.authentication.AuthenticationHandlerFactory;
import com.example.examscanner.authentication.state.StateFactory;
import com.example.examscanner.components.create_exam.CreateExamModelView;
import com.example.examscanner.components.scan_exam.BitmapsInstancesFactoryAndroidTest;
import com.example.examscanner.image_processing.ImageProcessingFacade;
import com.example.examscanner.image_processing.ImageProcessingFactory;
import com.example.examscanner.persistence.local.AppDatabase;
import com.example.examscanner.persistence.local.AppDatabaseFactory;
import com.example.examscanner.persistence.local.files_management.FilesManagerFactory;
import com.example.examscanner.persistence.remote.FirebaseDatabaseFactory;
import com.example.examscanner.persistence.remote.RemoteDatabaseFacadeFactory;
import com.example.examscanner.persistence.remote.files_management.RemoteFilesManagerFactory;
import com.example.examscanner.repositories.Repository;
import com.example.examscanner.repositories.exam.Exam;
import com.example.examscanner.repositories.exam.ExamRepositoryFactory;
import com.example.examscanner.repositories.grader.Grader;
import com.example.examscanner.repositories.grader.GraderRepoFactory;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.observers.TestObserver;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.examscanner.components.create_exam.CreateExamFragmentAbstractTestStateFull.BOB_ID;
import static java.lang.Thread.sleep;
import static org.junit.Assert.*;

public class AdminFragmentTest  extends StateFullTest {

    private ImageProcessingFacade imageProcessor;
    private CreateExamModelView createExamModelView;
    private Repository<Exam> examRepository;

    @Override
    @Before
    public void setUp() {
        FirebaseDatabaseFactory.setTestMode();
        AppDatabaseFactory.setTestMode();
        FilesManagerFactory.setTestMode(getApplicationContext());
        RemoteFilesManagerFactory.setTestMode();
        TestObserver to = new TestObserver(){
            @Override
            public void onNext(Object value) {
                StateFactory.get().login(StateFactory.getStateHolder(), value);
            }
        };
        AuthenticationHandlerFactory.getTest().authenticate().subscribe(to);
        to.awaitCount(1);
        ImageProcessingFactory.setTestMode(getApplicationContext());
        imageProcessor = new ImageProcessingFactory().create();
        createExamModelView = new CreateExamModelView(
                new ExamRepositoryFactory().create(),
                new GraderRepoFactory().create(),
                imageProcessor,
                StateFactory.get(),
                0
        );
        examRepository = new ExamRepositoryFactory().create();
        createExamModelView.holdVersionBitmap(BitmapsInstancesFactoryAndroidTest.getComp191_V1_ins_in1());
        createExamModelView.holdVersionNumber(3);
        createExamModelView.holdNumOfQuestions("50");
        createExamModelView.addVersion();
        createExamModelView.holdExamUrl("/durlurl");
        createExamModelView.create("CreateExamUpdatesGraderTest_courseName","A","Fall","2020");
        super.setUp();
    }

    @Test
    public void testDelete() {
        Utils.sleepABit();
        onView(withId(R.id.button_home_admin)).perform(click());
        Utils.sleepABit();
        onView(withId(R.id.button_admin_page_delete)).perform(click());
        Utils.sleepABit();
        onView(withText("OK")).perform(click());
        onView(withId(R.id.for_testing_home_fragment)).check(matches(isDisplayed()));
    }
}