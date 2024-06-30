package org.example.mathquiz.Controller;

import jakarta.validation.constraints.NotNull;
import org.example.mathquiz.Entities.Chapter;
import org.example.mathquiz.Entities.Quiz;
import org.example.mathquiz.Entities.QuizMatrix;
import org.example.mathquiz.Entities.QuizOption;
import org.example.mathquiz.RequesEntities.RequestModel;
import org.example.mathquiz.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/chapters")
public class ChapterController {
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private MathTypeService mathTypeService;
    @Autowired
    private QuizMatrixService quizMatrixService;
    @Autowired
    private QuizService quizService;
    @Autowired
    private QuizOptionService quizOptionService;

    private static List<Quiz> Quizs;

    @GetMapping("")
    public String showAllChapters(@NotNull Model model) {
        model.addAttribute("chapters", chapterService.getAllChapters());
        model.addAttribute("grades",
                gradeService.getAllGrades());
        model.addAttribute("mathTypes", mathTypeService.getAllMathTypes());
        return "chapter/index";
    }

    @GetMapping("/add")
    public String addChapterForm(@NotNull Model model) {
        model.addAttribute("chapterModel", new RequestModel());
        model.addAttribute("grades",
                gradeService.getAllGrades());
        model.addAttribute("mathTypes", mathTypeService.getAllMathTypes());

        return "chapter/add";
    }

    @PostMapping("/add")
    public String addChapter(@ModelAttribute("chapterModel") RequestModel chapterModel,
                           BindingResult bindingResult,
                           Model model) {
        List<Quiz> questionVMs = Quizs;
        List<QuizOption> quizOptionList = new ArrayList<>();
        Chapter savedChapter = chapterService.addChapter(chapterModel);
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
        return "redirect:/chapters";
    }

    @GetMapping("/delete/{id}")
    public String confirmDeleteChapter(@PathVariable String id, Model model) {
        var chapter = chapterService.getChapterById(id)
                .orElseThrow(() -> new IllegalArgumentException("Chapter not found"));
        model.addAttribute("chapter", chapter);

        return "chapter/delete";
    }

    @PostMapping("/delete")
    public String deleteChapter(@ModelAttribute("chapter") Chapter chapter) {
        chapterService.getChapterById(chapter.getId())
                .ifPresentOrElse(
                        lvl -> chapterService.deleteChapterById(lvl.getId()),
                        () -> { throw new IllegalArgumentException("Chapter not found"); });
        return "redirect:/chapters";
    }

    @GetMapping("/edit/{id}")
    public String editChapterForm(@NotNull Model model, @PathVariable String id) {
        var chapter = chapterService.getChapterById(id);
        model.addAttribute("grades",
                gradeService.getAllGrades());
        model.addAttribute("mathTypes", mathTypeService.getAllMathTypes());

        model.addAttribute("chapter", chapter.orElseThrow(() -> new IllegalArgumentException("Chapter not found")));
        return "chapter/edit";
    }

    @PostMapping("/edit")
    public String editChapter(@ModelAttribute("chapter") Chapter chapter,
                            BindingResult bindingResult,
                            Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "chapter/edit";
        }

        chapterService.updateChapter(chapter);
        return "redirect:/chapters";
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
        System.out.println(questionVMs.size());
        Quizs = questionVMs;
        return ResponseEntity.ok().build();
    }
}