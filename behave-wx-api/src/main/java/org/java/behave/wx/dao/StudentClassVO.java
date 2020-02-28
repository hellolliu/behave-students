package org.java.behave.wx.dao;

import org.java.behave.db.domain.BehaveCourse;
import org.java.behave.db.domain.BehaveUser;
import org.java.behave.db.domain.BehaveUserScore;

import java.util.List;

public class StudentClassVO {
    private BehaveUser user;
    private BehaveCourse course;
    private List<BehaveUserScore> userScore;

    public BehaveUser getUser() {
        return user;
    }

    public void setUser(BehaveUser user) {
        this.user = user;
    }

    public BehaveCourse getCourse() {
        return course;
    }

    public void setCourse(BehaveCourse course) {
        this.course = course;
    }

    public List<BehaveUserScore> getUserScore() {
        return userScore;
    }

    public void setUserScore(List<BehaveUserScore> userScore) {
        this.userScore = userScore;
    }
}
