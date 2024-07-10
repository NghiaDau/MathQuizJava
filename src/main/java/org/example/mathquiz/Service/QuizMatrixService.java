package org.example.mathquiz.Service;

import lombok.RequiredArgsConstructor;
import org.example.mathquiz.Entities.Chapter;
import org.example.mathquiz.Entities.Quiz;
import org.example.mathquiz.Entities.QuizMatrix;
import org.example.mathquiz.Entities.Result;
import org.example.mathquiz.Repositories.IChapterRepository;
import org.example.mathquiz.Repositories.IQuizMatrixRepository;
import org.example.mathquiz.RequesEntities.RequestModel;
import org.example.mathquiz.RequesEntities.RequestQuizMatrix;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public QuizMatrix updateQuizMatrix(RequestQuizMatrix requestQuizMatrix) {
        try {
            QuizMatrix quizMatrixModel = quizMatrixRepository.findById(requestQuizMatrix.getId()).get();
            quizMatrixModel.setName(requestQuizMatrix.getName());
            quizMatrixModel.setDefaultDuration(requestQuizMatrix.getDefaultDuration());
            quizMatrixModel.setStatus(requestQuizMatrix.isStatus());
            return quizMatrixRepository.save(quizMatrixModel);
        } catch (Exception e) {
            throw  new RuntimeException(e.getMessage());
        }
    }

    public void deleteQuizMatrix(QuizMatrix quizMatrix) {
        quizMatrix.setStatus(false);
        quizMatrixRepository.save(quizMatrix);
    }
    public List<QuizMatrix> getQuizMatricesByChapterId(String chapterId) {
        return quizMatrixRepository.findByChapterId(chapterId);
    }
    public QuizMatrix getQuizMatrixById(String id) {
        return quizMatrixRepository.findById(id).orElse(null);
    }
    public Optional<QuizMatrix> getQuizMatrixByIdNon(String id) {
        return quizMatrixRepository.findById(id);
    }
    public List<Object[]> findQuizMatrixByDay(){return quizMatrixRepository.findQuizMatrixByCurrentDay();}
    public List<Object[]> findQuizMatrixByMonth(){return quizMatrixRepository.findQuizMatrixByCurrentMonth();}
    public List<Object[]> findQuizMatrixByYear(){return quizMatrixRepository.findQuizMatrixByCurrentYear();}
    public List<Object[]> findNewestQuizMatrix(){return quizMatrixRepository.findNewestQuizMatrix();}
    public List<Object[]> findQuizMatrixResultByQuizMatrixId(String quizMatrixId){
        return quizMatrixRepository.findQuizMatrixResultByQuizMatrixId(quizMatrixId);
    }
    public List<Object[]> findQuizMatrixResultByQuizMatrixIdAndUserName( String quizMatrixId, String userName){
        return quizMatrixRepository.findQuizMatrixResultByQuizMatrixIdAndUserName(quizMatrixId,userName);
    }

}
