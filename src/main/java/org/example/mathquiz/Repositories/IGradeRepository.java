package org.example.mathquiz.Repositories;

import org.example.mathquiz.Entities.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IGradeRepository extends JpaRepository<Grade, String> {
}
