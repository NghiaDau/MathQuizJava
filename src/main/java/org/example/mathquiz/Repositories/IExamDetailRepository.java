package org.example.mathquiz.Repositories;

import org.example.mathquiz.Entities.ExamDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IExamDetailRepository extends JpaRepository<ExamDetail, String> {
    public List<ExamDetail> findByExamId(String examId);
}
