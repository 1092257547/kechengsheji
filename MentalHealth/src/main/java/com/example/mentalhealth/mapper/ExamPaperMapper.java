package com.example.mentalhealth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.mentalhealth.entities.ExamPaper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExamPaperMapper extends BaseMapper<ExamPaper> {

    List<ExamPaper> selectByQuery(@Param("examPaper") ExamPaper examPaper);
}
