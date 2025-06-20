package com.example.mentalhealth.entities;

import lombok.Data;

// 选项类
@Data
public class QuestionOption {
    private String key;         // 选项标识(A, B, C, D...)
    private String value;       // 选项内容
}
