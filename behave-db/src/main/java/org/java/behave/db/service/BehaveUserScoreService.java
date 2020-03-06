package org.java.behave.db.service;

import org.java.behave.db.dao.BehaveUserScoreMapper;
import org.java.behave.db.domain.BehaveUserScore;
import org.java.behave.db.domain.BehaveUserScoreExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BehaveUserScoreService {
    //类型（1：提问；0：作答）
    private final Byte QUESTION=1;
    private final Byte ANSWER=0;
    @Autowired
    private BehaveUserScoreMapper userScoreMapper;

    public void addQuestion(BehaveUserScore score) {
        score.setAddTime(LocalDateTime.now());
        score.setUpdateTime(LocalDateTime.now());
        score.setType(QUESTION);
        userScoreMapper.insertSelective(score);
    }
    public void addAnswer(BehaveUserScore score) {
        score.setAddTime(LocalDateTime.now());
        score.setUpdateTime(LocalDateTime.now());
        score.setType(ANSWER);
        userScoreMapper.insertSelective(score);
    }
    public List<BehaveUserScore>queryBetweenTime(Integer teacherId,String startTime,String endTime){
        BehaveUserScoreExample example=new BehaveUserScoreExample();
        LocalDateTime startDateTime =
                LocalDateTime.parse(startTime+" 00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime endDateTime=
                LocalDateTime.parse(endTime+" 24:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        example.or().andTeacherIdEqualTo(teacherId).andAddTimeBetween(startDateTime,endDateTime).andDeletedEqualTo(false);
        return userScoreMapper.selectByExample(example);
    }
    public List<BehaveUserScore>BetweenTimeStudent(Integer student,String startTime,String endTime){
        BehaveUserScoreExample example=new BehaveUserScoreExample();
        LocalDateTime startDateTime =
                LocalDateTime.parse(startTime+" 00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime endDateTime=
                LocalDateTime.parse(endTime+" 24:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        example.or().andUserIdEqualTo(student).andAddTimeBetween(startDateTime,endDateTime).andDeletedEqualTo(false);
        return userScoreMapper.selectByExample(example);
    }
    public List<BehaveUserScore>queryStudentScore(Integer teacherId,Integer studentId,Integer courseId){
        BehaveUserScoreExample example=new BehaveUserScoreExample();
        example.or().andTeacherIdEqualTo(teacherId).andUserIdEqualTo(studentId).andCourseIdEqualTo(courseId).andTypeEqualTo(QUESTION).andDeletedEqualTo(false);
        return userScoreMapper.selectByExample(example);
    }
    public Map<Integer,List<BehaveUserScore>> queryStudents(Integer teacherId,Integer courseId){
        BehaveUserScoreExample example=new BehaveUserScoreExample();
        BehaveUserScoreExample.Criteria criteria=example.createCriteria();
        criteria.andTeacherIdEqualTo(teacherId).andCourseIdEqualTo(courseId).andDeletedEqualTo(false);
        List<BehaveUserScore> scores=userScoreMapper.selectByExample(example);
        if (scores==null)
            return null;
        Map<Integer,List<BehaveUserScore>> mapUserScore=scores.stream().collect(Collectors.groupingBy(BehaveUserScore::getUserId));
        return mapUserScore;
    }
}
