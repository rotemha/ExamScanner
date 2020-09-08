package com.example.examscanner.persistence.remote;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import io.reactivex.observers.TestObserver;

@Ignore("These are just examples of fire base database usage")
public class MsgSenderTest {

    public static final String TEST_SUCCESS_REF = "TEST_SUCCESS_REF";
    public static final String TEST_FAIL_REF = "TEST_FAIL_REF";
    public static final String TEST_MSG = "TEST_MSG";

    @Before
    public void setUp() throws Exception {
        FirebaseDatabaseFactory.setTestMode();
    }

    @Test
    public void sendAndReadSuccess() throws InterruptedException {
        TestObserver sendTestObserver = new TestObserver();
        new MsgSender().send(TEST_SUCCESS_REF, TEST_MSG).subscribe(sendTestObserver);
        sendTestObserver.awaitCount(1);
        sendTestObserver.assertComplete();
        TestObserver readTestObserver = new TestObserver();
        new MsgReader().read(TEST_SUCCESS_REF).subscribe(readTestObserver);
        readTestObserver.awaitCount(1);
        readTestObserver.assertValue(TEST_MSG);
    }

    @Test
    public void sendAndReadFail() throws InterruptedException {
        TestObserver sendTestObserver = new TestObserver();
        new MsgSender().send(TEST_FAIL_REF, TEST_MSG).subscribe(sendTestObserver);
        sendTestObserver.awaitCount(1);
        sendTestObserver.assertNotComplete();
        TestObserver readTestObserver = new TestObserver();
        new MsgReader().read(TEST_FAIL_REF).subscribe(readTestObserver);
        readTestObserver.awaitCount(1);
        sendTestObserver.assertNotComplete();
    }
}