package org.example.mathquiz.Controller.API;

import org.example.mathquiz.Entities.QuizMatrix;
import org.example.mathquiz.Service.QuizMatrixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/quizmatrices")
public class ApiQuizMatrixController {
   @Autowired
   private QuizMatrixService quizMatrixService;
}
