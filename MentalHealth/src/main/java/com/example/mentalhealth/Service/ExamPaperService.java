package com.example.mentalhealth.Service;


import com.example.mentalhealth.entities.ExamPaper;
import com.example.mentalhealth.entities.Question;
import com.example.mentalhealth.mapper.ExamPaperMapper;
import com.example.mentalhealth.mapper.QuestionMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ExamPaperService {
    @Autowired
    public ExamPaperMapper examPaperMapper;

    @Autowired
    public ExamPaperQuestionService examPaperQuestionService;

    @Autowired
    public QuestionMapper questionMapper;
    // 保存试卷
    public int saveExamPaper(ExamPaper examPaper){
       return examPaperMapper.insert(examPaper);
    }


    // 查询试卷
    public  PageInfo<ExamPaper> queryExamPapersByPage(ExamPaper examPaper, int page, int size){
        // 开启分页
        PageHelper.startPage(page, size);
        // 执行查询
        List<ExamPaper> examPaperList = examPaperMapper.selectByQuery(examPaper);
        List<ExamPaper> examPaperList1 = new ArrayList<>();
        for(ExamPaper examPaper1:examPaperList){
            //1.获取试题id
            Long[] questionIds = examPaperQuestionService.getQuestionIdsByPaperId(examPaper1.getId());
            List<Question> questions = new ArrayList<>();
            //2.根据试题id查询试题
            for (long questionId: questionIds){
                Question question = questionMapper.selectById(questionId);
                questions.add(question);
            }
            //将试题集成到试卷中
            examPaper1.setQuestions(questions);
            examPaperList1.add(examPaper1);
        }
        System.out.println(examPaperList1);
        return new PageInfo<>(examPaperList1);

    }

    public int deleteById(Long paperId) {
        return examPaperMapper.deleteById(paperId);
    }

}
