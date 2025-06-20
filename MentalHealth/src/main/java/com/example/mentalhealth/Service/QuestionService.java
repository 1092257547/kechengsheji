package com.example.mentalhealth.Service;


import com.example.mentalhealth.entities.Question;
import com.example.mentalhealth.mapper.QuestionMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class QuestionService {
    @Autowired
    public QuestionMapper questionMapper;


    //新增试题
    public int saveQuestion(Question question){
        return questionMapper.insert(question);
    }

    // 更新试题
    public int updateQuestion(Question question){
        return questionMapper.updateById(question);
    }

    // 查询试题
    public PageInfo<Question> queryQuestionsByPage(Question question, int page, int size){
        // 开启分页
        PageHelper.startPage(page, size);
        // 执行查询
        List<Question> questionList = questionMapper.selectByQuery(question);
        return new PageInfo<>(questionList);
    }

    public int deleteQuestion(int parseInt) {
        return questionMapper.deleteById(parseInt);
    }
}
