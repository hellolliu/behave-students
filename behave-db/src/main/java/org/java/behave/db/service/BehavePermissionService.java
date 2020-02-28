package org.java.behave.db.service;

import org.java.behave.db.dao.BehavePermissionMapper;
import org.java.behave.db.domain.BehavePermission;
import org.java.behave.db.domain.BehavePermissionExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BehavePermissionService {
    @Resource
    private BehavePermissionMapper permissionMapper;

    public Set<String> queryByRoleIds(Integer[] roleIds) {
        Set<String> permissions = new HashSet<String>();
        if(roleIds.length == 0){
            return permissions;
        }

        BehavePermissionExample example = new BehavePermissionExample();
        example.or().andRoleIdIn(Arrays.asList(roleIds)).andDeletedEqualTo(false);
        List<BehavePermission> permissionList = permissionMapper.selectByExample(example);

        for(BehavePermission permission : permissionList){
            permissions.add(permission.getPermission());
        }

        return permissions;
    }


    public Set<String> queryByRoleId(Integer roleId) {
        Set<String> permissions = new HashSet<String>();
        if(roleId == null){
            return permissions;
        }

        BehavePermissionExample example = new BehavePermissionExample();
        example.or().andRoleIdEqualTo(roleId).andDeletedEqualTo(false);
        List<BehavePermission> permissionList = permissionMapper.selectByExample(example);

        for(BehavePermission permission : permissionList){
            permissions.add(permission.getPermission());
        }

        return permissions;
    }

    public boolean checkSuperPermission(Integer roleId) {
        if(roleId == null){
            return false;
        }

        BehavePermissionExample example = new BehavePermissionExample();
        example.or().andRoleIdEqualTo(roleId).andPermissionEqualTo("*").andDeletedEqualTo(false);
        return permissionMapper.countByExample(example) != 0;
    }

    public void deleteByRoleId(Integer roleId) {
        BehavePermissionExample example = new BehavePermissionExample();
        example.or().andRoleIdEqualTo(roleId).andDeletedEqualTo(false);
        permissionMapper.logicalDeleteByExample(example);
    }

    public void add(BehavePermission BehavePermission) {
        BehavePermission.setAddTime(LocalDateTime.now());
        BehavePermission.setUpdateTime(LocalDateTime.now());
        permissionMapper.insertSelective(BehavePermission);
    }
}
