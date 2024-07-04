package org.example.mathquiz.Service;

import lombok.RequiredArgsConstructor;
import org.example.mathquiz.Entities.Exam;

import org.example.mathquiz.Repositories.IExamRepository;
import org.example.mathquiz.RequesEntities.RequestPushExam;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE,
        rollbackFor = {Exception.class, Throwable.class})
public class ExamService {
    private final IExamRepository examRepository;
    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }
    public Optional<Exam> getExamById(String id) {
        return examRepository.findById(id);
    }
    public Exam addExam(Exam exam) {
        return examRepository.save(exam);
    }
    public Exam addExamFromQuizMatrix(RequestPushExam requestPushExam) {
        Exam exam = new Exam();
        exam.setQuizMatrix(requestPushExam.getQuizMatrix());
        exam.setIsCustomExam(false);
        exam.setName(requestPushExam.getName());
        exam.setDuration(requestPushExam.getDuration());
        exam.setNumberOfQuiz(requestPushExam.getNumOfQuiz());
        exam.setUser(requestPushExam.getUser());
        return examRepository.save(exam);
    }
    public Exam updateExam(Exam exam) {
        return examRepository.save(exam);
    }
    public void deleteExamById(String id) {
        examRepository.deleteById(id);
    }

}
