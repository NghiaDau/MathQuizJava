package org.example.mathquiz.Controller;

import org.example.mathquiz.Entities.Chapter;
import org.example.mathquiz.Entities.Grade;
import org.example.mathquiz.Entities.MathType;
import org.example.mathquiz.Entities.QuizMatrix;
import org.example.mathquiz.RequesEntities.RequestChapterJson;
import org.example.mathquiz.RequesEntities.RequestJson;
import org.example.mathquiz.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
                newGrade.setId(grade.getId());
                newGrade.setName(grade.getName());
                newGrade.setStatus((grade.isStatus() == true) ? "Đang Sử Dụng": "Không Sử Dụng" );
                newGrades.add(newGrade);
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return ResponseEntity.ok(newGrades);
    }
}
