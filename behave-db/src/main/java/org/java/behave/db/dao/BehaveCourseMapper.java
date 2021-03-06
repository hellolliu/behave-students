package org.java.behave.db.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.java.behave.db.domain.BehaveCourse;
import org.java.behave.db.domain.BehaveCourseExample;

public interface BehaveCourseMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_course
     *
     * @mbg.generated
     */
    long countByExample(BehaveCourseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_course
     *
     * @mbg.generated
     */
    int deleteByExample(BehaveCourseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_course
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_course
     *
     * @mbg.generated
     */
    int insert(BehaveCourse record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_course
     *
     * @mbg.generated
     */
    int insertSelective(BehaveCourse record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_course
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    BehaveCourse selectOneByExample(BehaveCourseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_course
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    BehaveCourse selectOneByExampleSelective(@Param("example") BehaveCourseExample example, @Param("selective") BehaveCourse.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_course
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    List<BehaveCourse> selectByExampleSelective(@Param("example") BehaveCourseExample example, @Param("selective") BehaveCourse.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_course
     *
     * @mbg.generated
     */
    List<BehaveCourse> selectByExample(BehaveCourseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_course
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    BehaveCourse selectByPrimaryKeySelective(@Param("id") Integer id, @Param("selective") BehaveCourse.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_course
     *
     * @mbg.generated
     */
    BehaveCourse selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_course
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    BehaveCourse selectByPrimaryKeyWithLogicalDelete(@Param("id") Integer id, @Param("andLogicalDeleted") boolean andLogicalDeleted);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_course
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") BehaveCourse record, @Param("example") BehaveCourseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_course
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") BehaveCourse record, @Param("example") BehaveCourseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_course
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(BehaveCourse record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_course
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(BehaveCourse record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_course
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    int logicalDeleteByExample(@Param("example") BehaveCourseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_course
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    int logicalDeleteByPrimaryKey(Integer id);
}