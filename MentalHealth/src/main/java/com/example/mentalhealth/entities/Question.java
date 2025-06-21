package com.example.mentalhealth.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

// 题目类
@Data
public class Question {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;            // 题目ID
    private Integer type;       // 题目类型(1: 单选题, 2: 多选题, 3: 判断题, 4: 填空题, 5: 简答题)
    private String content;     // 题目内容

    @TableField(typeHandler = JacksonTypeHandler.class,jdbcType = JdbcType.VARCHAR)
    private List<QuestionOption> options; // 选项列表(选择题)
    private String answer;      // 答案
    private String explanation; // 答案解析
    private Date createTime;    // 创建时间
    private BigDecimal score;   // 题目分数
}

