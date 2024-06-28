package org.example.mathquiz.Controller;

import jakarta.validation.constraints.NotNull;
import org.example.mathquiz.Entities.QuizOption;
import org.example.mathquiz.Service.QuizOptionService;
import org.example.mathquiz.Service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/quizOptions")
public class QuizOptionController {
    @Autowired
    private QuizOptionService quizOptionService;
    @Autowired
    private QuizService quizService;
    @GetMapping("")
    public String showAllQuizOptions(@NotNull Model model) {
        model.addAttribute("quizOptions", quizOptionService.getAllQuizOptions());
        model.addAttribute("quizs",
                quizService.getAllQuizs());
        return "quizOption/index";
    }

    @GetMapping("/add")
    public String addQuizOptionForm(@NotNull Model model) {
        model.addAttribute("quizOption", new QuizOption());
        model.addAttribute("quizs",
                quizService.getAllQuizs());
        return "quizOption/add";
    }

    @PostMapping("/add")
    public String addQuizOption(@ModelAttribute("quizOption") QuizOption quizOption,
                           BindingResult bindingResult,
                           Model model) {
        quizOptionService.addQuizOption(quizOption);
        return "redirect:/quizOptions";
    }

    @GetMapping("/delete/{id}")
    public String confirmDeleteQuizOption(@PathVariable String id, Model model) {
        var quizOption = quizOptionService.getQuizOptionById(id)
                .orElseThrow(() -> new IllegalArgumentException("QuizOption not found"));
        model.addAttribute("quizOption", quizOption);
        return "quizOption/delete";
    }

    @PostMapping("/delete")
    public String deleteQuizOption(@ModelAttribute("quizOption") QuizOption quizOption) {
        quizOptionService.getQuizOptionById(quizOption.getId())
                .ifPresentOrElse(
                        lvl -> quizOptionService.deleteQuizOptionById(lvl.getId()),
                        () -> { throw new IllegalArgumentException("QuizOption not found"); });
        return "redirect:/quizOptions";
    }

    @GetMapping("/edit/{id}")
    public String editQuizOptionForm(@NotNull Model model, @PathVariable String id) {
        var quizOption = quizOptionService.getQuizOptionById(id);
        model.addAttribute("quizs",
                quizService.getAllQuizs());
        model.addAttribute("quizOption", quizOption.orElseThrow(() -> new IllegalArgumentException("QuizOption not found")));
        return "quizOption/edit";
    }

    @PostMapping("/edit")
    public String editQuizOption(@ModelAttribute("quizOption") QuizOption quizOption,
                            BindingResult bindingResult,
                            Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "quizOption/edit";
        }

        quizOptionService.updateQuizOption(quizOption);
        return "redirect:/quizOptions";
    }
}