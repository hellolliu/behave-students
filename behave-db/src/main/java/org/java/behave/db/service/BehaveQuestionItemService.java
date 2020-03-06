package org.java.behave.db.service;

import com.github.pagehelper.PageHelper;
import org.java.behave.db.dao.BehaveQuestionItemMapper;
import org.java.behave.db.dao.BehaveTeacherQuestionMapper;
import org.java.behave.db.domain.*;
import org.java.behave.db.domain.BehaveQuestionItem;
import org.java.behave.db.domain.BehaveQuestionItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Service
public class BehaveQuestionItemService {
    //0判断，1多选，2单选
    public final Integer PANDUAN=0;
    public final Integer DUOXUAN=1;
    public final Integer DANXUAN=2;
    @Autowired
    private BehaveQuestionItemMapper questionItemMapper;

    public BehaveQuestionItem findById(Integer id){
        return questionItemMapper.selectByPrimaryKey(id);
    }
    public List<BehaveQuestionItem> queryAll(BehaveQuestionItem question){
        BehaveQuestionItemExample example=new BehaveQuestionItemExample();
        BehaveQuestionItemExample.Criteria ca=example.or();
        if (question.getId()!=null){
            ca.andIdEqualTo(question.getId());
        }
        if (question.getQuestionId()!=null){
            ca.andQuestionIdEqualTo(question.getQuestionId());
        }
        ca.andDeletedEqualTo(false);
        return questionItemMapper.selectByExample(example);
    }
    public List<BehaveQuestionItem> querySelective(Integer questionid, Integer page, Integer limit, String sort, String order) {
        BehaveQuestionItemExample example = new BehaveQuestionItemExample();
        BehaveQuestionItemExample.Criteria criteria = example.createCriteria();
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(questionid))
            criteria.andQuestionIdEqualTo(questionid);
        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return questionItemMapper.selectByExample(example);
    }
    public int add(BehaveQuestionItem question){
        question.setAddTime(LocalDateTime.now());
        question.setUpdateTime(LocalDateTime.now());
        question.setDeleted(false);
        return questionItemMapper.insert(question);
    }
    public int update(BehaveQuestionItem question){
        question.setAddTime(LocalDateTime.now());
        question.setUpdateTime(LocalDateTime.now());
        question.setDeleted(false);
        return questionItemMapper.updateByPrimaryKey(question);
    }
    public int delete(Integer id){
        return questionItemMapper.deleteByPrimaryKey(id);
    }
}
