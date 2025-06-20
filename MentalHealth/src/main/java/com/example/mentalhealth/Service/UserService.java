package com.example.mentalhealth.Service;


import com.example.mentalhealth.entities.User;
import com.example.mentalhealth.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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

    /**
     * 根据用户名等信息查询所有用户
     * @return
     */
    public PageInfo<User> queryUsersByPage(User user, int pageNum, int pageSize) {
        // 开始分页
        PageHelper.startPage(pageNum, pageSize);

        // 执行查询
        List<User> userList = userMapper.selectByQuery(user);

        // 封装分页信息
        return new PageInfo<>(userList);
    }

    /**
     * 根据用户名修改用户信息
     * @param username
     * @param user
     * @return
     */
    public int updateUserByUsername(String  username,User user){
        return userMapper.updateUserByUsername(username,user);
    }

    /**
     * 根据用户id删除用户
     * @param uId
     * @return
     */
    public boolean deleteUser(Integer uId) {
        return userMapper.deleteById(uId) > 0;
    }


    public List<User> queryUser() {
        return userMapper.queryUser();
    }





}
