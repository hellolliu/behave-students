package org.java.behave.db.service;
import org.java.behave.db.dao.BehaveSystemMapper;
import org.java.behave.db.domain.BehaveSystem;
import org.java.behave.db.domain.BehaveSystemExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BehaveSystemService {
    @Resource
    private BehaveSystemMapper behaveSystemMapper;

    public List<BehaveSystem> queryAll(){
        BehaveSystemExample example=new BehaveSystemExample();
        BehaveSystemExample.Criteria criteria=example.or();
        return behaveSystemMapper.selectByExample(example );
    }
}
