package org.java.behave.db.service;

import org.java.behave.db.dao.BehaveUserAnswerMapper;
import org.java.behave.db.domain.BehaveUserAnswer;
import org.java.behave.db.domain.BehaveUserAnswerExample;
import org.java.behave.db.domain.BehaveUserAnswer;
import org.java.behave.db.domain.BehaveUserAnswerExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class BehaveUserAnswerService {
    //状态0未批改，1已回答，2已批改
    public final Byte APPROVAL=0;
    public final Byte NOTAPPROVAL=1;
    public final Byte FINISH=2;
    @Autowired
    private BehaveUserAnswerMapper userAnswerMapper;

    public List<BehaveUserAnswer> queryAll(BehaveUserAnswer behaveUserAnswer){
        BehaveUserAnswerExample example=new BehaveUserAnswerExample();
        BehaveUserAnswerExample.Criteria ca=example.or();
        if (behaveUserAnswer.getId()!=null){
            ca.andIdEqualTo(behaveUserAnswer.getId());
        }
        if (behaveUserAnswer.getQuestionClassId()!=null)
            ca.andQuestionClassIdEqualTo(behaveUserAnswer.getQuestionClassId());
        if (behaveUserAnswer.getStudentId()!=null)
            ca.andStudentIdEqualTo(behaveUserAnswer.getStudentId());
        if (behaveUserAnswer.getStatus()!=null)
            ca.andStatusEqualTo(behaveUserAnswer.getStatus());
        ca.andDeletedEqualTo(false);
        return userAnswerMapper.selectByExample(example);
    }
    public List<BehaveUserAnswer> queryBetweenTime(Integer studentId,String startTime,String endTime ){
        BehaveUserAnswerExample example=new BehaveUserAnswerExample();
        LocalDateTime startDateTime =
                LocalDateTime.parse(startTime+" 00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime endDateTime=
                LocalDateTime.parse(endTime+" 24:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        example.or().andStudentIdEqualTo(studentId).andAddTimeBetween(startDateTime,endDateTime).andDeletedEqualTo(false);
        return userAnswerMapper.selectByExample(example);
    }

    public int add(BehaveUserAnswer behaveUserAnswer){
        behaveUserAnswer.setAddTime(LocalDateTime.now());
        behaveUserAnswer.setUpdateTime(LocalDateTime.now());
        behaveUserAnswer.setDeleted(false);
        return userAnswerMapper.insert(behaveUserAnswer);
    }
    public int update(BehaveUserAnswer behaveUserAnswer){
        behaveUserAnswer.setAddTime(LocalDateTime.now());
        behaveUserAnswer.setAddTime(LocalDateTime.now());
        return userAnswerMapper.updateByPrimaryKey(behaveUserAnswer);
    }
    public int delete(Integer id){
        return userAnswerMapper.deleteByPrimaryKey(id);
    }
}
