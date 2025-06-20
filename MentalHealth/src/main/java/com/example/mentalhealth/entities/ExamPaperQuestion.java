package com.example.mentalhealth.entities;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("exam_paper_question")
public class ExamPaperQuestion {
    @TableId(type = IdType.AUTO)
    private Long id;            // ID
    private Long paperId;       // 试卷ID
    private Long questionId;    // 题目ID
    private Integer questionType; // 题目类型(1: 单选题, 2: 多选题, 3: 判断题, 4: 填空题, 5: 简答题)
    private BigDecimal score;   // 题目分值
    private Integer sort;       // 题目排序
}