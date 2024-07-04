package org.example.mathquiz.Controller;

import jakarta.validation.constraints.NotNull;
import org.example.mathquiz.Entities.*;
import org.example.mathquiz.RequesEntities.RequestModel;
import org.example.mathquiz.RequesEntities.RequestPushExam;
import org.example.mathquiz.RequesEntities.RequestPushExamDetailList;
import org.example.mathquiz.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
    @Autowired
    private ExamDetailService examDetailService;

    @GetMapping()
    public String showAllGrade (@NotNull Model model) {
        model.addAttribute("grades", gradeService.getAllGrades());
        return "quizMatrices/index";
    }
    @GetMapping("/add")
    public String addChapterForm(@NotNull Model model,@RequestParam("chapterId") String chapterId) {
        ChapterId = chapterId;
        model.addAttribute("chapterModel", new RequestModel());
        return "quizMatrices/add";
    }

    @PostMapping("/add")
    public String addChapter(@ModelAttribute("chapterModel") RequestModel chapterModel,
                             BindingResult bindingResult,
                             Model model) {
        List<Quiz> questionVMs = Quizs;
        List<QuizOption> quizOptionList = new ArrayList<>();
        System.out.println(chapterModel.getChapter_id());
        Chapter savedChapter = chapterService.getChapterByIdNon(ChapterId);
        System.out.println(savedChapter);
        chapterModel.setChapter(savedChapter);
        QuizMatrix quizMatrix =  quizMatrixService.addQuizMatrix(chapterModel,questionVMs);
        for (Quiz quiz: questionVMs){
            quiz.setQuizMatrix(quizMatrix);
        }

        List<Quiz> quizDemon =  quizService.addQuiz(questionVMs);
        for (Quiz quiz: quizDemon){
            for ( QuizOption quizOption : quiz.getQuizOptions()){
                quizOption.setQuiz(quiz);
                quizOptionList.add(quizOption);
            }
        }
        quizOptionService.addQuizOption(quizOptionList);
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


}
