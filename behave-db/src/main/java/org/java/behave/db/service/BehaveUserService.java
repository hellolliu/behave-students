package org.java.behave.db.service;

import com.github.pagehelper.PageHelper;
import org.java.behave.db.dao.BehaveUserMapper;
import org.java.behave.db.domain.BehaveCourse;
import org.java.behave.db.domain.BehaveUser;
import org.java.behave.db.domain.BehaveUserExample;
import org.java.behave.db.domain.UserVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.swing.plaf.basic.BasicHTML;
import java.sql.Struct;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BehaveUserService {
    @Resource
    private BehaveUserMapper userMapper;
    public final Byte STUDENT=1;
    public final Byte TEACHER=0;//0 老师，1 学生
    public BehaveUser findById(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    public UserVo findUserVoById(Integer userId) {
        BehaveUser user = findById(userId);
        UserVo userVo = new UserVo();
        userVo.setNickname(user.getNickname());
        userVo.setAvatar(user.getAvatar());
        return userVo;
    }
    public List<BehaveUser> queryStudentByClassId (String classId){
        BehaveUserExample example = new BehaveUserExample();
        example.or().andClassIdEqualTo(Integer.valueOf(classId)).andUserTypeEqualTo(STUDENT).andDeletedEqualTo(false);
        return userMapper.selectByExample(example);
    }

    public BehaveUser queryByOid(String openId) {
        BehaveUserExample example = new BehaveUserExample();
        example.or().andWeixinOpenidEqualTo(openId).andDeletedEqualTo(false);
        return userMapper.selectOneByExample(example);
    }

    public void add(BehaveUser user) {
        user.setAddTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insertSelective(user);
    }

    public int updateById(BehaveUser user) {
        user.setUpdateTime(LocalDateTime.now());
        return userMapper.updateByPrimaryKeySelective(user);
    }

    public List<BehaveUser> querySelective(String username, String mobile,Byte userType, Integer page, Integer size, String sort, String order) {
        BehaveUserExample example = new BehaveUserExample();
        BehaveUserExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(username)) {
            criteria.andUsernameLike("%" + username + "%");
        }
        if (!StringUtils.isEmpty(mobile)) {
            criteria.andMobileEqualTo(mobile);
        }
        if (userType!=null) {
            criteria.andUserTypeEqualTo(userType);
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return userMapper.selectByExample(example);
    }

    public int count() {
        BehaveUserExample example = new BehaveUserExample();
        example.or().andDeletedEqualTo(false);

        return (int) userMapper.countByExample(example);
    }

    public List<BehaveUser> queryByUsername(String username) {
        BehaveUserExample example = new BehaveUserExample();
        example.or().andUsernameEqualTo(username).andDeletedEqualTo(false);
        return userMapper.selectByExample(example);
    }

    public boolean checkByUsername(String username) {
        BehaveUserExample example = new BehaveUserExample();
        example.or().andUsernameEqualTo(username).andDeletedEqualTo(false);
        return userMapper.countByExample(example) != 0;
    }

    public List<BehaveUser> queryByMobile(String mobile) {
        BehaveUserExample example = new BehaveUserExample();
        example.or().andMobileEqualTo(mobile).andDeletedEqualTo(false);
        return userMapper.selectByExample(example);
    }

    public BehaveUser queryByPassAndMobile(String password,String mobile){
        BehaveUserExample example = new BehaveUserExample();
        BehaveUserExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isEmpty(mobile)|| StringUtils.isEmpty(password))
            return null;
        if (!StringUtils.isEmpty(mobile)) {
            criteria.andMobileEqualTo(mobile);
        }
        List<BehaveUser>users=userMapper.selectByExample(example);
        if(users==null||users.size()>1) return null;
        return users.get(0);
    }
    public List<BehaveUser> queryByNameOrMobile(String name,String mobile){
        BehaveUserExample example = new BehaveUserExample();
        BehaveUserExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isEmpty(name)&&StringUtils.isEmpty(mobile))
            return null;
        if (!StringUtils.isEmpty(name)) {
            criteria.andUsernameLike("%" + name + "%");
        }
        if (!StringUtils.isEmpty(mobile)) {
            criteria.andMobileEqualTo(mobile);
        }
        return userMapper.selectByExample(example);
    }
    public List<BehaveUser> queryByOpenid(String openid) {
        BehaveUserExample example = new BehaveUserExample();
        example.or().andWeixinOpenidEqualTo(openid).andDeletedEqualTo(false);
        return userMapper.selectByExample(example);
    }

    public void deleteById(Integer id) {
        userMapper.logicalDeleteByPrimaryKey(id);
    }
}
