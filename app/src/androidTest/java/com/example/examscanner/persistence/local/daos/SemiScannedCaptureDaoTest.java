package com.example.examscanner.persistence.local.daos;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore("Tests for persistence are in CommunicationFacadeTest")
public class SemiScannedCaptureDaoTest extends DaoAbstractTest {
    private static final String TAG = "SemiScannedCaptureDaoTest";
    private SemiScannedCaptureDao semiScannedCaptureDao;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        semiScannedCaptureDao = db.getSemiScannedCaptureDao();
    }

    @Test
    public void testConsistentSessionFK() {
//        long sid = db.getScanExamSessionDao().insert(new ScanExamSession(TAG));
//        long sscid = db.getSemiScannedCaptureDao().insert(new SemiScannedCapture(
//                0, 0, 0, 0, 0, sid
//        ));
//        SemiScannedCapture ssc = db.getSemiScannedCaptureDao().findById(sscid);
//        assertTrue(ssc.getSessionId()==sid);
    }

    @Test
    public void testCannotInsertWithoutPropperSessionId() {
//        try {
//            long i = db.getSemiScannedCaptureDao().insert(new SemiScannedCapture(
//                    0, 0, 0, 0, 0, 496351
//            ));
//            fail();
//        }catch (Exception e){}

    }
}