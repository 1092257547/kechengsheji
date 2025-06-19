package com.example.mentalhealth.Service;


import com.example.mentalhealth.entities.User;
import com.example.mentalhealth.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    public UserMapper userMapper;

    /**
     * 根据用户名查询用户是否存在
     * @param username
     * @return
     */
    public User queryUserByUsername(String username){
       return userMapper.queryUserByUsername(username);
    }

    /**
     * 用户信息注册
     * @param user
     * @return
     */
    public int register(User user){
        //如果注册成功，则返回1
        if(userMapper.insert(user)>0){
            return 1;
        }
        else return 0;
    }





}
