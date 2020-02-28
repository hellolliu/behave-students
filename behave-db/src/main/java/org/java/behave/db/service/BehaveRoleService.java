package org.java.behave.db.service;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import org.java.behave.db.dao.BehaveRoleMapper;
import org.java.behave.db.domain.BehaveRole;
import org.java.behave.db.domain.BehaveRoleExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BehaveRoleService {
    @Resource
    private BehaveRoleMapper roleMapper;


    public Set<String> queryByIds(Integer[] roleIds) {
        Set<String> roles = new HashSet<String>();
        if(roleIds.length == 0){
            return roles;
        }

        BehaveRoleExample example = new BehaveRoleExample();
        example.or().andIdIn(Arrays.asList(roleIds)).andEnabledEqualTo(true).andDeletedEqualTo(false);
        List<BehaveRole> roleList = roleMapper.selectByExample(example);

        for(BehaveRole role : roleList){
            roles.add(role.getName());
        }

        return roles;

    }

    public List<BehaveRole> querySelective(String name, Integer page, Integer limit, String sort, String order) {
        BehaveRoleExample example = new BehaveRoleExample();
        BehaveRoleExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return roleMapper.selectByExample(example);
    }

    public BehaveRole findById(Integer id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    public void add(BehaveRole role) {
        role.setAddTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());
        roleMapper.insertSelective(role);
    }

    public void deleteById(Integer id) {
        roleMapper.logicalDeleteByPrimaryKey(id);
    }

    public void updateById(BehaveRole role) {
        role.setUpdateTime(LocalDateTime.now());
        roleMapper.updateByPrimaryKeySelective(role);
    }

    public boolean checkExist(String name) {
        BehaveRoleExample example = new BehaveRoleExample();
        example.or().andNameEqualTo(name).andDeletedEqualTo(false);
        return roleMapper.countByExample(example) != 0;
    }

    public List<BehaveRole> queryAll() {
        BehaveRoleExample example = new BehaveRoleExample();
        example.or().andDeletedEqualTo(false);
        return roleMapper.selectByExample(example);
    }
}
