package org.example.mathquiz.Repositories;

import org.example.mathquiz.Entities.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IExamRepository extends JpaRepository<Exam, String> {
}
