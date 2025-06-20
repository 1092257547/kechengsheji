package com.example.mentalhealth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.mentalhealth.entities.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    // 查询用户是否存在
    User queryUserByUsername(String username);

    // 根据信息查询用户
    List<User> selectByQuery(User user);

    // 更新用户
    int updateUserByUsername(@Param("username") String  username, @Param("user") User user);

    // 查询所有用户
    List<User> queryUser();

}
