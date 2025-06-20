package com.example.mentalhealth.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("user")
public class User {
    @TableId(value = "uId",type = IdType.AUTO)
    private Integer uId;//用户id
    private String username;//账号
    private String Name;//账户名
    private String password;//登录密码
    private String email;//邮箱
    private String phone;//电话
    private String sex;//性别
    private Integer rId;//角色id
    private String creatTime;//用户创建时间

    private String profileSrc;//头像地址

    public User(String username, String password, Integer rId) {
        this.username = username;
        this.password=password;
        this.rId = rId;
    }

}
