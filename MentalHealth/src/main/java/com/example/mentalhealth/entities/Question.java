package com.example.mentalhealth.entities;

import lombok.Data;
import java.util.Date;
import java.util.List;

// 题目类
@Data
public class Question {
    private Long id;            // 题目ID
    private Integer type;       // 题目类型(1: 单选题, 2: 多选题, 3: 判断题, 4: 填空题, 5: 简答题)
    private String content;     // 题目内容
    private List<QuestionOption> options; // 选项列表(选择题)
    private String answer;      // 答案
    private String explanation; // 答案解析
    private Date createTime;    // 创建时间
}

// 选项类
@Data
class QuestionOption {
    private String key;         // 选项标识(A, B, C, D...)
    private String value;       // 选项内容
}