package org.java.behave.db.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.java.behave.db.domain.BehavePermission;
import org.java.behave.db.domain.BehavePermissionExample;

public interface BehavePermissionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_permission
     *
     * @mbg.generated
     */
    long countByExample(BehavePermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_permission
     *
     * @mbg.generated
     */
    int deleteByExample(BehavePermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_permission
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_permission
     *
     * @mbg.generated
     */
    int insert(BehavePermission record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_permission
     *
     * @mbg.generated
     */
    int insertSelective(BehavePermission record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_permission
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    BehavePermission selectOneByExample(BehavePermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_permission
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    BehavePermission selectOneByExampleSelective(@Param("example") BehavePermissionExample example, @Param("selective") BehavePermission.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_permission
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    List<BehavePermission> selectByExampleSelective(@Param("example") BehavePermissionExample example, @Param("selective") BehavePermission.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_permission
     *
     * @mbg.generated
     */
    List<BehavePermission> selectByExample(BehavePermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_permission
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    BehavePermission selectByPrimaryKeySelective(@Param("id") Integer id, @Param("selective") BehavePermission.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_permission
     *
     * @mbg.generated
     */
    BehavePermission selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_permission
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    BehavePermission selectByPrimaryKeyWithLogicalDelete(@Param("id") Integer id, @Param("andLogicalDeleted") boolean andLogicalDeleted);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_permission
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") BehavePermission record, @Param("example") BehavePermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_permission
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") BehavePermission record, @Param("example") BehavePermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_permission
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(BehavePermission record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_permission
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(BehavePermission record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_permission
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    int logicalDeleteByExample(@Param("example") BehavePermissionExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table behave_permission
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    int logicalDeleteByPrimaryKey(Integer id);
}