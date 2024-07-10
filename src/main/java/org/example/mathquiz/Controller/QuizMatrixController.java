package org.example.mathquiz.Controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.example.mathquiz.Entities.*;
import org.example.mathquiz.RequesEntities.RequestModel;
import org.example.mathquiz.RequesEntities.RequestQuizMatrix;
import org.example.mathquiz.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/quizMatrices")

public class QuizMatrixController {
    @Autowired
    private QuizMatrixService quizMatrixService;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private QuizService quizService;
    @Autowired
    private QuizOptionService quizOptionService;
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private ExamService examService;
    @Autowired
    private UserService userService;
    private static String ChapterId;
    private static List<Quiz> Quizs;
    private static String idQuiz;
    @Autowired
    private ExamDetailService examDetailService;

    @GetMapping()
    public String showAllGrade(@NotNull Model model) {
        model.addAttribute("grades", gradeService.getAllGrades());
        return "quizMatrices/index";
    }

    @GetMapping("/add")
    public String addChapterForm(@NotNull Model model, @RequestParam("chapterId") String chapterId) {
        ChapterId = chapterId;
        model.addAttribute("chapterModel", new RequestModel());
        return "quizMatrices/add";
    }

    @PostMapping("/add")
    public String addChapter(@Valid @ModelAttribute("chapterModel") RequestModel chapterModel,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            for (var error : errors) {
                System.out.println(error);
            }
            return "quizMatrices/add";
        }
        List<Quiz> questionVMs = Quizs;
        List<QuizOption> quizOptionList = new ArrayList<>();
        System.out.println(chapterModel.getChapter_id());
        Chapter savedChapter = chapterService.getChapterByIdNon(ChapterId);
        System.out.println(savedChapter);
        chapterModel.setChapter(savedChapter);
        QuizMatrix quizMatrix = quizMatrixService.addQuizMatrix(chapterModel, questionVMs);
        for (Quiz quiz : questionVMs) {
            quiz.setQuizMatrix(quizMatrix);
        }

        List<Quiz> quizDemon = quizService.addQuiz(questionVMs);
        for (Quiz quiz : quizDemon) {
            for (QuizOption quizOption : quiz.getQuizOptions()) {
                quizOption.setQuiz(quiz);
                quizOptionList.add(quizOption);
            }
        }
        quizOptionService.addQuizOption(quizOptionList);
        return "redirect:/quizMatrices";
    }

    @GetMapping("/edit/{id}")
    public String editQuizMatrixForm(@NotNull Model model, @PathVariable String id) {
        try {
            QuizMatrix quizMatrixModel = quizMatrixService.getQuizMatrixById(id);
            idQuiz = id;
            model.addAttribute("quizMatrix", quizMatrixModel);
        } catch (Exception e) {
            throw new RuntimeException("QuizMatrix Not Found");
        }
        return "quizMatrices/edit";
    }

    @PostMapping("/edit")
    public String editChapter(@ModelAttribute("quizMatrix") RequestQuizMatrix requestQuizMatrix,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "quizMatrices/edit";
        }
        requestQuizMatrix.setId(idQuiz);
        quizMatrixService.updateQuizMatrix(requestQuizMatrix);
        return "redirect:/quizMatrices";
    }

    @GetMapping("/delete/{id}")
    public String confirmDeleteQuizMatrix(@PathVariable String id, Model model) {
        var quizMatrix = quizMatrixService.getQuizMatrixByIdNon(id)
                .orElseThrow(() -> new IllegalArgumentException("QuizMatrix not found"));
        model.addAttribute("quizMatrix", quizMatrix);
        return "quizMatrices/delete";
    }

    @PostMapping("/delete")
    public String deleteDeleteQuizMatrix(@ModelAttribute("quizMatrix") QuizMatrix quizMatrix) {
        quizMatrixService.getQuizMatrixByIdNon(quizMatrix.getId())
                .ifPresentOrElse(
                        quizM -> quizMatrixService.deleteQuizMatrix(quizM),
                        () -> {
                            throw new IllegalArgumentException("QuizMatrix not found");
                        });
        return "redirect:/quizMatrices";
    }

    @PostMapping("/uploadEndpoint")
    public ResponseEntity<?> handleFileUpload(@RequestParam("files") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file uploaded.");
        }
        List<Quiz> questionVMs;
        try {
            questionVMs = quizService.readFileLatex(file);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        Quizs = questionVMs;
        return ResponseEntity.ok().build();
    }

    @GetMapping("/quizMatrixPreview")
    public String showQuizMatrixPreview(@ModelAttribute("quizMatrixId") String quizMatrixId, @NotNull Model model) {
        QuizMatrix quizMatrix = quizMatrixService.getQuizMatrixById(quizMatrixId);
        model.addAttribute("quizMatrix", quizMatrix);
        return "quizMatrices/quizMatrixPreview";
    }

    @GetMapping("/editQuizMatrix/{id}")
    public String editQuizMatrices(@PathVariable String id, Model model) {
//        QuizMatrix quizMatrix = quizMatrixService.getQuizMatrixById(id);
//        List<Quiz> quizList = quizMatrix.getQuizs();
//        model.addAttribute("quizMatrix", quizList);
        return "quizMatrices/editQuizMatrix";
    }

//    @PostMapping("/editQuizMatrix")
//    public String editQuizMatrices(@ModelAttribute("quizMatrix") List<Quiz> listQuiz, BindingResult bindingResult,Model model) {
//        if (bindingResult.hasErrors()) {
//            var errors = bindingResult.getAllErrors()
//                    .stream()
//                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
//                    .toArray(String[]::new);
//            model.addAttribute("errors", errors);
//            return "quizMatrices/editQuizMatrix";
//        }
//        List<QuizOption> quizOptionList = new ArrayList<>();
//        List<Quiz> quizDemon = quizService.addQuiz(listQuiz);
//        for (Quiz quiz : quizDemon) {
//            for (QuizOption quizOption : quiz.getQuizOptions()) {
//                quizOption.setQuiz(quiz);
//                quizOptionList.add(quizOption);
//            }
//        }
//        quizOptionService.addQuizOption(quizOptionList);
//        return "redirect:/quizMatrices";
//    }
}
