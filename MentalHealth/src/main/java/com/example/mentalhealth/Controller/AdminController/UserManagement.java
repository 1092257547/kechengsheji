package com.example.mentalhealth.Controller.AdminController;

import com.example.mentalhealth.Service.UserService;
import com.example.mentalhealth.entities.ResultData;
import com.example.mentalhealth.entities.User;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//用户管理
@RestController
@RequestMapping("/api")
public class UserManagement {

    @Autowired
    private UserService userService;

    // 获取用户列表（支持分页和搜索）
    @GetMapping("/GetUsers")
    public ResultData getUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String sex,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        System.out.println("GetUsers: page=" + page + ", size=" + size);

        // 创建查询条件对象
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setSex(sex);

        // 调用带分页的查询方法
        PageInfo<User> pageInfo = userService.queryUsersByPage(user, page, size);

        return new ResultData(200, "success", pageInfo);
    }

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    @PostMapping("/AddUser")
    public ResultData addUser(@RequestBody User user) {
        System.out.println("AddUser: " + user);
        int result = userService.register(user);
        if (result > 0) {
            return new ResultData(200, "success", null);
        } else {
            return new ResultData(500, "error", null);
        }
    }

    /**
     * 修改用户
     *
     * @param user
     * @return
     */
    @PostMapping("/updateUser")
    public ResultData updateUser(@RequestBody User user) {
        System.out.println("updateUser: " + user);
        int result = userService.updateUserByUsername(user.getUsername(), user);
        if (result > 0) {
            return new ResultData(200, "success");
        } else {
            return new ResultData(500, "error");
        }
    }


    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @DeleteMapping("/deleteUser/{id}")
    public ResultData deleteUser(@PathVariable Integer id) {
        System.out.println("deleteUser: " + id);
        boolean success = userService.deleteUser(id);
        if (success) {
            return new ResultData(200, "用户删除成功", null);
        } else {
            return new ResultData(404, "用户不存在", null);
        }
    }

}
