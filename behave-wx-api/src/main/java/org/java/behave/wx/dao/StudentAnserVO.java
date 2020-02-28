package org.java.behave.wx.dao;

import org.java.behave.db.domain.BehaveTeacherQuestion;
import org.java.behave.db.domain.BehaveUserAnswer;

public class StudentAnserVO {
    private BehaveUserAnswer userAnswer;
    private BehaveTeacherQuestion teacherQuestion;

    public BehaveUserAnswer getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(BehaveUserAnswer userAnswer) {
        this.userAnswer = userAnswer;
    }

    public BehaveTeacherQuestion getTeacherQuestion() {
        return teacherQuestion;
    }

    public void setTeacherQuestion(BehaveTeacherQuestion teacherQuestion) {
        this.teacherQuestion = teacherQuestion;
    }
}
