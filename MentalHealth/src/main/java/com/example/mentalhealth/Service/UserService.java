package com.example.mentalhealth.Service;


import com.example.mentalhealth.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    public UserMapper userMapper;

    /**
     * 通过用户名查询用户信息
     * @param username
     * @return
     */


}
