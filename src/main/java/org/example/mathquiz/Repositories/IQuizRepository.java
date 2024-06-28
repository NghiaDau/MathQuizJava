package org.example.mathquiz.Repositories;

import org.example.mathquiz.Entities.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IQuizRepository extends JpaRepository<Quiz, String> {
}
