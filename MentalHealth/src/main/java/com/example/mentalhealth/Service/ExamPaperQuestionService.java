package com.example.mentalhealth.Service;


import com.example.mentalhealth.entities.ExamPaperQuestion;
import com.example.mentalhealth.mapper.ExamPaperMapper;
import com.example.mentalhealth.mapper.ExamPaperQuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ExamPaperQuestionService {
    @Autowired
    public ExamPaperQuestionMapper examPaperQuestionMapper;

    //新增试卷信息
    public void saveExamPaperQuestion(ExamPaperQuestion examPaperQuestion)
    {
         examPaperQuestionMapper.insert(examPaperQuestion);
    }

    //根据试卷id获取所有试题id
    public Long[] getQuestionIdsByPaperId(Long paperId){
        return examPaperQuestionMapper.getQuestionIdsByPaperId(paperId);
    }

    //删除所有试题
    public  int deleteExamPaperQuestionsByPaperId(Long paperId){
        return examPaperQuestionMapper.deleteById(paperId);
    }



}
