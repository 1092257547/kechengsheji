package com.example.mentalhealth.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


// 用以封装数据
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserStats {
    private Integer userCount;//用户数量
    private Integer examCount;//试卷数量
    private Integer answerCount;//答卷数量
    private Integer todayAnswerCount;//今日新增答卷

}
