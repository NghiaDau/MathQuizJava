package org.example.mathquiz.Controller;

import jakarta.validation.constraints.NotNull;
import org.example.mathquiz.Entities.ExamDetail;
import org.example.mathquiz.Entities.Result;
import org.example.mathquiz.Entities.User;
import org.example.mathquiz.RequesEntities.ResultDetail;
import org.example.mathquiz.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/results")
public class ResultController {
    @Autowired
    private ResultService resultService;
    @Autowired
    private UserService userService;
    @Autowired
    private MathTypeService mathTypeService;

    @GetMapping("")
    public String showAllResults(@NotNull Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        User user = userService.findByUserName(username);
        if (user == null) {
            return "redirect:/login";
        }
        List<Result> results = resultService.findByUserId(user.getId());
        results.sort(Comparator.comparing(Result::getStartTime));
        model.addAttribute("results", results);
        return "result/index";
    }

    @GetMapping("/add")
    public String addResultForm(@NotNull Model model) {
        model.addAttribute("result", new Result());

        model.addAttribute("mathTypes", mathTypeService.getAllMathTypes());

        return "result/add";
    }

//    @PostMapping("/add")
//    public String addResult(@ModelAttribute("result") Result result,
//                             BindingResult bindingResult,
//                             Model model) {
//        Result savedResult = resultService.addResult(result);
//        return "redirect:/results";
//    }

    @GetMapping("/delete/{id}")
    public String confirmDeleteResult(@PathVariable String id, Model model) {
        var result = resultService.getResultById(id)
                .orElseThrow(() -> new IllegalArgumentException("Result not found"));
        model.addAttribute("result", result);

        return "result/delete";
    }

    @PostMapping("/delete")
    public String deleteResult(@ModelAttribute("result") Result result) {
        resultService.getResultById(result.getId())
                .ifPresentOrElse(
                        lvl -> resultService.deleteResultById(lvl.getId()),
                        () -> { throw new IllegalArgumentException("Result not found"); });
        return "redirect:/results";
    }

    @GetMapping("/edit/{id}")
    public String editResultForm(@NotNull Model model, @PathVariable String id) {
        var result = resultService.getResultById(id);
        model.addAttribute("mathTypes", mathTypeService.getAllMathTypes());

        model.addAttribute("result", result.orElseThrow(() -> new IllegalArgumentException("Result not found")));
        return "result/edit";
    }
    @GetMapping("/resultDetail/{id}")
    public String resultDetail(@NotNull Model model, @PathVariable String id) {
        List<ResultDetail> resultDetailList = new ArrayList<>();
        Result result = resultService.getResultById(id).orElseThrow();
        List<ExamDetail> examDetailList = result.getExam().getExamDetails();
        for (ExamDetail examDetail : examDetailList) {
            ResultDetail resultDetail = new ResultDetail();
            resultDetail.setQuiz(examDetail.getQuiz());
            resultDetail.setYourQuizOption(examDetail.getSelectedOption());
            resultDetailList.add(resultDetail);
        }
        model.addAttribute("result", result);
        model.addAttribute("resultDetailList", resultDetailList);
        return "result/resultDetail";
    }

    @PostMapping("/edit")
    public String editResult(@ModelAttribute("result") Result result,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "result/edit";
        }

        resultService.updateResult(result);
        return "redirect:/results";
    }

}