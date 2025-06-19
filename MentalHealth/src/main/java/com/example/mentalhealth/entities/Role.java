package com.example.mentalhealth.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//用户角色实体类
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("role")
public class Role {
    @TableId(type = IdType.AUTO)
    private Integer rId;//角色id
    private  String rName;//角色名称
    private String rCode;//角色代码
    private  String pId;//用户权限id

    public Role(String rName, String rCode, String pId) {
        this.rName = rName;
        this.rCode = rCode;
        this.pId = pId;
    }
}
