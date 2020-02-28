package org.java.behave.admin.web;

import org.apache.commons.collections.map.HashedMap;
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
import org.java.behave.db.domain.BehaveCourse;
import org.java.behave.db.domain.BehaveCourse;
import org.java.behave.db.domain.BehaveUser;
import org.java.behave.db.service.BehaveAdminService;
import org.java.behave.db.service.BehaveCourseService;
import org.java.behave.db.service.BehaveCourseService;
import org.java.behave.db.service.BehaveUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

import static org.java.behave.admin.util.AdminResponseCode.*;

@RestController
@RequestMapping("/admin/course")
@Validated
public class AdminCourseController {
    private final Log logger = LogFactory.getLog(AdminCourseController.class);

    @Autowired
    private BehaveCourseService courseService;
    @Autowired
    private BehaveUserService userService;
    @Autowired
    private LogHelper logHelper;

    @RequiresPermissions("admin:course:list")
    @RequiresPermissionsDesc(menu = {"功能中心", "课程管理"}, button = "查询")
    @GetMapping("/list")
    public Object list(String username,String mobile,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        List<BehaveUser> users= userService.queryByNameOrMobile(username,mobile);
        if (users==null||users.size()==0)
            return ResponseUtil.fail(USER_MOBILE_EXIST,"没有查到该教师");
        List<BehaveCourse> adminList = courseService.querySelective(username,mobile, page, limit, sort, order);
        Object listObj=ResponseUtil.okList(adminList);
        Map<String ,Object> map=new HashedMap();
        map.put("teacher",users.get(0));
        map.put("adminList",listObj);
        return ResponseUtil.ok(map);
    }

    @GetMapping("/all")
    public Object all() {
        List<BehaveCourse> courses= courseService.queryAll();
        return ResponseUtil.okList(courses);
    }
    private Object validate(BehaveCourse behaveClass) {
        String name = behaveClass.getName();
        if (StringUtils.isEmpty(name)) {
            return ResponseUtil.badArgument();
        }
        return null;
    }

    @RequiresPermissions("admin:course:create")
    @RequiresPermissionsDesc(menu = {"功能中心", "课程管理"}, button = "添加")
    @PostMapping("/create")
    public Object create(@RequestBody BehaveCourse behaveClass) {
        Object error = validate(behaveClass);
        if (error != null) {
            return error;
        }

        String username = behaveClass.getName();
        List<BehaveCourse> adminList = courseService.findAdmin(username);
        if (adminList.size() > 0) {
            return ResponseUtil.fail(ADMIN_NAME_EXIST, "课程已经存在");
        }
        courseService.add(behaveClass);
        return ResponseUtil.ok(behaveClass);
    }

    @RequiresPermissions("admin:course:read")
    @RequiresPermissionsDesc(menu = {"功能中心", "课程管理"}, button = "详情")
    @GetMapping("/read")
    public Object read(@NotNull Integer id) {
        BehaveCourse admin = courseService.findById(id);
        return ResponseUtil.ok(admin);
    }

    @RequiresPermissions("admin:course:update")
    @RequiresPermissionsDesc(menu = {"功能中心", "课程管理"}, button = "编辑")
    @PostMapping("/update")
    public Object update(@RequestBody BehaveCourse behaveClass) {
        Object error = validate(behaveClass);
        if (error != null) {
            return error;
        }

        Integer anotherAdminId = behaveClass.getId();
        if (anotherAdminId == null) {
            return ResponseUtil.badArgument();
        }

        if (courseService.updateById(behaveClass) == 0) {
            return ResponseUtil.updatedDataFailed();
        }

        return ResponseUtil.ok(behaveClass);
    }

    @RequiresPermissions("admin:course:delete")
    @RequiresPermissionsDesc(menu = {"功能中心", "课程管理"}, button = "删除")
    @PostMapping("/delete")
    public Object delete(@RequestBody BehaveCourse behaveClass) {
        Integer classId = behaveClass.getId();
        if (classId == null) {
            return ResponseUtil.badArgument();
        }
        courseService.deleteById(classId);
        logHelper.logAuthSucceed("课程班级", behaveClass.getName());
        return ResponseUtil.ok();
    }
}
