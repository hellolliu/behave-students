package org.java.behave.wx.dao;

import org.java.behave.db.domain.BehaveScheduleSolt;
import org.java.behave.db.domain.BehaveScheduleValue;

public class ScheduleVo {
    private BehaveScheduleValue scheduleValue;
    private BehaveScheduleSolt scheduleSolt;
    private Integer status;

    public BehaveScheduleValue getScheduleValue() {
        return scheduleValue;
    }

    public void setScheduleValue(BehaveScheduleValue scheduleValue) {
        this.scheduleValue = scheduleValue;
    }

    public BehaveScheduleSolt getScheduleSolt() {
        return scheduleSolt;
    }

    public void setScheduleSolt(BehaveScheduleSolt scheduleSolt) {
        this.scheduleSolt = scheduleSolt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
