package com.example.mentalhealth.entities;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ExamRecordQuestion {
    private Long id;             // ID
    private Long recordId;       // 答卷记录ID
    private Long questionId;     // 题目ID
    private String userAnswer;   // 用户答案
    private Boolean isCorrect;   // 是否正确(0: 错误, 1: 正确)
    private BigDecimal score;    // 得分
}
