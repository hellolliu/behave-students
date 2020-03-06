package org.java.behave.wx.web;

import org.apache.commons.collections.map.HashedMap;
import org.java.behave.core.util.ResponseUtil;
import org.java.behave.db.domain.*;
import org.java.behave.db.service.BehaveClassService;
import org.java.behave.db.service.BehaveScheduleService;
import org.java.behave.db.service.BehaveScheduleSoltService;
import org.java.behave.db.service.BehaveUserService;
import org.java.behave.wx.dao.ScheduleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wx/schedule")
@Validated
public class WxScheduleController {
    @Autowired
    private BehaveScheduleService scheduleService;
    @Autowired
    private BehaveScheduleSoltService scheduleSoltService;
    @Autowired
    private BehaveClassService classService;
    @Autowired
    private BehaveUserService userService;

    //老师今天课程
    @GetMapping("schTeaToday")
    public Object scheduleTeacher(Integer userId){
        if (StringUtils.isEmpty(userId))
            return ResponseUtil.fail();
        List<BehaveScheduleValue> scheduleValues= scheduleService.querySchByUserTody(userId);
        List<ScheduleVo> scheduleVos=initSchedule(scheduleValues);
        return ResponseUtil.ok(scheduleVos);
    }
    //学生今天课程
    @GetMapping("allCourse")
    public Object allCourse(String classId){
        if (StringUtils.isEmpty(classId))
            return ResponseUtil.badArgument();
        BehaveSchedule schedule=scheduleService.findByClassId(classId);
        if(schedule==null){
            return ResponseUtil.fail();
        }
        List<BehaveScheduleValue> scheduleValues=scheduleService.queryScheduleVo(schedule.getId(), (LocalDateTime.now().getDayOfWeek().getValue()-1)+"");
        List<ScheduleVo> scheduleVos=initSchedule(scheduleValues);
        return ResponseUtil.ok(scheduleVos);
    }
    @GetMapping("toClass")
    public Object toClass(String scheudleId){
        if (StringUtils.isEmpty(scheudleId))
            return ResponseUtil.fail();
        BehaveSchedule schedule=scheduleService.findById(scheudleId);
        BehaveClass behaveClass=classService.findById(schedule.getClassId());
        return ResponseUtil.ok(behaveClass);
    }
    @GetMapping("studentsByClass")
    public Object studentsByClass(String classId){
        if (StringUtils.isEmpty(classId))
            return ResponseUtil.fail();
        List<BehaveUser>users=userService.queryStudentByClassId(classId);
        return ResponseUtil.ok(users);
    }

