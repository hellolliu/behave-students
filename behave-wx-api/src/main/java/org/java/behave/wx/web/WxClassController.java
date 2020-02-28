package org.java.behave.wx.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.java.behave.core.util.ResponseUtil;
import org.java.behave.db.domain.BehaveClass;
import org.java.behave.db.service.BehaveClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/wx/class")
@Validated
public class WxClassController {
    private final Log log= LogFactory.getLog(WxClassController.class);
    @Autowired
    private BehaveClassService classService;

    @GetMapping("/all")
    public Object listAll(){
        List<BehaveClass> classes=classService.queryAll();
        return ResponseUtil.ok(classes);
    }
}
