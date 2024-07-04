package org.example.mathquiz.Service;

import lombok.RequiredArgsConstructor;
import org.example.mathquiz.Entities.Chapter;
import org.example.mathquiz.Entities.Quiz;
import org.example.mathquiz.Entities.QuizMatrix;
import org.example.mathquiz.Repositories.IChapterRepository;
import org.example.mathquiz.Repositories.IQuizMatrixRepository;
import org.example.mathquiz.RequesEntities.RequestModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE,
        rollbackFor = {Exception.class, Throwable.class})
public class QuizMatrixService {
    private final IQuizMatrixRepository quizMatrixRepository;
    private final IChapterRepository chapterRepository;
    public List<QuizMatrix> getAllQuizMatrices() {
        return quizMatrixRepository.findAll();
    }
    public QuizMatrix addQuizMatrix(RequestModel requestModel, List<Quiz> questions) {
        try {
            QuizMatrix quizMatrix = QuizMatrix.fromRequestModel(requestModel,questions);
            return quizMatrixRepository.save(quizMatrix);
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    public List<QuizMatrix> getQuizMatricesbyChapter(String id){
        return quizMatrixRepository.findQuizMatricesByChapter(id);
    }
    public QuizMatrix updateQuizMatrix(QuizMatrix quizMatrix) {
        return quizMatrixRepository.save(quizMatrix);
    }
    public List<QuizMatrix> getQuizMatricesByChapterId(String chapterId) {
        return quizMatrixRepository.findByChapterId(chapterId);
    }
    public QuizMatrix getQuizMatrixById(String id) {
        return quizMatrixRepository.findById(id).orElse(null);
    }
}
