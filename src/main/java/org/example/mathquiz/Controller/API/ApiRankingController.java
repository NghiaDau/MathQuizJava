package org.example.mathquiz.Controller.API;

import lombok.Builder;
import org.example.mathquiz.Entities.QuizMatrix;
import org.example.mathquiz.Service.QuizMatrixService;
import org.example.mathquiz.Service.QuizService;
import org.example.mathquiz.Service.ResultService;
import org.example.mathquiz.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ranking")
public class ApiRankingController {
    @Autowired
    private ResultService resultService;
    @Autowired
    private QuizMatrixService quizMatrixService;
    @Autowired
    private UserService userService;
    @GetMapping("/quizmatrix/{period}")
    public List<Object[]> getQuizMatrixByPeriod(@PathVariable String period) {
        List<Object[]> r;
        switch (period.toLowerCase()){
            case "day":
                r = quizMatrixService.findQuizMatrixByDay().stream().limit(10).toList();
                return r;
            case "month":
                r = quizMatrixService.findQuizMatrixByMonth().stream().limit(10).toList();
                return r;
            case "year":
                r = quizMatrixService.findQuizMatrixByYear().stream().limit(10).toList();
                return r;
            default:
                throw new IllegalArgumentException("Invalid period: " + period);
        }
    }
    @GetMapping("/users/{period}")
    public List<Object[]> getUsersByPeriod(@PathVariable String period) {
        List<Object[]> r;
        switch (period.toLowerCase()){
            case "day":
                r = userService.findUsersByDay().stream().limit(10).toList();
                return r;
            case "month":
                r = userService.findUsersByMonth().stream().limit(10).toList();
                return r;
            case "year":
                r = userService.findUsersByYear().stream().limit(10).toList();
                return r;
            default:
                throw new IllegalArgumentException("Invalid period: " + period);
        }

    }
}
