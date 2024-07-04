package org.example.mathquiz.Controller;

import jakarta.validation.constraints.NotNull;
import org.example.mathquiz.Entities.*;
import org.example.mathquiz.RequesEntities.RequestPushExam;
import org.example.mathquiz.RequesEntities.RequestPushExamDetailList;
import org.example.mathquiz.RequesEntities.RequestPushResult;
import org.example.mathquiz.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/exams")
public class ExamController {
    @Autowired
    private ExamService examService;
    @Autowired
    private UserService userService;
    @Autowired
    private ExamDetailService examDetailService;
    @Autowired
    private QuizMatrixService quizMatrixService;
    @Autowired
    private ResultService resultService;

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

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
    @PostMapping("/addExamFromQuizMatrix")
    public Exam addExamFromQuizMatrix(@ModelAttribute("quizMatrix") QuizMatrix quizMatrix,
                              BindingResult bindingResult,
                              Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        User user = userService.findByUserName(username);
        RequestPushExam requestPushExam = new RequestPushExam();
        requestPushExam.setQuizMatrix(quizMatrix);
        requestPushExam.setDuration(quizMatrix.getDefaultDuration());
        requestPushExam.setName(quizMatrix.getName());
        requestPushExam.setNumOfQuiz(quizMatrix.getNumOfQuiz());
        requestPushExam.setUser(user);
        return examService.addExamFromQuizMatrix(requestPushExam);
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
    @GetMapping("/pushNewExam")
    public String pushNewExam(@ModelAttribute("quizMatrixId") String quizMatrixId, @NotNull Model model) {
        QuizMatrix quizMatrix = quizMatrixService.getQuizMatrixById(quizMatrixId);
        User user = userService.findByUserName("user");
        RequestPushExam requestPushExam = new RequestPushExam();
        requestPushExam.setQuizMatrix(quizMatrix);
        requestPushExam.setDuration(quizMatrix.getDefaultDuration());
        requestPushExam.setName(quizMatrix.getName());
        requestPushExam.setNumOfQuiz(quizMatrix.getNumOfQuiz());
        requestPushExam.setUser(user);
        Exam newExam = examService.addExamFromQuizMatrix(requestPushExam);

        List<Quiz> quizList = quizMatrix.getQuizs();
        Collections.shuffle(quizList, new Random());
        RequestPushExamDetailList requestPushExamDetailList = new RequestPushExamDetailList();
        requestPushExamDetailList.setExam(newExam);
        requestPushExamDetailList.setQuizList(quizList);
        List<ExamDetail> examDetailList = examDetailService.addExamDetailList(requestPushExamDetailList);

        RequestPushResult requestPushResult = new RequestPushResult();
        requestPushResult.setTotalQuiz(newExam.getNumberOfQuiz());
        requestPushResult.setStartTime(new Date());
        requestPushResult.setUser(user);
        requestPushResult.setExam(newExam);
        resultService.addResult(requestPushResult);

        model.addAttribute("examDetailList", examDetailList);
        model.addAttribute("currentIndex", 0); // Thêm chỉ số câu hỏi hiện tại
        return "exam/doExam";
    }
}