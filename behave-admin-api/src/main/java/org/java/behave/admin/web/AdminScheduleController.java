package org.java.behave.admin.web;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.java.behave.admin.annotation.RequiresPermissionsDesc;
import org.java.behave.admin.service.LogHelper;
import org.java.behave.admin.vo.ScheduleVo;
import org.java.behave.core.util.ResponseUtil;
import org.java.behave.core.validator.Order;
import org.java.behave.db.domain.BehaveSchedule;
import org.java.behave.db.domain.BehaveScheduleSolt;
import org.java.behave.db.domain.BehaveScheduleValue;
import org.java.behave.db.service.BehaveClassService;
import org.java.behave.db.service.BehaveScheduleService;
import org.java.behave.db.service.BehaveScheduleSoltService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/admin/schedule")
@Validated
public class AdminScheduleController {

    @Autowired
    private BehaveScheduleService scheduleService;

    @Autowired
    private LogHelper logHelper;

    @RequiresPermissions("admin:schedule:list")
    @RequiresPermissionsDesc(menu = {"功能中心", "课程表管理"}, button = "查询")
    @GetMapping("/list")
    public Object list(String classId) {
        if (StringUtils.isEmpty(classId))
            return ResponseUtil.badArgument();
        BehaveSchedule schedule=scheduleService.findByClassId(classId);
        List<ScheduleVo> scheduleVos=new ArrayList<>();
        for(int i=0;i<7;i++){
            List<BehaveScheduleValue> scheduleValues=scheduleService.queryScheduleVo(schedule.getId(),i+"");
            ScheduleVo scheduleVo=new ScheduleVo(i+"","",scheduleValues);
            scheduleVos.add(scheduleVo);
        }
        scheduleVos.get(0).setLabel("周一");
        scheduleVos.get(1).setLabel("周二");
        scheduleVos.get(2).setLabel("周三");
        scheduleVos.get(3).setLabel("周四");
        scheduleVos.get(4).setLabel("周五");
        scheduleVos.get(5).setLabel("周六");
        scheduleVos.get(6).setLabel("周七");
        return ResponseUtil.ok(scheduleVos);
    }

    @PostMapping("/delete")
    public Object delete(String classId,String week,String slotFd) {
        if (StringUtils.isEmpty(classId)||StringUtils.isEmpty(week)||StringUtils.isEmpty(slotFd)) {
            return ResponseUtil.badArgument();
        }
        BehaveSchedule schedule=scheduleService.findByClassId(classId);
        scheduleService.deleteValue(schedule.getId(),week,slotFd);
        return ResponseUtil.ok();
    }
    @PostMapping("/updateValue")
    public Object updateValue(@RequestBody BehaveScheduleValue scheduleValue) {
        if (scheduleValue.getCourseId() == null||
                scheduleValue.getScheduleId() == null||
                scheduleValue.getWeek() == null||scheduleValue.getSlotOrderFie()==null) {
            return ResponseUtil.badArgument();
        }
        scheduleService.updateValue(scheduleValue);
        return ResponseUtil.ok();
    }
}
