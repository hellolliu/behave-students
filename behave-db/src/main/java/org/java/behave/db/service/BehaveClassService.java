package org.java.behave.db.service;

import com.github.pagehelper.PageHelper;
import org.java.behave.db.dao.BehaveClassMapper;
import org.java.behave.db.dao.BehaveScheduleMapper;
import org.java.behave.db.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BehaveClassService {
    @Autowired
    private BehaveClassMapper behaveClassMapper;
    @Autowired
    private BehaveScheduleMapper scheduleMapper;

    public List<BehaveClass> queryAll(){
        BehaveClassExample example=new BehaveClassExample();
        example.or().andDeletedEqualTo(false);
        return behaveClassMapper.selectByExample(example);
    }
    public List<BehaveClass> querySelective(String name, Integer page, Integer limit, String sort, String order) {
        BehaveClassExample example = new BehaveClassExample();
        BehaveClassExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(name)) {
            criteria.andNameEqualTo("%" + name + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return behaveClassMapper.selectByExample(example);
    }
    public List<BehaveClass> findAdmin(String username) {
        BehaveClassExample example = new BehaveClassExample();
        example.or().andNameEqualTo(username).andDeletedEqualTo(false);
        return behaveClassMapper.selectByExample(example);
    }
    public int updateById(BehaveClass behaveClass) {
        behaveClass.setUpdateTime(LocalDateTime.now());
        return behaveClassMapper.updateByPrimaryKeySelective(behaveClass);
    }

    public void deleteById(Integer id) {
        behaveClassMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(BehaveClass behaveClass) {
        behaveClass.setAddTime(LocalDateTime.now());
        behaveClass.setUpdateTime(LocalDateTime.now());
        behaveClassMapper.insertSelective(behaveClass);
        BehaveSchedule schedule=new BehaveSchedule();
        schedule.setClassId(behaveClass.getId());
        schedule.setAddTime(LocalDateTime.now());
        schedule.setUpdateTime(LocalDateTime.now());
        scheduleMapper.insertSelective(schedule);
    }

    public BehaveClass findById(Integer id) {
        return behaveClassMapper.selectByPrimaryKey(id);
    }

}
