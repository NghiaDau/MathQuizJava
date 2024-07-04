package org.example.mathquiz.Controller;

import jakarta.validation.constraints.NotNull;
import org.example.mathquiz.Entities.ExamDetail;
import org.example.mathquiz.Service.ExamDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/examDetails")
public class ExamDetailController {
    @Autowired
    private ExamDetailService examDetailService;

    @GetMapping("")
    public String showAllExamDetails(@NotNull Model model) {
        model.addAttribute("examDetails", examDetailService.getAllExamDetails());
        return "examDetail/index";
    }

    @GetMapping("/add")
    public String addExamDetailForm(@NotNull Model model) {
        model.addAttribute("examDetail", new ExamDetail());
        return "examDetail/add";
    }

    @PostMapping("/add")
    public String addExamDetail(@ModelAttribute("examDetail") ExamDetail examDetail,
                          BindingResult bindingResult,
                          Model model) {
        examDetailService.addExamDetail(examDetail);
        return "redirect:/examDetails";
    }

    @GetMapping("/delete/{id}")
    public String confirmDeleteExamDetail(@PathVariable String id, Model model) {
        var examDetail = examDetailService.getExamDetailById(id)
                .orElseThrow(() -> new IllegalArgumentException("ExamDetail not found"));
        model.addAttribute("examDetail", examDetail);
        return "examDetail/delete";
    }

    @PostMapping("/delete")
    public String deleteExamDetail(@ModelAttribute("examDetail") ExamDetail examDetail) {
        examDetailService.getExamDetailById(examDetail.getId())
                .ifPresentOrElse(
                        lvl -> examDetailService.deleteExamDetailById(lvl.getId()),
                        () -> { throw new IllegalArgumentException("ExamDetail not found"); });
        return "redirect:/examDetails";
    }

    @GetMapping("/edit/{id}")
    public String editExamDetailForm(@NotNull Model model, @PathVariable String id) {
        var examDetail = examDetailService.getExamDetailById(id);
        model.addAttribute("examDetail", examDetail.orElseThrow(() -> new IllegalArgumentException("ExamDetail not found")));
        return "examDetail/edit";
    }

    @PostMapping("/edit")
    public String editExamDetail(@ModelAttribute("examDetail") ExamDetail examDetail,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "examDetail/edit";
        }

        examDetailService.updateExamDetail(examDetail);
        return "redirect:/examDetails";
    }    @PostMapping("/editFromExam")
    public void editExamDetailFromExam(@ModelAttribute("examDetail") ExamDetail examDetail,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
        }
        examDetailService.updateExamDetail(examDetail);
    }
}