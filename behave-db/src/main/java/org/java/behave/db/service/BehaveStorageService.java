package org.java.behave.db.service;

import com.github.pagehelper.PageHelper;
import org.java.behave.db.dao.BehaveStorageMapper;
import org.java.behave.db.domain.BehaveStorage;
import org.java.behave.db.domain.BehaveStorageExample;
import org.java.behave.db.domain.BehaveSystem;
import org.java.behave.db.domain.BehaveSystemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BehaveStorageService {
    @Autowired
    private BehaveStorageMapper storageMapper;

    public void deleteByKey(String key) {
        BehaveStorageExample example = new BehaveStorageExample();
        example.or().andKeyEqualTo(key);
        storageMapper.logicalDeleteByExample(example);
    }

    public void add(BehaveStorage storageInfo) {
        storageInfo.setAddTime(LocalDateTime.now());
        storageInfo.setUpdateTime(LocalDateTime.now());
        storageMapper.insertSelective(storageInfo);
    }

    public BehaveStorage findByKey(String key) {
        BehaveStorageExample example = new BehaveStorageExample();
        example.or().andKeyEqualTo(key).andDeletedEqualTo(false);
        return storageMapper.selectOneByExample(example);
    }

    public int update(BehaveStorage storageInfo) {
        storageInfo.setUpdateTime(LocalDateTime.now());
        return storageMapper.updateByPrimaryKeySelective(storageInfo);
    }

    public BehaveStorage findById(Integer id) {
        return storageMapper.selectByPrimaryKey(id);
    }

    public List<BehaveStorage> querySelective(String key, String name, Integer page, Integer limit, String sort, String order) {
        BehaveStorageExample example = new BehaveStorageExample();
        BehaveStorageExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(key)) {
            criteria.andKeyEqualTo(key);
        }
        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return storageMapper.selectByExample(example);
    }
}
