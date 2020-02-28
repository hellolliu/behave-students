package org.java.behave.admin.web;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.java.behave.admin.annotation.RequiresPermissionsDesc;
import org.java.behave.core.util.ResponseUtil;
import org.java.behave.core.util.bcrypt.BCryptPasswordEncoder;
import org.java.behave.core.validator.Sort;
import org.java.behave.db.domain.BehaveClass;
import org.java.behave.db.domain.BehaveUser;
import org.java.behave.db.service.BehaveClassService;
import org.java.behave.db.service.BehaveUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.java.behave.core.validator.Order;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

import static org.java.behave.admin.util.AdminResponseCode.ADMIN_NAME_EXIST;
import static org.java.behave.admin.util.AdminResponseCode.STUDENT;
import static org.java.behave.admin.util.AdminResponseCode.TEACHER;

@RestController
@RequestMapping("/admin/user")
@Validated
public class AdminUserController {
    private final Log logger = LogFactory.getLog(AdminUserController.class);

    @Autowired
    private BehaveUserService userService;

    @Autowired
    private BehaveClassService classService;
    @RequiresPermissions("admin:student:list")
    @RequiresPermissionsDesc(menu = {"功能中心", "学生管理"}, button = "查询")
    @GetMapping("/listStudent")
    public Object list(String username, String mobile,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        List<BehaveUser> userList = userService.querySelective(username, mobile, STUDENT,page, limit, sort, order);
        return ResponseUtil.okList(userList);
    }
    @RequiresPermissions("admin:student:read")
    @RequiresPermissionsDesc(menu = {"功能中心", "学生管理"}, button = "详情")
    @GetMapping("/readStudent")
    public Object readStudent(@NotNull Integer id) {
        BehaveUser student = userService.findById(id);
        Map<String ,Object> objectMap=new HashedMap();
        objectMap.put("student",student);
        BehaveClass behaveClass;//班级
        if (student.getClassId()!=null)
            behaveClass=classService.findById(student.getClassId());
        else
            behaveClass=new BehaveClass();
        objectMap.put("studentClass",behaveClass);
        return ResponseUtil.ok(objectMap);
    }
    @RequiresPermissions("admin:teacher:list")
    @RequiresPermissionsDesc(menu = {"功能中心", "教师管理"}, button = "查询")
    @GetMapping("/listTeacher")
    public Object listTeacher(String username, String mobile,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        List<BehaveUser> userList = userService.querySelective(username, mobile, TEACHER,page, limit, sort, order);
        return ResponseUtil.okList(userList);
    }
    private Object validate(BehaveUser behaveUser) {
        String name = behaveUser.getUsername();
        if (StringUtils.isEmpty(name)) {
            return ResponseUtil.badArgument();
        }
        String mobile=behaveUser.getMobile();
        if (StringUtils.isEmpty(mobile))
            return ResponseUtil.badArgument();
        return null;
    }

    @RequiresPermissions("admin:teahcer:create")
    @RequiresPermissionsDesc(menu = {"功能中心", "教师管理"}, button = "添加")
    @PostMapping("/create")
    public Object create(@RequestBody BehaveUser behaveUser) {
        Object error = validate(behaveUser);
        if (error != null) {
            return error;
        }

        String mobile = behaveUser.getMobile();
        List<BehaveUser> adminList = userService.queryByMobile(mobile);
        if (adminList.size() > 0) {
            return ResponseUtil.fail(ADMIN_NAME_EXIST, "教师已经存在");
        }
        initTeacher(behaveUser);
        userService.add(behaveUser);
        return ResponseUtil.ok(behaveUser);
    }

    private void initTeacher(BehaveUser user){
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        user.setPassword(encoder.encode("123"));
        user.setAvatar("https://yanxuan.nosdn.127.net/80841d741d7fa3073e0ae27bf487339f.jpg?imageView&quality=90&thumbnail=64x64");
    }
    @RequiresPermissions("admin:teacher:read")
    @RequiresPermissionsDesc(menu = {"功能中心", "教师管理"}, button = "详情")
    @GetMapping("/read")
    public Object read(@NotNull Integer id) {
        BehaveUser admin = userService.findById(id);
        return ResponseUtil.ok(admin);
    }

    @RequiresPermissions("admin:teacher:update")
    @RequiresPermissionsDesc(menu = {"功能中心", "教师管理"}, button = "编辑")
    @PostMapping("/update")
    public Object update(@RequestBody BehaveUser behaveUser) {
        Object error = validate(behaveUser);
        if (error != null)
            return error;

        Integer anotherAdminId = behaveUser.getId();
        if (anotherAdminId == null)
            return ResponseUtil.badArgument();

        if (userService.updateById(behaveUser) == 0)
            return ResponseUtil.updatedDataFailed();

        return ResponseUtil.ok(behaveUser);
    }

    @RequiresPermissions("admin:teacher:delete")
    @RequiresPermissionsDesc(menu = {"功能中心", "教师管理"}, button = "删除")
    @PostMapping("/delete")
    public Object delete(@RequestBody BehaveUser behaveUser) {
        Integer classId = behaveUser.getId();
        if (classId == null) {
            return ResponseUtil.badArgument();
        }
        userService.deleteById(classId);
        return ResponseUtil.ok();
    }
}
