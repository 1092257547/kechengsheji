package com.example.mentalhealth.Controller.AdminController;
import com.example.mentalhealth.Service.QuestionService;
import com.example.mentalhealth.entities.Question;
import com.example.mentalhealth.entities.ResultData;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    /**
     * 新增试题
     * @return
     */
    @RequestMapping("/AddQuestion")
    public ResultData addQuestion(@RequestBody Question question){
        System.out.println("AddQuestion: " + question);
            int result =questionService.saveQuestion(question);
            if (result>0){
                return new ResultData(200,"添加成功",result);
            }
            return new ResultData(200,"添加失败",result);
    }


    /**
     * 更新试题
     * @param question
     * @return
     */
    @PutMapping("/updateQuestion")
    public ResultData updateQuestion(@RequestBody Question question){
        System.out.println("updateQuestion: " + question);
        int result =questionService.updateQuestion(question);
        if (result>0){
            return new ResultData(200,"更新成功",result);
        }
        return new ResultData(200,"更新失败",result);
    }


    /**
     * 查询所有试题
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/GetQuestions")
    public ResultData getQuestions(
            @RequestParam(required = false) String content,
            @RequestParam(required = false)  String type,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Question question = new Question();
        question.setContent(content);
        if (type != null && !type.isEmpty()) {
            question.setType(Integer.parseInt(type));
        }
        PageInfo<Question> pageInfo = questionService.queryQuestionsByPage(question, page, size);
        return new ResultData(200, "success", pageInfo);
    }

    @DeleteMapping("/deleteQuestion/{id}")
    public ResultData deleteQuestion(@PathVariable String id){
        int result =questionService.deleteQuestion(Integer.parseInt(id));
        return result > 0 ? new ResultData(200, "success") : new ResultData(500, "failed");
    }



}
