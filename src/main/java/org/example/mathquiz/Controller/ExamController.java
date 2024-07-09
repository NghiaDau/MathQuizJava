package org.example.mathquiz.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
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

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    @Autowired
    private QuizOptionService quizOptionService;
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
    public String pushNewExam(@ModelAttribute("quizMatrixId") String quizMatrixId, @NotNull Model model, HttpServletRequest request) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        QuizMatrix quizMatrix = quizMatrixService.getQuizMatrixById(quizMatrixId);
        User user = userService.findByUserName(username);
        if (user != null) {
            RequestPushExam requestPushExam = new RequestPushExam();
            requestPushExam.setQuizMatrix(quizMatrix);
            requestPushExam.setDuration(quizMatrix.getDefaultDuration());
            requestPushExam.setName(quizMatrix.getName());
            requestPushExam.setNumOfQuiz(quizMatrix.getNumOfQuiz());
            requestPushExam.setUser(user);
            Exam newExam = examService.addExamFromQuizMatrix(requestPushExam);

            List<Quiz> quizList = quizMatrix.getQuizs();

            Collections.shuffle(quizList, new Random());
            for (Quiz quiz : quizList) {
                Collections.shuffle(quiz.getQuizOptions(), new Random());
            }
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
            model.addAttribute("newExam", newExam);
            model.addAttribute("currentIndex", 0); // Thêm chỉ số câu hỏi hiện tại
            request.getSession().setAttribute("fullName", user.getFullName());
            return "exam/doExam";
        }
        return "user/login";
    }

    @PostMapping("/submitExam")
    public ResponseEntity<Result> submitExam(@RequestBody List<ExamDetail> examDetailList) {
        Date endTime = new Date();
        int numOfCorrectAnswer = 0;
        double score = 0;

        if (examDetailList != null && !examDetailList.isEmpty()) {
            for (ExamDetail detail : examDetailList) {
                // Fetch the current ExamDetail from the database
                ExamDetail currentExamDetail = examDetailService.getExamDetailById(detail.getId())
                        .orElseThrow(() -> new RuntimeException("ExamDetail not found"));

                // Update the selected option
                currentExamDetail.setSelectedOption(detail.getSelectedOption());

                // Save the updated ExamDetail back to the database
                ExamDetail newDetail = examDetailService.updateExamDetail(currentExamDetail);

                // Check if the selected option is correct
                if (newDetail.getSelectedOption() != null && Boolean.TRUE.equals(newDetail.getSelectedOption().getIsCorrect())) {
                    numOfCorrectAnswer++;
                }
            }

            // Calculate the score
            score = (double) numOfCorrectAnswer / examDetailList.size() * 10;
        }

        // Fetch the Result associated with the Exam
        ExamDetail firstExamDetail = examDetailService.getExamDetailById(examDetailList.get(0).getId())
                .orElseThrow(() -> new RuntimeException("ExamDetail not found"));
        Result currentResult = resultService.findByExamId(firstExamDetail.getExam().getId());

        // Update the Result with the end time, score, and number of correct answers
        currentResult.setEndTime(endTime);
        currentResult.setScore(score);
        currentResult.setCorrectAnswers(numOfCorrectAnswer);

        // Save the updated Result back to the database
        Result newResult = resultService.updateResult(currentResult);

        // Return the Result as JSON
        return ResponseEntity.ok(newResult);
    }

    @GetMapping("/examResult")
    public String showResult(@RequestParam("score") double score,
                             @RequestParam("correctAnswers") int correctAnswers,
                             @RequestParam("startTime") String startTime,
                             @RequestParam("endTime") String endTime, Model model) {
        // Adjust the pattern to match the date-time string format being received
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        LocalDateTime start = LocalDateTime.parse(startTime, formatter);
        LocalDateTime end = LocalDateTime.parse(endTime, formatter);
        Duration duration = Duration.between(start, end);
        long seconds = duration.getSeconds();
        model.addAttribute("score", score);
        model.addAttribute("correctAnswers", correctAnswers);
        model.addAttribute("endTime", endTime);
        model.addAttribute("duration", seconds);
        return "result/examResult";
    }

}