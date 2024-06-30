package org.example.mathquiz.Service;

import lombok.RequiredArgsConstructor;
import org.example.mathquiz.Entities.Quiz;
import org.example.mathquiz.Entities.QuizOption;

import org.example.mathquiz.Repositories.IQuizOptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE,
        rollbackFor = {Exception.class, Throwable.class})
public class QuizOptionService {
    private final IQuizOptionRepository gradeRepository;
    public List<QuizOption> getAllQuizOptions() {
        return gradeRepository.findAll();
    }
    public Optional<QuizOption> getQuizOptionById(String id) {
        return gradeRepository.findById(id);
    }
    public QuizOption addQuizOption(QuizOption grade) {
        return gradeRepository.save(grade);
    }
    public List<QuizOption> addQuizOption(List<QuizOption> quizList) {
        List<QuizOption> savedQuizzes = new ArrayList<>();
        for( QuizOption quiz: quizList){
            savedQuizzes.add(gradeRepository.save(quiz));
        }
        return savedQuizzes;
    }
    public QuizOption updateQuizOption(QuizOption grade) {
        return gradeRepository.save(grade);
    }
    public void deleteQuizOptionById(String id) {
        gradeRepository.deleteById(id);
    }
}
