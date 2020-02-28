package org.java.behave.db.service;

import org.java.behave.db.dao.BehaveQuestionClassMapper;
import org.java.behave.db.domain.BehaveQuestionClass;
import org.java.behave.db.domain.BehaveQuestionClassExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BehaveQuestionClassService {
    @Autowired
    private BehaveQuestionClassMapper questionClassMapper;

    public List<BehaveQuestionClass> queryAll(BehaveQuestionClass questionClass){
        BehaveQuestionClassExample example=new BehaveQuestionClassExample();
        BehaveQuestionClassExample.Criteria ca=example.or();
        if (questionClass.getId()!=null){
            ca.andIdEqualTo(questionClass.getId());
        }
        if (questionClass.getClassId()!=null)
            ca.andClassIdEqualTo(questionClass.getClassId());
        if (questionClass.getQuestionId()!=null)
            ca.andQuestionIdEqualTo(questionClass.getQuestionId());
        if (questionClass.getUserId()!=null)
            ca.andUserIdEqualTo(questionClass.getUserId());
        ca.andDeletedEqualTo(false);
        return questionClassMapper.selectByExample(example);
    }

    public BehaveQuestionClass findById(Integer id){
        return questionClassMapper.selectByPrimaryKey(id);
    }
    public int add(BehaveQuestionClass questionClass){
        questionClass.setAddTime(LocalDateTime.now());
        questionClass.setUpdateTime(LocalDateTime.now());
        questionClass.setDeleted(false);
        return questionClassMapper.insert(questionClass);
    }
    public int update(BehaveQuestionClass questionClass){
        questionClass.setAddTime(LocalDateTime.now());
        questionClass.setAddTime(LocalDateTime.now());
        return questionClassMapper.updateByPrimaryKey(questionClass);
    }
    public int delete(Integer id){
        return questionClassMapper.deleteByPrimaryKey(id);
    }
}
