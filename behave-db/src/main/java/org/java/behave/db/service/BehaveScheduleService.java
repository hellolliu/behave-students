package org.java.behave.db.service;

import org.java.behave.db.dao.BehaveScheduleMapper;
import org.java.behave.db.dao.BehaveScheduleValueMapper;
import org.java.behave.db.domain.BehaveSchedule;
import org.java.behave.db.domain.BehaveScheduleExample;
import org.java.behave.db.domain.BehaveScheduleValue;
import org.java.behave.db.domain.BehaveScheduleValueExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class BehaveScheduleService {
    @Autowired
    private BehaveScheduleMapper scheduleMapper;
    @Autowired
    private BehaveScheduleValueMapper scheduleValueMapper;

    public List<BehaveScheduleValue> queryScheduleVo(Integer scheduleId,String week){
        BehaveScheduleValueExample example=new BehaveScheduleValueExample();
        example.setOrderByClause("slot_order_fie asc");
        BehaveScheduleValueExample.Criteria criteria=example.createCriteria();
        criteria.andScheduleIdEqualTo(scheduleId).andWeekEqualTo(week).andDeletedEqualTo(false);
        return scheduleValueMapper.selectByExample(example);
    }
    public List<BehaveScheduleValue> querySchByUserTody(Integer userId){
        BehaveScheduleValueExample example=new BehaveScheduleValueExample();
        BehaveScheduleValueExample.Criteria criteria=example.createCriteria();
        example.setOrderByClause("slot_order_fie asc");
        criteria.andCourseUserIdEqualTo(userId).andWeekEqualTo((LocalDateTime.now().getDayOfWeek().getValue()-1)+"").andDeletedEqualTo(false);
        return scheduleValueMapper.selectByExample(example);
    }
    public List<BehaveScheduleValue> queryBySortId(Integer scheduleId,Integer slotId){
        BehaveScheduleValueExample example=new BehaveScheduleValueExample();
        BehaveScheduleValueExample.Criteria criteria=example.createCriteria();
        example.setOrderByClause("week");
        criteria.andScheduleIdEqualTo(scheduleId).andSlotIdEqualTo(slotId).andDeletedEqualTo(false);
        return scheduleValueMapper.selectByExample(example);
    }
    public List<BehaveScheduleValue> queryBySort(Integer userId,Integer slotId){
        BehaveScheduleValueExample example=new BehaveScheduleValueExample();
        BehaveScheduleValueExample.Criteria criteria=example.createCriteria();
        example.setOrderByClause("week");
        criteria.andCourseUserIdEqualTo(userId).andSlotIdEqualTo(slotId).andDeletedEqualTo(false);
        return scheduleValueMapper.selectByExample(example);
    }
    public BehaveSchedule findByClassId(String classId){
        BehaveScheduleExample example=new BehaveScheduleExample();
        BehaveScheduleExample.Criteria criteria=example.createCriteria();
        criteria.andClassIdEqualTo(Integer.valueOf(classId)).andDeletedEqualTo(false);
        return scheduleMapper.selectOneByExample(example);
    }
    public BehaveSchedule findById(String scheduleId){
        BehaveScheduleExample example=new BehaveScheduleExample();
        BehaveScheduleExample.Criteria criteria=example.createCriteria();
        criteria.andIdEqualTo(Integer.valueOf(scheduleId));
        return scheduleMapper.selectOneByExample(example);
    }
    public void deleteValue(Integer scheduleId,String week,String slotOF){
        BehaveScheduleValueExample example=new BehaveScheduleValueExample();
        example.or().andScheduleIdEqualTo(scheduleId)
                .andWeekEqualTo(week)
                .andSlotOrderFieEqualTo(new Byte(slotOF))
        .andDeletedEqualTo(false);
        scheduleValueMapper.deleteByExample(example);
    }
    public void updateValue(BehaveScheduleValue scheduleValue){
        BehaveSchedule schedule=findByClassId(scheduleValue.getScheduleId()+"");
        deleteValue(schedule.getId(),scheduleValue.getWeek(),scheduleValue.getSlotOrderFie()+"");
        scheduleValue.setScheduleId(schedule.getId());
        scheduleValue.setUpdateTime(LocalDateTime.now());
        scheduleValue.setAddTime(LocalDateTime.now());
        scheduleValue.setDeleted(false);
        scheduleValueMapper.insert(scheduleValue);
    }
}
