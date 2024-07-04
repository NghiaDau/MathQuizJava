package org.example.mathquiz.Repositories;

import org.example.mathquiz.Entities.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IGradeRepository extends JpaRepository<Grade, String> {
    List<Grade> findByLevelId(String levelId);
}
