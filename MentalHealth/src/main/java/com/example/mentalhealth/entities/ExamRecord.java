package com.example.mentalhealth.entities;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

// 考试记录
@Data
public class ExamRecord {
    private Long id;            // 记录ID
    private Long userId;        // 用户ID
    private Long paperId;       // 试卷ID
    private Date startTime;     // 开始时间
    private Date submitTime;    // 提交时间
    private Integer duration;   // 用时(分钟)
    private BigDecimal score;   // 得分
    private Date createTime;    // 创建时间
}