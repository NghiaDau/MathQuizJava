package org.example.mathquiz.Controller.API;

import org.example.mathquiz.Entities.*;
import org.example.mathquiz.RequesEntities.RequestChapterJson;
import org.example.mathquiz.RequesEntities.RequestJson;
import org.example.mathquiz.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiChapterController {
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private QuizMatrixService quizMatrixService;
    @Autowired
    private MathTypeService mathTypeService;
    @Autowired
    private QuizService quizService;
    @Autowired
    private QuizOptionService quizOptionService;
    private static String save_gradeId;
    @GetMapping("/chapters")
    public ResponseEntity<?> getAllChapter(){
        return ResponseEntity.ok(chapterService.getAllChapters());
    }

    @PostMapping("/grades")
    public ResponseEntity<?> getAllGrade(){
        List<Grade> grades = gradeService.getAllGrades();
        List<RequestJson> newGrades = new ArrayList<>();
        for (Grade grade : grades) {
            RequestJson newGrade = new RequestJson();
            newGrade.setId(grade.getId());
            newGrade.setName(grade.getName());
            newGrades.add(newGrade);
        }
        return ResponseEntity.ok(newGrades);
    }

    @PostMapping("/findMathType")
    public ResponseEntity<?> getAllMathType(@RequestParam("gradeId") String gradeId){
        if (gradeId.isEmpty()) {
            return ResponseEntity.badRequest().body("Grade ID cannot be empty.");
        }
        List<MathType> mathTypes = mathTypeService.getAllMathTypes();
        save_gradeId = gradeId;
        List<RequestJson> listmathTypes = new ArrayList<>();
        for (MathType mathType : mathTypes) {
            try {
                RequestJson math = new RequestJson();
                math.setId(mathType.getId());
                math.setName(mathType.getName());
                listmathTypes.add(math);
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return ResponseEntity.ok(listmathTypes);
    }

    @PostMapping("/findChapter")
    public ResponseEntity<?> getAllChapter (@RequestParam("mathTypeId") String mathTypeId) {
        if (mathTypeId.isEmpty()) {
            return ResponseEntity.badRequest().body("Grade ID cannot be empty.");
        }
        List<Chapter> chapters = chapterService.getChapterbyGrade(save_gradeId,mathTypeId);
        List<RequestChapterJson> chapterDtos = new ArrayList<>();
        for (Chapter chapter : chapters) {
            try {
                RequestChapterJson chapterDto = new RequestChapterJson();
                chapterDto.setId(chapter.getId());
                chapterDto.setName(chapter.getName());
                chapterDto.setMathType(chapter.getMathType());
                chapterDtos.add(chapterDto);
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return ResponseEntity.ok(chapterDtos);
    }
    @PostMapping("/quizMatrices")
    public ResponseEntity<?> getAllQuizMatric(@RequestParam("chapterId") String chapterId){
        if (chapterId.isEmpty()) {
            return ResponseEntity.badRequest().body("Chapter ID cannot be empty.");
        }
        List<QuizMatrix> quizMatrices = quizMatrixService.getQuizMatricesbyChapter(chapterId);
        List<RequestJson> newGrades = new ArrayList<>();
        for (QuizMatrix grade : quizMatrices) {
            try {
                RequestJson newGrade = new RequestJson();
                newGrade.setNumOfQuiz(grade.getNumOfQuiz());
                newGrade.setDefaultDuration(grade.getDefaultDuration());
                newGrade.setId(grade.getId());
                newGrade.setName(grade.getName());
                newGrade.setStatus((grade.isStatus() == true) ? "Đang Sử Dụng": "Không Sử Dụng" );
                newGrades.add(newGrade);
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        newGrades.sort(Comparator.comparing(RequestJson::getName));

        return ResponseEntity.ok(newGrades);
    }

    @GetMapping("/editQuizMatrix/{id}")
    public ResponseEntity<?> editQuizMatrices (@PathVariable String id){
        QuizMatrix quizMatrix = quizMatrixService.getQuizMatrixById(id);
        List<Quiz> quizList = quizMatrix.getQuizs();
        return  ResponseEntity.ok(quizList);
    }

    @PostMapping("/submitQuizData")
    public ResponseEntity<?> editQuizMatrices(@RequestBody DataRequest dataRequest) {
        try {
            String quizMatrixId = dataRequest.getStringValue();
            List<Quiz> quizList = dataRequest.getQuizArray();
            for (Quiz quiz : quizList) {
                quiz.setQuizMatrix(quizMatrixService.getQuizMatrixById(quizMatrixId));
            }
            List<Quiz> savedQuizzes = quizService.addQuiz(quizList);
            List<QuizOption> quizOptionList = new ArrayList<>();
            for (Quiz quiz : quizList) {
                for (QuizOption quizOption : quiz.getQuizOptions()) {
                    quizOption.setQuiz(quiz);
                    quizOptionList.add(quizOption);
                }
            }
            quizOptionService.addQuizOption(quizOptionList);
            return ResponseEntity.ok(savedQuizzes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
