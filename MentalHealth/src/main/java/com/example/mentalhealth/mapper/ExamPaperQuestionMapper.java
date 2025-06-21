package com.example.mentalhealth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.mentalhealth.entities.ExamPaperQuestion;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExamPaperQuestionMapper extends BaseMapper<ExamPaperQuestion> {
    Long[] getQuestionIdsByPaperId(Long paperId);
}
