package org.example.mathquiz.Controller;

import jakarta.validation.constraints.NotNull;
import org.example.mathquiz.Entities.QuizMatrix;
import org.example.mathquiz.Service.QuizMatrixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/quizMatrices")
public class QuizMatrixController {
    @Autowired
    private QuizMatrixService quizMatrixService;
    @GetMapping("")
    public List<QuizMatrix> getAll(@NotNull Model model,
                                   @RequestParam(defaultValue = "0")
                                   Integer pageNo,
                                   @RequestParam(defaultValue = "20")
                                       Integer pageSize,
                                   @RequestParam(defaultValue = "id")
                                       String sortBy) {
        return quizMatrixService.getAllQuizMatrices(pageNo, pageSize, sortBy);
    }
}
