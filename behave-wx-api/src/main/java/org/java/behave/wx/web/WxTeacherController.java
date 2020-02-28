package org.java.behave.wx.web;

import com.sun.org.apache.regexp.internal.RE;
import org.java.behave.core.util.ResponseUtil;
import org.java.behave.db.domain.*;
import org.java.behave.db.service.*;
import org.java.behave.wx.dao.UserScoreVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sun.dc.pr.PRError;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/wx/teacher")
@Validated
public class WxTeacherController {
    @Autowired
    private BehaveTeacherQuestionService questionService;
    @Autowired
    private BehaveQuestionClassService questionClassService;
    @Autowired
    private BehaveUserService userService;
    @Autowired
    private BehaveUserAnswerService userAnswerService;
    @Autowired
    private BehaveUserScoreService userScoreService;

    @GetMapping("enableAllQuestion")
    public Object listQuestionEnable(Integer userId){
        if (userId==null)
            return ResponseUtil.fail();
        BehaveTeacherQuestion question=new BehaveTeacherQuestion();
        question.setUserId(userId);
        question.setStatus(questionService.ENABLE);
        List<BehaveTeacherQuestion> questions=questionService.queryAll(question);
        return ResponseUtil.ok(questions);
    }
    @GetMapping("allQuestion")
    public Object allQuestion(Integer userId, String start,String end){
        if (userId==null||start==null||end==null)
            return ResponseUtil.fail();
        List<BehaveTeacherQuestion> questions=questionService.queryBetweenTime(userId,start,end);
        return ResponseUtil.ok(questions);
    }
    @PostMapping("addQuestion")
    public Object addQuestion(@RequestBody BehaveTeacherQuestion question){
        if (verifiedQuestion(question))
            return ResponseUtil.fail();
        questionService.add(question);
        return ResponseUtil.ok();
    }
    private boolean verifiedQuestion(BehaveTeacherQuestion question){
        if (question.getUserId()==null)
            return true;
        if (question.getValue()==null)
            return true;
        if (question.getStatus()==null)
            return true;
        return false;
    }
    @PostMapping("updateQuestion")
    public Object updateQuestion(@RequestBody BehaveTeacherQuestion question){
        if (verifiedQuestion(question))
            return ResponseUtil.fail();
        questionService.update(question);
        return ResponseUtil.ok();
    }
    @GetMapping("deleteQuestion")
    public Object deleteQuestion(Integer id){
        if (id==null)
            return ResponseUtil.fail();
        BehaveTeacherQuestion question=questionService.findById(id);
        BehaveQuestionClass questionClass=new BehaveQuestionClass();
        questionClass.setQuestionId(question.getId());
        List<BehaveQuestionClass>questionClasses=questionClassService.queryAll(questionClass);
        if (questionClasses!=null&&questionClasses.size()>0)
            return ResponseUtil.fail(1,"作业已经发布");
        questionService.delete(id);
        return ResponseUtil.ok();
    }

    @PostMapping("addOrUpdateQC")
    public Object addOrUpdateQC(@RequestBody BehaveQuestionClass questionClass){
        if (questionClass.getQuestionId()==null||questionClass.getClassId()==null)
            return ResponseUtil.fail();
        List<BehaveQuestionClass> questionClasses=questionClassService.queryAll(questionClass);
        if (questionClasses!=null&&questionClasses.size()!=0)
            return ResponseUtil.fail(1,"已经发布");
        questionClassService.add(questionClass);
        List<BehaveUser> userList=userService.queryStudentByClassId(questionClass.getClassId()+"");
        if (userList!=null)
            userList.forEach(e->{
                BehaveUserAnswer userAnswer=new BehaveUserAnswer();
                userAnswer.setStudentId(e.getId());
                userAnswer.setStudentName(e.getUsername());
                userAnswer.setQuestionClassId(questionClass.getId());
                userAnswer.setStatus(userAnswerService.APPROVAL);
                userAnswerService.add(userAnswer);
            });
        return ResponseUtil.ok();
    }
    @GetMapping("studentAnswers")
    public Object studentAnswers(Integer userId,Integer classId,Integer questionId){
        if (userId==null||classId==null||questionId==null)
            return ResponseUtil.badArgument();
        BehaveQuestionClass questionClass=new BehaveQuestionClass();
        questionClass.setUserId(userId);
        questionClass.setClassId(classId);
        questionClass.setQuestionId(questionId);
        List<BehaveQuestionClass>behaveQuestionClasses=questionClassService.queryAll(questionClass);
        if (behaveQuestionClasses.size()==0)
            return ResponseUtil.badArgumentValue();
        else if (behaveQuestionClasses.size()==1){
            BehaveUserAnswer userAnswer=new BehaveUserAnswer();
            userAnswer.setQuestionClassId(behaveQuestionClasses.get(0).getId());
            List<BehaveUserAnswer>userAnswers=userAnswerService.queryAll(userAnswer);
            return ResponseUtil.ok(userAnswers);
        }
        return ResponseUtil.fail();
    }
    @PostMapping("teacherAddScore")
    public Object teacherAddScore(@RequestBody UserScoreVO userScoreVO){
        if (userScoreVO.getUserAnswer().getQuestionClassId()==null||userScoreVO.getTeacherId()==null||userScoreVO.getScore()==null)
            return ResponseUtil.badArgument();
        userScoreVO.getUserAnswer().setStatus(userAnswerService.FINISH);
        userAnswerService.update(userScoreVO.getUserAnswer());
        BehaveQuestionClass questionClass=questionClassService.findById(userScoreVO.getUserAnswer().getQuestionClassId());
        BehaveUserScore userScore=new BehaveUserScore();
        userScore.setQuestionId(questionClass.getQuestionId());
        userScore.setTeacherId(userScoreVO.getTeacherId());
        userScore.setUserId(userScoreVO.getUserAnswer().getStudentId());
        userScore.setUserName(userScoreVO.getUserAnswer().getStudentName());
        userScore.setValue(Integer.valueOf(userScoreVO.getScore()));
        userScoreService.addAnswer(userScore);
        return ResponseUtil.ok();
    }
    @GetMapping("teacherQueryScore")
    public Object teacherQueryScore(Integer teacherId,String startTime,String endTime){
        if (teacherId==null||startTime==null||endTime==null)
            return ResponseUtil.badArgument();
        List<BehaveUserScore> behaveUserScores=userScoreService.queryBetweenTime(teacherId,startTime,endTime);
        return ResponseUtil.ok(behaveUserScores);
    }
}
