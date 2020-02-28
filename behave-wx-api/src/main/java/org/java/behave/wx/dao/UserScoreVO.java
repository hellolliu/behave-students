package org.java.behave.wx.dao;

import org.java.behave.db.domain.BehaveUserAnswer;

public class UserScoreVO {
    private BehaveUserAnswer userAnswer;
    private String score;
    private Integer teacherId;

    public BehaveUserAnswer getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(BehaveUserAnswer userAnswer) {
        this.userAnswer = userAnswer;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }
}
