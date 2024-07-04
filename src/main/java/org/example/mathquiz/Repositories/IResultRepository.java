package org.example.mathquiz.Repositories;

import org.example.mathquiz.Entities.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IResultRepository extends JpaRepository<Result, String> {
    Result findResultByExamId(String examId);
    List<Result> findResultByUserId(String userId);
    Result findResultById(String id);
}
