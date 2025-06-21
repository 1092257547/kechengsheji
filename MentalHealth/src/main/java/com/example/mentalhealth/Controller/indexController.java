package com.example.mentalhealth.Controller;

import com.example.mentalhealth.Service.UserService;
import com.example.mentalhealth.entities.ResultData;
import com.example.mentalhealth.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//与前端交互数据
@RestController
@RequestMapping("/api")
public class indexController {
    @Autowired
    private UserService userService;

    /**
     *  用户注册
     *  将前端输入的数据传入后端进行存储
     * @param user
     * @return
     */
    @PostMapping("/register")
    public ResultData register(@RequestBody User user) {
        // 检查用户是否已存在
        if (userService.queryUserByUsername(user.getUsername())!=null) {
            return new ResultData(400, "用户已存在");
        }
        // 设置用户角色为普通用户
        user.setRId(2);
        System.out.println(user.getRId());
        // 保存用户
        int i = userService.register(user);
        if(i==0){
            return new ResultData(500, "注册失败");
        }
        return new ResultData(200, "注册成功");
    }

    /**
     * 用户登录
     * @param loginRequest
     * @return
     */
    @PostMapping("/login")
    public ResultData login(@RequestBody User loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        System.out.println(username);

        User user = userService.queryUserByUsername(username);// 根据用户名查询用户
        if (user == null) {
            return new ResultData(400, "用户不存在");
        }
        if (!user.getPassword().equals(password)) {
            return new ResultData(400, "密码错误");
        }
        return new ResultData(200, "登录成功", user);

    }
}
