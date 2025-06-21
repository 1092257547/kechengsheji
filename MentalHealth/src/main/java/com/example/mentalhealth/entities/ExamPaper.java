package com.example.mentalhealth.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


// 试卷
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("exam_paper")
public class ExamPaper {
    @TableId(type = IdType.AUTO)
    private Long id;            // 试卷ID
    private String title;       // 试卷标题
    private BigDecimal totalScore; // 总分
    private BigDecimal passScore;  // 及格分数
    private Date createTime;    // 创建时间

    private List<Question> questions;//接收试题使用
}