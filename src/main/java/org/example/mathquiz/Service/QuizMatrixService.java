package org.example.mathquiz.Service;

import lombok.RequiredArgsConstructor;
import org.example.mathquiz.Entities.Quiz;
import org.example.mathquiz.Entities.QuizMatrix;
import org.example.mathquiz.Repositories.IQuizMatrixRepository;
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
    public List<QuizMatrix> getAllQuizMatrices() {
        return quizMatrixRepository.findAll();
    }
    public QuizMatrix addQuizMatrix(QuizMatrix quizMatrix) {
        return quizMatrixRepository.save(quizMatrix);
    }
    public QuizMatrix updateQuizMatrix(QuizMatrix quizMatrix) {
        return quizMatrixRepository.save(quizMatrix);
    }

}
