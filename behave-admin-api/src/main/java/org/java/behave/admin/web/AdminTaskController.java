package org.java.behave.admin.web;

import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.java.behave.admin.annotation.RequiresPermissionsDesc;
import org.java.behave.core.util.ResponseUtil;
import org.java.behave.core.util.bcrypt.BCryptPasswordEncoder;
import org.java.behave.core.validator.Order;
import org.java.behave.core.validator.Sort;
import org.java.behave.db.domain.BehaveQuestionItem;
import org.java.behave.db.domain.BehaveTeacherQuestion;
import org.java.behave.db.domain.BehaveUser;
import org.java.behave.db.service.BehaveQuestionItemService;
import org.java.behave.db.service.BehaveTeacherQuestionService;
import org.java.behave.db.service.BehaveUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/task")
@Validated
public class AdminTaskController {

    @Autowired
    private BehaveTeacherQuestionService teacherQuestionService;

    @Autowired
    private BehaveQuestionItemService questionItemService;

    @Autowired
    private BehaveUserService userService;

    @RequiresPermissions("admin:task:list")
    @RequiresPermissionsDesc(menu = {"功能中心", "练习管理"}, button = "查询")
    @GetMapping("/list")
    public Object list(String username, String mobile,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        BehaveUser user=userService.queryByPassAndMobile(username,mobile);
        if (user==null){
            return ResponseUtil.fail();
        }
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        String enPass=encoder.encode(username);
        if(!encoder.matches(user.getPassword(),enPass))
            return ResponseUtil.fail();
        List<BehaveTeacherQuestion> teacherQuestions = teacherQuestionService.querySelective(user.getId(), page, limit, sort, order);
        Map<String,Object> re=new HashedMap();
        re.put("user",user);
        re.put("teacherQuestions", ResponseUtil.okList(teacherQuestions));
        return ResponseUtil.ok(re);
    }
    private Object validate(BehaveTeacherQuestion teacherQuestion) {
        String name = teacherQuestion.getTitle();
        if (StringUtils.isEmpty(name)) {
            return ResponseUtil.badArgument();
        }
        return null;
    }

    @RequiresPermissions("admin:task:create")
    @RequiresPermissionsDesc(menu = {"功能中心", "练习管理"}, button = "添加")
    @PostMapping("/create")
    public Object create(@RequestBody BehaveTeacherQuestion teacherQuestion) {
        Object error = validate(teacherQuestion);
        if (error != null) {
            return error;
        }

        teacherQuestionService.add(teacherQuestion);
        return ResponseUtil.ok(teacherQuestion);
    }

    @RequiresPermissions("admin:task:read")
    @RequiresPermissionsDesc(menu = {"功能中心", "练习管理"}, button = "详情")
    @GetMapping("/read")
    public Object read(@NotNull Integer id) {
        BehaveTeacherQuestion teacherQuestion = teacherQuestionService.findById(id);
        return ResponseUtil.ok(teacherQuestion);
    }

    @RequiresPermissions("admin:task:update")
    @RequiresPermissionsDesc(menu = {"功能中心", "练习管理"}, button = "编辑")
    @PostMapping("/update")
    public Object update(@RequestBody BehaveTeacherQuestion teacherQuestion) {
        Object error = validate(teacherQuestion);
        if (error != null) {
            return error;
        }

        Integer teacherQuestionId = teacherQuestion.getId();
        if (teacherQuestionId == null) {
            return ResponseUtil.badArgument();
        }

        if (teacherQuestionService.update(teacherQuestion) == 0) {
            return ResponseUtil.updatedDataFailed();
        }

        return ResponseUtil.ok(teacherQuestion);
    }

    @RequiresPermissions("admin:task:delete")
    @RequiresPermissionsDesc(menu = {"功能中心", "练习管理"}, button = "删除")
    @PostMapping("/delete")
    public Object delete(@RequestBody BehaveTeacherQuestion teacherQuestion) {
        Integer questionId = teacherQuestion.getId();
        if (questionId == null) {
            return ResponseUtil.badArgument();
        }
        BehaveQuestionItem item=new BehaveQuestionItem();
        item.setQuestionId(questionId);
        List<BehaveQuestionItem>items=questionItemService.queryAll(item);
        if (items!=null&&items.size()!=0)
            return ResponseUtil.fail(500,"有数据关联，不能删除");
        teacherQuestionService.delete(questionId);
        return ResponseUtil.ok();
    }
}
