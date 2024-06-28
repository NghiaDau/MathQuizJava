package org.example.mathquiz.Controller;

import jakarta.validation.constraints.NotNull;
import org.example.mathquiz.Entities.Quiz;
import org.example.mathquiz.Service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/quizs")
public class QuizController {
    @Autowired
    private QuizService quizService;

    @GetMapping("")
    public String showAllQuizs(@NotNull Model model) {
        model.addAttribute("quizs", quizService.getAllQuizs());
        return "quiz/index";
    }

    @GetMapping("/add")
    public String addQuizForm(@NotNull Model model) {
        model.addAttribute("quiz", new Quiz());
        return "quiz/add";
    }

    @PostMapping("/add")
    public String addQuiz(@ModelAttribute("quiz") Quiz quiz,
                           BindingResult bindingResult,
                           Model model) {
        quizService.addQuiz(quiz);
        return "redirect:/quizs";
    }

    @GetMapping("/delete/{id}")
    public String confirmDeleteQuiz(@PathVariable String id, Model model) {
        var quiz = quizService.getQuizById(id)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found"));
        model.addAttribute("quiz", quiz);
        return "quiz/delete";
    }

    @PostMapping("/delete")
    public String deleteQuiz(@ModelAttribute("quiz") Quiz quiz) {
        quizService.getQuizById(quiz.getId())
                .ifPresentOrElse(
                        lvl -> quizService.deleteQuizById(lvl.getId()),
                        () -> { throw new IllegalArgumentException("Quiz not found"); });
        return "redirect:/quizs";
    }

    @GetMapping("/edit/{id}")
    public String editQuizForm(@NotNull Model model, @PathVariable String id) {
        var quiz = quizService.getQuizById(id);
        model.addAttribute("quiz", quiz.orElseThrow(() -> new IllegalArgumentException("Quiz not found")));
        return "quiz/edit";
    }

    @PostMapping("/edit")
    public String editQuiz(@ModelAttribute("quiz") Quiz quiz,
                            BindingResult bindingResult,
                            Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "quiz/edit";
        }

        quizService.updateQuiz(quiz);
        return "redirect:/quizs";
    }
}