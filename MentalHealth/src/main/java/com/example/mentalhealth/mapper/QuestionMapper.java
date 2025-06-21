package com.example.mentalhealth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.mentalhealth.entities.Question;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuestionMapper extends BaseMapper<Question> {

     // 根据查询条件查询
     List<Question> selectByQuery(@Param("question") Question question);

     // 根据id查询
     Question selectById(@Param("id") Long id);
}
