package org.java.behave.admin.web;

import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.java.behave.admin.annotation.RequiresPermissionsDesc;
import org.java.behave.core.util.ResponseUtil;
import org.java.behave.core.validator.Order;
import org.java.behave.core.validator.Sort;
import org.java.behave.db.domain.BehaveClass;
import org.java.behave.db.domain.BehaveUser;
import org.java.behave.db.service.BehaveClassService;
import org.java.behave.db.service.BehaveUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

import static org.java.behave.admin.util.AdminResponseCode.STUDENT;

@RestController
@RequestMapping("/admin/student")
@Validated
public class AdminStudentController {
    @Autowired
    private BehaveUserService userService;

    @Autowired
    private BehaveClassService classService;
    @RequiresPermissions("admin:student:list")
    @RequiresPermissionsDesc(menu = {"功能中心", "学生管理"}, button = "查询")
    @GetMapping("/list")
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
    @GetMapping("/read")
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
}