    private List<ScheduleVo> initSchedule(List<BehaveScheduleValue> scheduleValues){
        if (scheduleValues==null&&scheduleValues.size()==0)
            return new ArrayList<>();
        List<ScheduleVo> scheduleVos=new ArrayList<>();
        scheduleValues.forEach(e->{
            BehaveScheduleSolt scheduleSolt=scheduleSoltService.findById(e.getSlotId());
            ScheduleVo scheduleVo=new ScheduleVo();
            scheduleVo.setScheduleSolt(scheduleSolt);
            scheduleVo.setScheduleValue(e);
            String slot= scheduleSolt.getTimeSlot();
            String[] slotValues=slot.split("~");
            int status=initTime(slotValues);//0未开始，1上课中，2已结束
            scheduleVo.setStatus(status);
            scheduleVos.add(scheduleVo);
        });
        return scheduleVos;
    }
    private int initTime(String [] values){
        //0未开始，1上课中，2已结束
        String[] start=values[0].split(":");
        if (start.length<2)
            start=values[0].split("：");
        String[] end=values[1].split(":");
        if (end.length<2)
            end=values[1].split("：");
        Integer intStart=Integer.parseInt(start[0]+start[1]);
        Integer intEnd=Integer.parseInt(end[0]+end[1]);
        int  minute= LocalDateTime.now().getMinute();
        String stringMinute="";
        if (minute<10)
            stringMinute="0"+minute;
        else
            stringMinute=""+minute;
        Integer nowTime=Integer.parseInt(LocalDateTime.now().getHour()+stringMinute);
        //0未开始，1上课中，2已结束
        if(nowTime<intStart)
            return 0;
        else if(nowTime>intEnd)
            return 2;
        else
            return 1;
    }
    @GetMapping("studentSchedule")
    public Object studentSchedule(String classId){
        BehaveClass behaveClass=classService.findById(Integer.valueOf(classId));
        BehaveSchedule schedule=scheduleService.findByClassId(classId);
        List<BehaveScheduleSolt>solts=scheduleSoltService.queryAll();
        if (solts==null||solts.size()==0||classId==null)
            return ResponseUtil.badArgumentValue();
        Map<String,List<BehaveScheduleValue>> scheduleValues=initScheduleValues(solts,schedule);
        Map<String,List<BehaveScheduleSolt>> values=initSlotValues(solts);
        Map<String,Object> map=new HashedMap();
        map.put("class",behaveClass);
        map.putAll(values);
        map.putAll(scheduleValues);
        return ResponseUtil.ok(map);
    }
    @GetMapping("teacherSchedule")
    public Object teacherSchedule(String teacherId){

        List<BehaveScheduleSolt>solts=scheduleSoltService.queryAll();
        if (teacherId==null||solts==null||solts.size()==0)
            return ResponseUtil.badArgumentValue();
        Map<String,List<BehaveScheduleSolt>> values=initSlotValues(solts);
        Map<String,Object> map=new HashedMap();
        map.putAll(values);
        BehaveUser user=userService.findById(Integer.valueOf(teacherId));
        Map<String,List<BehaveScheduleValue>> stringListMap=initScheduleValues(solts,user);
        map.putAll(stringListMap);
        return ResponseUtil.ok(map);
    }
    private Map<String,List<BehaveScheduleValue>> initScheduleValues(List<BehaveScheduleSolt> solts,BehaveSchedule schedule){
        Map<String,List<BehaveScheduleValue>> all=new HashedMap();
        List<BehaveScheduleValue> morning=new ArrayList<>();
        List<BehaveScheduleValue> noon=new ArrayList<>();
        List<BehaveScheduleValue> night=new ArrayList<>();
        Byte ZENO=0,ONE=1,TWO=2;
        solts.forEach(scheduleSolt -> {
            if (scheduleSolt.getLabel().equals(ZENO)){
                morning.addAll(scheduleService.queryBySortId(schedule.getId(),scheduleSolt.getId()));
            }else if (scheduleSolt.getLabel().equals(ONE)){
                noon.addAll(scheduleService.queryBySortId(schedule.getId(),scheduleSolt.getId()));
            }else if (scheduleSolt.getLabel().equals(TWO)){
                night.addAll(scheduleService.queryBySortId(schedule.getId(),scheduleSolt.getId()));
            }
        });
        all.put("morning",morning);
        all.put("noon",noon);
        all.put("night",night);
        //0早，1中，2晚
        return all;
    }
    private Map<String,List<BehaveScheduleValue>> initScheduleValues(List<BehaveScheduleSolt> solts,BehaveUser user){
        Map<String,List<BehaveScheduleValue>> all=new HashedMap();
        List<BehaveScheduleValue> morning=new ArrayList<>();
        List<BehaveScheduleValue> noon=new ArrayList<>();
        List<BehaveScheduleValue> night=new ArrayList<>();
        Byte ZENO=0,ONE=1,TWO=2;
        solts.forEach(scheduleSolt -> {
            if (scheduleSolt.getLabel().equals(ZENO)){
                morning.addAll(scheduleService.queryBySort(user.getId(),scheduleSolt.getId()));
            }else if (scheduleSolt.getLabel().equals(ONE)){
                noon.addAll(scheduleService.queryBySort(user.getId(),scheduleSolt.getId()));
            }else if (scheduleSolt.getLabel().equals(TWO)){
                night.addAll(scheduleService.queryBySort(user.getId(),scheduleSolt.getId()));
            }
        });
        all.put("morning",morning);
        all.put("noon",noon);
        all.put("night",night);
        //0早，1中，2晚
        return all;
    }
    private  Map<String,List<BehaveScheduleSolt>> initSlotValues(List<BehaveScheduleSolt>solts){
        Map<String,List<BehaveScheduleSolt>> all=new HashedMap();
        List<BehaveScheduleSolt> morning=new ArrayList<>();
        List<BehaveScheduleSolt> noon=new ArrayList<>();
        List<BehaveScheduleSolt> night=new ArrayList<>();
        Byte ZENO=0,ONE=1,TWO=2;
        solts.forEach(scheduleSolt -> {
            if (scheduleSolt.getLabel().equals(ZENO)){
                morning.add(scheduleSolt);
            }else if (scheduleSolt.getLabel().equals(ONE)){
                noon.add(scheduleSolt);
            }else if (scheduleSolt.getLabel().equals(TWO)){
                night.add(scheduleSolt);
            }
        });
        all.put("slotMorning",morning);
        all.put("slotNoon",noon);
        all.put("slotNight",night);
        return all;
    }
}
