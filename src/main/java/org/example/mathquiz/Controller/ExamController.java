package org.example.mathquiz.Controller;

import jakarta.validation.constraints.NotNull;
import org.example.mathquiz.Entities.Exam;
import org.example.mathquiz.Service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/exams")
public class ExamController {
    @Autowired
    private ExamService examService;

    @GetMapping("")
    public String showAllExams(@NotNull Model model) {
        model.addAttribute("exams", examService.getAllExams());
        return "exam/index";
    }

    @GetMapping("/add")
    public String addExamForm(@NotNull Model model) {
        model.addAttribute("exam", new Exam());
        return "exam/add";
    }

    @PostMapping("/add")
    public String addExam(@ModelAttribute("exam") Exam exam,
                              BindingResult bindingResult,
                              Model model) {
        examService.addExam(exam);
        return "redirect:/exams";
    }

    @GetMapping("/delete/{id}")
    public String confirmDeleteExam(@PathVariable String id, Model model) {
        var exam = examService.getExamById(id)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found"));
        model.addAttribute("exam", exam);
        return "exam/delete";
    }

    @PostMapping("/delete")
    public String deleteExam(@ModelAttribute("exam") Exam exam) {
        examService.getExamById(exam.getId())
                .ifPresentOrElse(
                        lvl -> examService.deleteExamById(lvl.getId()),
                        () -> { throw new IllegalArgumentException("Exam not found"); });
        return "redirect:/exams";
    }

    @GetMapping("/edit/{id}")
    public String editExamForm(@NotNull Model model, @PathVariable String id) {
        var exam = examService.getExamById(id);
        model.addAttribute("exam", exam.orElseThrow(() -> new IllegalArgumentException("Exam not found")));
        return "exam/edit";
    }

    @PostMapping("/edit")
    public String editExam(@ModelAttribute("exam") Exam exam,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "exam/edit";
        }

        examService.updateExam(exam);
        return "redirect:/exams";
    }
}