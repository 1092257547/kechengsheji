package com.example.mentalhealth.Controller.AdminController;


import com.example.mentalhealth.Service.ExamPaperQuestionService;
import com.example.mentalhealth.Service.ExamPaperService;
import com.example.mentalhealth.entities.ExamPaper;
import com.example.mentalhealth.entities.ExamPaperQuestion;
import com.example.mentalhealth.entities.Question;
import com.example.mentalhealth.entities.ResultData;
import com.example.mentalhealth.mapper.ExamPaperMapper;
import com.example.mentalhealth.mapper.ExamPaperQuestionMapper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ExamPaperController {

    @Autowired
    public ExamPaperService examPaperService;

    @Autowired
    public ExamPaperQuestionService examPaperQuestionService;




    //获取所有试卷
    @GetMapping("/GetExamPapers")
    public ResultData GetExamPapers( @RequestParam(required = false) String title,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        ExamPaper examPaper = new ExamPaper();
        examPaper.setTitle(title);
        //查询所有试卷
        PageInfo<ExamPaper> pageInfo = examPaperService.queryExamPapersByPage(examPaper, page, size);
        return new ResultData(200, "success", pageInfo);
    }


    /**
     * 新增试卷
     * @param examPaper
     * @return
     */
    @RequestMapping("/AddExamPaper")
    public ResultData addExamPaper(@RequestBody ExamPaper examPaper){
        List<Question> questions = examPaper.getQuestions();//试题列表
        //1.新增试卷
        examPaper.setQuestions(null);//删除试题列表
        examPaperService.saveExamPaper(examPaper);//保存试卷
        //2.获取试卷id
        Long id = examPaper.getId();
        //3.保存试卷id与试题
        for (Question question : questions){
            ExamPaperQuestion examPaperQuestion = new ExamPaperQuestion();
            examPaperQuestion.setPaperId(id);//试卷id
            examPaperQuestion.setQuestionId(question.getId());//试题id
            examPaperQuestion.setQuestionType(question.getType());//试题类型
            if(question.getScore() != null){
                examPaperQuestion.setScore(question.getScore());//试题分数
            }else {
                examPaperQuestion.setScore(new BigDecimal(0));
            }
            examPaperQuestionService.saveExamPaperQuestion(examPaperQuestion);
        }
        return new ResultData(200,"添加成功");
    }


    /**
     * 更新试卷
     * @param examPaper
     * @return
     */
    @PutMapping("/UpdateExamPaper")
    public ResultData updateExamPaper(@RequestBody ExamPaper examPaper){
        List<Question> questions = examPaper.getQuestions();//试题列表
        //1.获取试卷id
        Long id = examPaper.getId();
        //2.删除原来的所有试题
        examPaperQuestionService.deleteExamPaperQuestionsByPaperId(id);
        //3.保存新的试题
        for (Question question : questions){
            ExamPaperQuestion examPaperQuestion = new ExamPaperQuestion();
            examPaperQuestion.setPaperId(id);//试卷id
            examPaperQuestion.setQuestionId(question.getId());//试题id
            examPaperQuestion.setQuestionType(question.getType());//试题类型
            if(question.getScore() != null){
                examPaperQuestion.setScore(question.getScore());//试题分数
            }else {
                examPaperQuestion.setScore(new BigDecimal(0));
            }
            examPaperQuestionService.saveExamPaperQuestion(examPaperQuestion);
        }
        return new ResultData(200,"更新成功");
    }

    @DeleteMapping("/deleteExamPaper/{id}")
    public ResultData deleteExamPaper(@PathVariable Long id) {
        System.out.println("deleteExamPaper: " + id);
        //1.删除试卷
        int delete = examPaperService.deleteById(id);
        //2.删除试卷中的所有试题
        examPaperQuestionService.deleteExamPaperQuestionsByPaperId(id);
        if(delete>0){
            return new ResultData(200,"删除成功");
        }else {
            return new ResultData(500,"删除失败");
        }
    }

}
