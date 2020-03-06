package org.java.behave.db.service;

import com.github.pagehelper.PageHelper;
import org.java.behave.db.dao.BehaveTeacherQuestionMapper;
import org.java.behave.db.domain.BehaveClass;
import org.java.behave.db.domain.BehaveClassExample;
import org.java.behave.db.domain.BehaveTeacherQuestion;
import org.java.behave.db.domain.BehaveTeacherQuestionExample;
import org.omg.CORBA.INITIALIZE;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class BehaveTeacherQuestionService {
    //状态（0：禁用；1：启用）
    public final Integer ENABLE=1;
    public final Integer NOTENABLE=0;
    @Autowired
    private BehaveTeacherQuestionMapper questionMapper;

    public BehaveTeacherQuestion findById(Integer id){
        return questionMapper.selectByPrimaryKey(id);
    }
    public List<BehaveTeacherQuestion> queryAll(BehaveTeacherQuestion question){
        BehaveTeacherQuestionExample example=new BehaveTeacherQuestionExample();
        BehaveTeacherQuestionExample.Criteria ca=example.or();
        if (question.getId()!=null){
            ca.andIdEqualTo(question.getId());
        }
        if (question.getUserId()!=null)
            ca.andUserIdEqualTo(question.getUserId());
        if (question.getStatus()!=null)
            ca.andStatusEqualTo(question.getStatus());
        ca.andDeletedEqualTo(false);
        return questionMapper.selectByExample(example);
    }
    public List<BehaveTeacherQuestion> querySelective(Integer userid, Integer page, Integer limit, String sort, String order) {
        BehaveTeacherQuestionExample example = new BehaveTeacherQuestionExample();
        BehaveTeacherQuestionExample.Criteria criteria = example.createCriteria();
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(userid))
            criteria.andUserIdEqualTo(userid);
        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return questionMapper.selectByExample(example);
    }
    public List<BehaveTeacherQuestion>queryBetweenTime(Integer userId,String start,String end){

        BehaveTeacherQuestionExample example=new BehaveTeacherQuestionExample();
        LocalDateTime startDateTime =
                LocalDateTime.parse(start+" 00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime endDateTime=
                LocalDateTime.parse(end+" 24:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        example.or().andDeletedEqualTo(false).andUserIdEqualTo(userId).andAddTimeBetween(startDateTime,endDateTime);
        example.setOrderByClause(" status desc");
        return questionMapper.selectByExample(example);
    }
    public int add(BehaveTeacherQuestion question){
        question.setAddTime(LocalDateTime.now());
        question.setUpdateTime(LocalDateTime.now());
        question.setDeleted(false);
        return questionMapper.insert(question);
    }
    public int update(BehaveTeacherQuestion question){
        question.setAddTime(LocalDateTime.now());
        question.setUpdateTime(LocalDateTime.now());
        return questionMapper.updateByPrimaryKey(question);
    }
    public int delete(Integer id){
        return questionMapper.deleteByPrimaryKey(id);
    }
}
