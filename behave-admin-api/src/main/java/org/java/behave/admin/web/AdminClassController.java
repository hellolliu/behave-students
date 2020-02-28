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
import org.java.behave.db.domain.BehaveClass;
import org.java.behave.db.service.BehaveAdminService;
import org.java.behave.db.service.BehaveClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

import java.util.List;

import static org.java.behave.admin.util.AdminResponseCode.*;

@RestController
@RequestMapping("/admin/class")
@Validated
public class AdminClassController {
    private final Log logger = LogFactory.getLog(AdminClassController.class);

    @Autowired
    private BehaveClassService classService;
    @Autowired
    private LogHelper logHelper;

    @RequiresPermissions("admin:class:list")
    @RequiresPermissionsDesc(menu = {"功能中心", "班级管理"}, button = "查询")
    @GetMapping("/list")
    public Object list(String username,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        List<BehaveClass> adminList = classService.querySelective(username, page, limit, sort, order);
        return ResponseUtil.okList(adminList);
    }

    @GetMapping("/all")
    public Object all() {
        List<BehaveClass> adminList = classService.queryAll();
        return ResponseUtil.okList(adminList);
    }
    private Object validate(BehaveClass behaveClass) {
        String name = behaveClass.getName();
        if (StringUtils.isEmpty(name)) {
            return ResponseUtil.badArgument();
        }
        return null;
    }

    @RequiresPermissions("admin:class:create")
    @RequiresPermissionsDesc(menu = {"功能中心", "班级管理"}, button = "添加")
    @PostMapping("/create")
    public Object create(@RequestBody BehaveClass behaveClass) {
        Object error = validate(behaveClass);
        if (error != null) {
            return error;
        }

        String username = behaveClass.getName();
        List<BehaveClass> adminList = classService.findAdmin(username);
        if (adminList.size() > 0) {
            return ResponseUtil.fail(ADMIN_NAME_EXIST, "班级已经存在");
        }
        classService.add(behaveClass);
        return ResponseUtil.ok(behaveClass);
    }

    @RequiresPermissions("admin:class:read")
    @RequiresPermissionsDesc(menu = {"功能中心", "班级管理"}, button = "详情")
    @GetMapping("/read")
    public Object read(@NotNull Integer id) {
        BehaveClass admin = classService.findById(id);
        return ResponseUtil.ok(admin);
    }

    @RequiresPermissions("admin:class:update")
    @RequiresPermissionsDesc(menu = {"功能中心", "班级管理"}, button = "编辑")
    @PostMapping("/update")
    public Object update(@RequestBody BehaveClass behaveClass) {
        Object error = validate(behaveClass);
        if (error != null) {
            return error;
        }

        Integer anotherAdminId = behaveClass.getId();
        if (anotherAdminId == null) {
            return ResponseUtil.badArgument();
        }

        if (classService.updateById(behaveClass) == 0) {
            return ResponseUtil.updatedDataFailed();
        }

        return ResponseUtil.ok(behaveClass);
    }

    @RequiresPermissions("admin:class:delete")
    @RequiresPermissionsDesc(menu = {"功能中心", "班级管理"}, button = "删除")
    @PostMapping("/delete")
    public Object delete(@RequestBody BehaveClass behaveClass) {
        Integer classId = behaveClass.getId();
        if (classId == null) {
            return ResponseUtil.badArgument();
        }
        classService.deleteById(classId);
        logHelper.logAuthSucceed("删除班级", behaveClass.getName());
        return ResponseUtil.ok();
    }
}
