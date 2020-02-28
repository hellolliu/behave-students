package org.java.behave.db.service;

import com.github.pagehelper.PageHelper;
import org.java.behave.db.dao.BehaveLogMapper;
import org.java.behave.db.domain.BehaveLog;
import org.java.behave.db.domain.BehaveLogExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BehaveLogService {
    @Resource
    private BehaveLogMapper logMapper;

    public void deleteById(Integer id) {
        logMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(BehaveLog log) {
        log.setAddTime(LocalDateTime.now());
        log.setUpdateTime(LocalDateTime.now());
        logMapper.insertSelective(log);
    }

    public List<BehaveLog> querySelective(String name, Integer page, Integer size, String sort, String order) {
        BehaveLogExample example = new BehaveLogExample();
        BehaveLogExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(name)) {
            criteria.andAdminLike("%" + name + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return logMapper.selectByExample(example);
    }
}
