package org.java.behave.admin.web;

import org.java.behave.core.util.ResponseUtil;
import org.java.behave.core.validator.Order;
import org.java.behave.core.validator.Sort;
import org.java.behave.db.domain.BehaveQuestionItem;
import org.java.behave.db.service.BehaveQuestionItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/admin/taskitem")
public class AdminTaskItemController {
    @Autowired
    private BehaveQuestionItemService questionItemService;

    @GetMapping("/list")
    public Object list(Integer quetionid,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {

        List<BehaveQuestionItem> questionItems = questionItemService.querySelective(quetionid, page, limit, sort, order);
        return ResponseUtil.okList(questionItems);
    }
    private Object validate(BehaveQuestionItem behaveQuestionItem) {
        String name = behaveQuestionItem.getTitle();
        if (StringUtils.isEmpty(name)) {
            return ResponseUtil.badArgument();
        }
        return null;
    }

    @PostMapping("/create")
    public Object create(@RequestBody BehaveQuestionItem questionItem) {
        Object error = validate(questionItem);
        if (error != null) {
            return error;
        }

        questionItemService.add(questionItem);
        return ResponseUtil.ok(questionItem);
    }


    @GetMapping("/read")
    public Object read(@NotNull Integer id) {
        BehaveQuestionItem questionItem = questionItemService.findById(id);
        return ResponseUtil.ok(questionItem);
    }

    @PostMapping("/update")
    public Object update(@RequestBody BehaveQuestionItem questionItem) {
        Object error = validate(questionItem);
        if (error != null) {
            return error;
        }

        Integer teacherQuestionId = questionItem.getId();
        if (teacherQuestionId == null) {
            return ResponseUtil.badArgument();
        }

        if (questionItemService.update(questionItem) == 0) {
            return ResponseUtil.updatedDataFailed();
        }

        return ResponseUtil.ok(questionItem);
    }

    @PostMapping("/delete")
    public Object delete(@RequestBody BehaveQuestionItem questionItem) {
        Integer classId = questionItem.getId();
        if (classId == null) {
            return ResponseUtil.badArgument();
        }
        questionItemService.delete(classId);
        return ResponseUtil.ok();
    }
}
