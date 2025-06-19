package com.example.mentalhealth.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//用户权限实体类
//@TableName的值是数据库里面的表名
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("permission")
public class Permission {
    /**
     * 数据库已经设置主键自增，这个对方也需要，这里也需要通过@TableId指定自增
     * 如果不指定，会按照Mybatis-plus的算法进行自增
     */
    @TableId(value = "p_id",type = IdType.AUTO)
    private Integer pId;//权限id
    private String pName;//权限名称
    private String pCode;//权限代码
    private String desc;//权限说明

}
