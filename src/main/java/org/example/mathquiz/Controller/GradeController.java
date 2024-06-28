package org.example.mathquiz.Controller;

import jakarta.validation.constraints.NotNull;
import org.example.mathquiz.Entities.Grade;
import org.example.mathquiz.Service.GradeService;
import org.example.mathquiz.Service.LevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/grades")
public class GradeController {
    @Autowired
    private GradeService gradeService;
    @Autowired
    private LevelService levelService;
    @GetMapping("")
    public String showAllGrades(@NotNull Model model) {
        model.addAttribute("grades", gradeService.getAllGrades());
        model.addAttribute("levels",
                levelService.getAllLevels());
        return "grade/index";
    }

    @GetMapping("/add")
    public String addGradeForm(@NotNull Model model) {
        model.addAttribute("grade", new Grade());
        model.addAttribute("levels",
                levelService.getAllLevels());
        return "grade/add";
    }

    @PostMapping("/add")
    public String addGrade(@ModelAttribute("grade") Grade grade,
                           BindingResult bindingResult,
                           Model model) {
        gradeService.addGrade(grade);
        return "redirect:/grades";
    }

    @GetMapping("/delete/{id}")
    public String confirmDeleteGrade(@PathVariable String id, Model model) {
        var grade = gradeService.getGradeById(id)
                .orElseThrow(() -> new IllegalArgumentException("Grade not found"));
        model.addAttribute("grade", grade);
        return "grade/delete";
    }

    @PostMapping("/delete")
    public String deleteGrade(@ModelAttribute("grade") Grade grade) {
        gradeService.getGradeById(grade.getId())
                .ifPresentOrElse(
                        lvl -> gradeService.deleteGradeById(lvl.getId()),
                        () -> { throw new IllegalArgumentException("Grade not found"); });
        return "redirect:/grades";
    }

    @GetMapping("/edit/{id}")
    public String editGradeForm(@NotNull Model model, @PathVariable String id) {
        var grade = gradeService.getGradeById(id);
        model.addAttribute("levels",
                levelService.getAllLevels());
        model.addAttribute("grade", grade.orElseThrow(() -> new IllegalArgumentException("Grade not found")));
        return "grade/edit";
    }

    @PostMapping("/edit")
    public String editGrade(@ModelAttribute("grade") Grade grade,
                            BindingResult bindingResult,
                            Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "grade/edit";
        }

        gradeService.updateGrade(grade);
        return "redirect:/grades";
    }
}