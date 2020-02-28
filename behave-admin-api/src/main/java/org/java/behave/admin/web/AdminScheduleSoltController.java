package org.java.behave.admin.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.java.behave.admin.annotation.RequiresPermissionsDesc;
import org.java.behave.admin.service.LogHelper;
import org.java.behave.core.util.RegexUtil;
import org.java.behave.core.util.ResponseUtil;
import org.java.behave.core.util.bcrypt.BCryptPasswordEncoder;
import org.java.behave.core.validator.Order;
import org.java.behave.core.validator.Sort;
import org.java.behave.db.domain.BehaveAdmin;
import org.java.behave.db.domain.BehaveScheduleSolt;
import org.java.behave.db.domain.BehaveScheduleSolt;
import org.java.behave.db.service.BehaveAdminService;
import org.java.behave.db.service.BehaveScheduleSoltService;
import org.java.behave.db.service.BehaveScheduleSoltService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

import java.util.List;

import static org.java.behave.admin.util.AdminResponseCode.*;

@RestController
@RequestMapping("/admin/scheduleSolt")
@Validated
public class AdminScheduleSoltController {
    private final Log logger = LogFactory.getLog(AdminClassController.class);

    @Autowired
    private BehaveScheduleSoltService scheduleSoltService;
    @Autowired
    private LogHelper logHelper;

    @RequiresPermissions("admin:scheduleSolt:list")
    @RequiresPermissionsDesc(menu = {"功能中心", "课程表时间段管理"}, button = "查询")
    @GetMapping("/list")
    public Object list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @RequestParam(defaultValue = "id") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        List<BehaveScheduleSolt> adminList = scheduleSoltService.querySelective( page, limit, sort, order);
        return ResponseUtil.okList(adminList);
    }
    @GetMapping("/all")
    public Object all() {
        List<BehaveScheduleSolt> adminList = scheduleSoltService.queryAll();
        return ResponseUtil.okList(adminList);
    }
    private Object validate(BehaveScheduleSolt scheduleSolt) {
//        String name = scheduleSolt.getName();
//        if (StringUtils.isEmpty(name)) {
//            return ResponseUtil.badArgument();
//        }
        return null;
    }

    @RequiresPermissions("admin:scheduleSolt:create")
    @RequiresPermissionsDesc(menu = {"功能中心", "课程表时间段管理"}, button = "添加")
    @PostMapping("/create")
    public Object create(@RequestBody BehaveScheduleSolt scheduleSolt) {
        Object error = validate(scheduleSolt);
        if (error != null) {
            return error;
        }

        int count=scheduleSoltService.count();
        if (count>12){
            return ResponseUtil.fail(ADMIN_NAME_EXIST, "一天的课程太多了");
        }
        String timeSlot = scheduleSolt.getTimeSlot();
        List<BehaveScheduleSolt> soltTimes = scheduleSoltService.findSoltTime(timeSlot);
        if (soltTimes.size() > 0) {
            return ResponseUtil.fail(ADMIN_NAME_EXIST, "时间段已经存在");
        }
        scheduleSoltService.add(scheduleSolt);
        return ResponseUtil.ok(scheduleSolt);
    }

    @RequiresPermissions("admin:scheduleSolt:read")
    @RequiresPermissionsDesc(menu = {"功能中心", "课程表时间段管理"}, button = "详情")
    @GetMapping("/read")
    public Object read(@NotNull Integer id) {
        BehaveScheduleSolt admin = scheduleSoltService.findById(id);
        return ResponseUtil.ok(admin);
    }

    @RequiresPermissions("admin:scheduleSolt:update")
    @RequiresPermissionsDesc(menu = {"功能中心", "课程表时间段管理"}, button = "编辑")
    @PostMapping("/update")
    public Object update(@RequestBody BehaveScheduleSolt scheduleSolt) {
        Object error = validate(scheduleSolt);
        if (error != null) {
            return error;
        }

        Integer anotherAdminId = scheduleSolt.getId();
        if (anotherAdminId == null) {
            return ResponseUtil.badArgument();
        }

        if (scheduleSoltService.updateById(scheduleSolt) == 0) {
            return ResponseUtil.updatedDataFailed();
        }

        return ResponseUtil.ok(scheduleSolt);
    }

    @RequiresPermissions("admin:scheduleSolt:delete")
    @RequiresPermissionsDesc(menu = {"功能中心", "课程表时间段管理"}, button = "删除")
    @PostMapping("/delete")
    public Object delete(@RequestBody BehaveScheduleSolt scheduleSolt) {
        Integer classId = scheduleSolt.getId();
        if (classId == null) {
            return ResponseUtil.badArgument();
        }
        scheduleSoltService.deleteById(classId);
        return ResponseUtil.ok();
    }
}
