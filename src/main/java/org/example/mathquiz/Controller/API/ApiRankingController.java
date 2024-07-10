package org.example.mathquiz.Controller.API;

import lombok.Builder;
import org.example.mathquiz.Entities.QuizMatrix;
import org.example.mathquiz.Service.QuizMatrixService;
import org.example.mathquiz.Service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ranking")
public class ApiRankingController {
    @Autowired
    private ResultService resultService;
    @Autowired
    private QuizMatrixService quizMatrixService;
    @GetMapping("/{period}")
    public List<Object[]> getQuizMatrixByPeriod(@RequestParam String period) {
        List<Object[]> r;
        switch (period.toLowerCase()){
            case "day":
                r = resultService.findByCurrentDay().stream().limit(5).toList();
                return r;
            case "month":
                r = resultService.findByCurrentMonth().stream().limit(5).toList();
                return r;
            case "year":
                r = resultService.findByCurrentYear().stream().limit(5).toList();
                return r;
            default:
                throw new IllegalArgumentException("Invalid period: " + period);
        }
    }
}
