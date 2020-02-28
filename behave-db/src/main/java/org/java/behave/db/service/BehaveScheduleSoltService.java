package org.java.behave.db.service;

import com.github.pagehelper.PageHelper;
import org.java.behave.db.dao.BehaveScheduleSoltMapper;
import org.java.behave.db.dao.BehaveScheduleSoltMapper;
import org.java.behave.db.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BehaveScheduleSoltService {
    @Autowired
    private BehaveScheduleSoltMapper scheduleSoltMapper;

    public List<BehaveScheduleSolt> queryAll(){
        BehaveScheduleSoltExample example=new BehaveScheduleSoltExample();
        example.or().andDeletedEqualTo(false);
        example.setOrderByClause("sort_fie");
        return scheduleSoltMapper.selectByExample(example);
    }

    public List<BehaveScheduleSolt> findSoltTime(String soltTime){
        BehaveScheduleSoltExample example=new BehaveScheduleSoltExample();
        example.or().andDeletedEqualTo(false).andTimeSlotEqualTo(soltTime);
        return scheduleSoltMapper.selectByExample(example);
    }
    public List<BehaveScheduleSolt> querySelective(Integer page, Integer limit, String sort, String order) {
        BehaveScheduleSoltExample example = new BehaveScheduleSoltExample();
        BehaveScheduleSoltExample.Criteria criteria = example.createCriteria();
//
//        if (!StringUtils.isEmpty(name)) {
//            criteria.andNameEqualTo("%" + name + "%");
//        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return scheduleSoltMapper.selectByExample(example);
    }

    public int updateById(BehaveScheduleSolt behaveClass) {
        behaveClass.setUpdateTime(LocalDateTime.now());
        return scheduleSoltMapper.updateByPrimaryKeySelective(behaveClass);
    }

    public int count(){
        BehaveScheduleSoltExample example=new BehaveScheduleSoltExample();
        example.or().andDeletedEqualTo(false);
        return (int)scheduleSoltMapper.countByExample(example);
    }
    public void deleteById(Integer id) {
        scheduleSoltMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(BehaveScheduleSolt behaveClass) {
        behaveClass.setAddTime(LocalDateTime.now());
        behaveClass.setUpdateTime(LocalDateTime.now());
        scheduleSoltMapper.insertSelective(behaveClass);
    }

    public BehaveScheduleSolt findById(Integer id) {
        return scheduleSoltMapper.selectByPrimaryKey(id);
    }

}
