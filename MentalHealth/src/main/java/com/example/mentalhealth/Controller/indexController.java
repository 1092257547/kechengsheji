package com.example.mentalhealth.Controller;

import com.example.mentalhealth.Service.UserService;
import com.example.mentalhealth.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

//与前端交互数据
@RestController
@RequestMapping("/api")
public class indexController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        /*// 检查用户名是否已存在
        if (userService.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("用户名已存在");
        }

        // 保存用户
        userService.save(user);*/

        Map<String, Object> response = new HashMap<>();
        response.put("message", "注册成功");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        System.out.println(username+"---"+password);

    /*    User user = userService.findByUsername(username);

        if (user == null || !userService.validatePassword(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户名或密码错误");
        }*/

        // 这里可以生成JWT令牌
        Map<String, Object> response = new HashMap<>();
        response.put("message", "登录成功");
        //response.put("user", user);

        return ResponseEntity.ok(response);
    }
}
