package org.java.behave.wx.dao;

import org.java.behave.db.domain.BehaveCourse;
import org.java.behave.db.domain.BehaveTeacherQuestion;
import org.java.behave.db.domain.BehaveUserScore;

public class StudentScoreAndCorse {
    private BehaveUserScore userScore;
    private BehaveCourse course;
    private BehaveTeacherQuestion teacherQuestion;

    public BehaveUserScore getUserScore() {
        return userScore;
    }

    public void setUserScore(BehaveUserScore userScore) {
        this.userScore = userScore;
    }

    public BehaveCourse getCourse() {
        return course;
    }

    public void setCourse(BehaveCourse course) {
        this.course = course;
    }

    public BehaveTeacherQuestion getTeacherQuestion() {
        return teacherQuestion;
    }

    public void setTeacherQuestion(BehaveTeacherQuestion teacherQuestion) {
        this.teacherQuestion = teacherQuestion;
    }
}
