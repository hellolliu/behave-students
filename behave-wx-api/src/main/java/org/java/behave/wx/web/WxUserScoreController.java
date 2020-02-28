package org.java.behave.wx.web;

import org.java.behave.core.util.ResponseUtil;
import org.java.behave.db.domain.BehaveUserScore;
import org.java.behave.db.service.BehaveUserScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wx/score")
@Validated
public class WxUserScoreController {
    @Autowired
    private BehaveUserScoreService scoreService;

    @PostMapping("add")
    public Object add(@RequestBody BehaveUserScore userScore){
        scoreService.addQuestion(userScore);
        return ResponseUtil.ok();
    }
}
