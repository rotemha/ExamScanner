package com.example.examscanner.persistence.local.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity
public class SemiScannedCapture {
    public static final String pkName = "id";
    @PrimaryKey(autoGenerate = true)
    private long id;
    private int leftMostX;
    private int upperMostY;
    private int rightMostX;
    private int bottomMostY;
    //    public static final String fkVersionId = "versionId";
//    @ForeignKey(entity = Version.class, parentColumns = {Version.pkName}, childColumns = {"versionId"})
//    private long versionId;
    public static final String fkSessionId = "sessionId";
    @ForeignKey(entity = ScanExamSession.class, parentColumns = {ScanExamSession.pkName}, childColumns = {"sessionId"})
    private long sessionId;

    public SemiScannedCapture(int leftMostX, int upperMostY, int rightMostX, int bottomMostY, long sessionId) {
        this.leftMostX = leftMostX;
        this.upperMostY = upperMostY;
        this.rightMostX = rightMostX;
        this.bottomMostY = bottomMostY;
//        this.versionId = versionId;
        this.sessionId = sessionId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getLeftMostX() {
        return leftMostX;
    }

    public void setLeftMostX(int leftMostX) {
        this.leftMostX = leftMostX;
    }

    public int getUpperMostY() {
        return upperMostY;
    }

    public void setUpperMostY(int upperMostY) {
        this.upperMostY = upperMostY;
    }

    public int getRightMostX() {
        return rightMostX;
    }

    public void setRightMostX(int rightMostX) {
        this.rightMostX = rightMostX;
    }

    public int getBottomMostY() {
        return bottomMostY;
    }

    public void setBottomMostY(int bottomMostY) {
        this.bottomMostY = bottomMostY;
    }


    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public String _getBitmapBath(){
        return "SSC_"+String.valueOf(id);
    }
}
