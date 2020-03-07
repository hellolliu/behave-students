package org.java.behave.db.service;

import com.github.pagehelper.PageHelper;
import org.java.behave.db.dao.BehaveCourseMapper;
import org.java.behave.db.dao.BehaveUserMapper;
import org.java.behave.db.domain.BehaveCourse;
import org.java.behave.db.domain.BehaveCourse.Column;
import org.java.behave.db.domain.BehaveCourseExample;
import org.java.behave.db.domain.BehaveUser;
import org.java.behave.db.domain.BehaveUserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BehaveCourseService {
    @Autowired
    private BehaveCourseMapper behaveCourseMapper;

    @Autowired
    private BehaveUserMapper userMapper;

    private final Column[] result = new Column[]{BehaveCourse.Column.id, Column.name,Column.address,Column.userId,Column.userName};
    public List<BehaveCourse> queryAll(){
        BehaveCourseExample example=new BehaveCourseExample();
        example.or().andDeletedEqualTo(false);
        return behaveCourseMapper.selectByExampleSelective(example,result);
    }
    public List<BehaveCourse> querySelective(Integer id, Integer page, Integer limit, String sort, String order) {

        if (id!=null){
            BehaveCourseExample example = new BehaveCourseExample();
            BehaveCourseExample.Criteria criteria = example.createCriteria();
            criteria.andUserIdEqualTo(id);
            criteria.andDeletedEqualTo(false);
            if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
                example.setOrderByClause(sort + " " + order);
            }
            PageHelper.startPage(page, limit);
            return behaveCourseMapper.selectByExample(example);
        }
        return new ArrayList<BehaveCourse>() ;
    }
    public List<BehaveCourse> findAdmin(String username) {
        BehaveCourseExample example = new BehaveCourseExample();
        example.or().andNameEqualTo(username).andDeletedEqualTo(false);
        return behaveCourseMapper.selectByExample(example);
    }
    public int updateById(BehaveCourse behaveClass) {
        behaveClass.setUpdateTime(LocalDateTime.now());
        return behaveCourseMapper.updateByPrimaryKeySelective(behaveClass);
    }

    public void deleteById(Integer id) {
        behaveCourseMapper.deleteByPrimaryKey(id);
    }

    public void add(BehaveCourse behaveClass) {
        behaveClass.setAddTime(LocalDateTime.now());
        behaveClass.setDeleted(false);
        behaveCourseMapper.insertSelective(behaveClass);
    }

    public BehaveCourse findById(Integer id) {
        return behaveCourseMapper.selectByPrimaryKey(id);
    }

}
