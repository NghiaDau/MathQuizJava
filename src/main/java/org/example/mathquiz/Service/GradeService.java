package org.example.mathquiz.Service;

import lombok.RequiredArgsConstructor;
import org.example.mathquiz.Entities.Grade;

import org.example.mathquiz.Repositories.IGradeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE,
        rollbackFor = {Exception.class, Throwable.class})
public class GradeService {
    private final IGradeRepository gradeRepository;
    public List<Grade> getAllGrades() {
        return gradeRepository.findAll().stream()
                .sorted((grade1, grade2) -> grade1.getName().compareToIgnoreCase(grade2.getName()))
                .collect(Collectors.toList());
    }
    public Optional<Grade> getGradeById(String id) {
        return gradeRepository.findById(id);
    }
    public Grade addGrade(Grade grade) {
        return gradeRepository.save(grade);
    }
    public Grade updateGrade(Grade grade) {
        return gradeRepository.save(grade);
    }
    public void deleteGradeById(String id) {
        gradeRepository.deleteById(id);
    }
    public List<Grade> findByLevelId(String levelId) {
        return gradeRepository.findByLevelId(levelId).stream()
                .sorted((grade1, grade2) -> grade1.getName().compareToIgnoreCase(grade2.getName()))
                .collect(Collectors.toList());
    }
}
