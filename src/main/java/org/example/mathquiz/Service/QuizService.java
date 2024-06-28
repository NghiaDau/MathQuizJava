package org.example.mathquiz.Service;

import lombok.RequiredArgsConstructor;
import org.example.mathquiz.Entities.Quiz;

import org.example.mathquiz.Repositories.IQuizRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE,
        rollbackFor = {Exception.class, Throwable.class})
public class QuizService {
    private final IQuizRepository levelRepository;
    public List<Quiz> getAllQuizs() {
        return levelRepository.findAll();
    }
    public Optional<Quiz> getQuizById(String id) {
        return levelRepository.findById(id);
    }
    public Quiz addQuiz(Quiz level) {
        return levelRepository.save(level);
    }
    public Quiz updateQuiz(Quiz level) {
        return levelRepository.save(level);
    }
    public void deleteQuizById(String id) {
        levelRepository.deleteById(id);
    }
}
