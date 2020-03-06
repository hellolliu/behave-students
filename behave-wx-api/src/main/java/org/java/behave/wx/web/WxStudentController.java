package org.java.behave.wx.web;

import com.sun.corba.se.impl.ior.OldJIDLObjectKeyTemplate;
import org.apache.commons.collections.map.HashedMap;
import org.java.behave.core.util.ResponseUtil;
import org.java.behave.db.domain.*;
import org.java.behave.db.service.*;
import org.java.behave.wx.dao.StudentAnserVO;
import org.java.behave.wx.dao.StudentClassVO;
import org.java.behave.wx.dao.StudentScoreAndCorse;
import org.omg.CORBA.INITIALIZE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/wx/student")
@Validated
public class WxStudentController {
    @Autowired
    private BehaveUserScoreService userScoreService;
    @Autowired
    private BehaveUserService userService;
    @Autowired
    private BehaveCourseService courseService;
    @Autowired
    private BehaveTeacherQuestionService teacherQuestionService;
    @Autowired
    private BehaveUserAnswerService userAnswerService;
    @Autowired
    private BehaveQuestionClassService questionClassService;
    @Autowired
    private BehaveQuestionItemService questionItemService;

    @GetMapping("intoClassValue")
    public Object intoClassValue(Integer courserid, Integer teacherid,Integer studentid, Integer scheduleid){
        if (courserid==null||teacherid==null||scheduleid==null||studentid==null)
            return ResponseUtil.badArgument();
        StudentClassVO studentClassVO=new StudentClassVO();
        BehaveUser teacher=userService.findById(teacherid);//教师信息
        studentClassVO.setUser(teacher);
        BehaveCourse course=courseService.findById(courserid);
        studentClassVO.setCourse(course);
        List<BehaveUserScore> userScores=userScoreService.queryStudentScore( teacherid, studentid, courserid);
        studentClassVO.setUserScore(userScores);
        return ResponseUtil.ok(studentClassVO);
    }
    @GetMapping("studentQueryScore")
    public Object studentQueryScore(Integer studentId,String startTime,String endTime){
        if (studentId==null||startTime==null||endTime==null)
            return ResponseUtil.badArgument();
        List<BehaveUserScore> behaveUserScores=userScoreService.BetweenTimeStudent(studentId,startTime,endTime);
        List<StudentScoreAndCorse> scoreAndCorses=new ArrayList<>();
        behaveUserScores.forEach(userScore -> {
            StudentScoreAndCorse studentScoreAndCorse=new StudentScoreAndCorse();
            studentScoreAndCorse.setUserScore(userScore);
            studentScoreAndCorse.setCourse(courseService.findById(userScore.getCourseId()));
            studentScoreAndCorse.setTeacherQuestion(teacherQuestionService.findById(userScore.getQuestionId()));
            scoreAndCorses.add(studentScoreAndCorse);
        });
        return ResponseUtil.ok(scoreAndCorses);
    }
    @GetMapping("studentAnswerAll")
    public Object studentAnswerAll(Integer studentId,String startTime,String endTime){
        if (studentId==null||startTime==null||endTime==null)
            return ResponseUtil.badArgument();
        List<StudentAnserVO> studentAnserVOS=new ArrayList<>();
        List<BehaveUserAnswer> userAnswers=userAnswerService.queryBetweenTime(studentId,startTime,endTime);
        userAnswers.forEach(behaveUserAnswer -> {
            StudentAnserVO vo=new StudentAnserVO();
            BehaveQuestionClass behaveQuestionClass=questionClassService.findById(behaveUserAnswer.getQuestionClassId());
            BehaveTeacherQuestion teacherQuestion=teacherQuestionService.findById(behaveQuestionClass.getQuestionId());
            vo.setTeacherQuestion(teacherQuestion);
            vo.setUserAnswer(behaveUserAnswer);
            if (teacherQuestion!=null){
                studentAnserVOS.add(vo);
            }
        });
        return ResponseUtil.ok(studentAnserVOS);
    }
    @GetMapping("studentQuestions")
    public Object studentQuestions(Integer questionId){
        if (questionId==null)
            return ResponseUtil.fail();
        BehaveQuestionItem item=new BehaveQuestionItem();
        item.setQuestionId(questionId);
        List<BehaveQuestionItem>questionItems=questionItemService.queryAll(item);
        Map<Byte,List<BehaveQuestionItem>> qm = questionItems.stream().collect(Collectors.groupingBy(BehaveQuestionItem::getType));
        if (qm.get(new Byte("0"))==null){
            qm.put(new Byte("0"),new ArrayList<BehaveQuestionItem>());
        }
        if (qm.get(new Byte("1"))==null){
            qm.put(new Byte("1"),new ArrayList<BehaveQuestionItem>());
        }
        if (qm.get(new Byte("2"))==null){
            qm.put(new Byte("2"),new ArrayList<BehaveQuestionItem>());
        }
        return ResponseUtil.ok(qm);
    }
    @PostMapping("studentAnswer")
    public Object studentAnswer(@RequestBody BehaveUserAnswer userAnswer){
        userAnswer.setStatus(userAnswerService.NOTAPPROVAL);
        userAnswerService.update(userAnswer);
        return ResponseUtil.ok();
    }

}
