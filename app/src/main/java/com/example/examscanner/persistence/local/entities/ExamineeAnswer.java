package com.example.examscanner.persistence.local.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        indices = {@Index(value = {"examineeSolutionId", "questionId"}, unique = true)},
        foreignKeys ={@ForeignKey(onDelete = CASCADE,entity = ExamineeSolution.class,
                parentColumns = ExamineeSolution.pkName,childColumns = ExamineeAnswer.fkESid)}
)
public class ExamineeAnswer {
    public static final String pkName = "id";
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ForeignKey(entity = Question.class, parentColumns = Question.pkName, childColumns = "questionId")
    public static final String fkQue = "questionId";
    private long questionId;
    public static final String fkESid = "examineeSolutionId";
    @ForeignKey(entity = ExamineeSolution.class, parentColumns = ExamineeSolution.pkName, childColumns = fkESid)
    private long examineeSolutionId;
    private int ans;
    private int leftX;
    private int upY;
    private int rightX;
    private int bottomY;

    public ExamineeAnswer(long questionId, long examineeSolutionId, int ans, int leftX, int upY, int rightX, int bottomY) {
        this.questionId = questionId;
        this.examineeSolutionId = examineeSolutionId;
        this.ans = ans;
        this.leftX = leftX;
        this.upY = upY;
        this.rightX = rightX;
        this.bottomY = bottomY;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public long getExamineeSolutionId() {
        return examineeSolutionId;
    }

    public void setExamineeSolutionId(long examineeSolutionId) {
        this.examineeSolutionId = examineeSolutionId;
    }

    public int getAns() {
        return ans;
    }

    public void setAns(int ans) {
        this.ans = ans;
    }

    public int getLeftX() {
        return leftX;
    }

    public void setLeftX(int leftX) {
        this.leftX = leftX;
    }

    public int getUpY() {
        return upY;
    }

    public void setUpY(int upY) {
        this.upY = upY;
    }

    public int getRightX() {
        return rightX;
    }

    public void setRightX(int rightX) {
        this.rightX = rightX;
    }

    public int getBottomY() {
        return bottomY;
    }

    public void setBottomY(int bottomY) {
        this.bottomY = bottomY;
    }
}
