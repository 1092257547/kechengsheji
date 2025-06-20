package com.example.mentalhealth.Controller.AdminController;


import com.example.mentalhealth.Service.UserService;
import com.example.mentalhealth.entities.ResultData;
import com.example.mentalhealth.entities.UserStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//用户首页api
@RestController
@RequestMapping("/api")
public class AdminDashboard {
    @Autowired
    private UserService userService;

    /**
     * 获取统计数据
     * @return
     */
    @GetMapping("/getStatsData")
    public ResultData getStatsData(){
        UserStats userStats = new UserStats();
        //获取用户数量
        userStats.setUserCount(userService.queryUser().size());
        //获取试卷数量
        userStats.setExamCount(500);
        //获取答卷数量
        userStats.setAnswerCount(980);
        //获取今日答卷数量
        userStats.setTodayAnswerCount(10);
        return new ResultData(200,"success",userStats);
    }
}
