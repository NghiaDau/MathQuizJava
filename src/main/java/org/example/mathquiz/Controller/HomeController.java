package org.example.mathquiz.Controller;

import jakarta.persistence.Column;
import org.example.mathquiz.Entities.*;
import org.example.mathquiz.RequesEntities.RequestQuizMatrix;
import org.example.mathquiz.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    private LevelService levelService;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private MathTypeService mathTypeService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private QuizMatrixService quizMatrixService;
    @GetMapping("")
    public String homePage(Model model){
        List<Level> levels = levelService.getAllLevels();
        System.out.println(levels.size());
        model.addAttribute("levels", levels);
        return "home/index";
    }
    @GetMapping("/getGradesByLevel")
    @ResponseBody
    public List<Grade> getGradesByLevel(@RequestParam String levelId) {
        return gradeService.findByLevelId(levelId);
    }

    @GetMapping("/getQuizMatricesByChapter")
    @ResponseBody
    public List<RequestQuizMatrix> getQuizMatricesByChapter(@RequestParam String chapterId) {
        List<QuizMatrix> quizMatrices = quizMatrixService.getQuizMatricesByChapterId(chapterId);
        return quizMatrices.stream()
                .map(quizMatrix -> new RequestQuizMatrix(quizMatrix.getId(), quizMatrix.getName()))
                .collect(Collectors.toList());
    }

    @GetMapping("/getMathTypesByGrade")
    @ResponseBody
    public Set<MathType> getMathTypesByGrade(@RequestParam String gradeId) {
        List<Chapter> chapters = chapterService.findByGradeId(gradeId);
        return chapters.stream()
                .map(Chapter::getMathType)
                .collect(Collectors.toSet());
    }

    @GetMapping("/getChaptersByGradeAndMathType")
    @ResponseBody
    public List<Chapter> getChaptersByGradeAndMathType(@RequestParam String gradeId, @RequestParam(required = false) String mathTypeId) {
        return chapterService.findByGradeAndMathType(gradeId, mathTypeId);
    }
}
