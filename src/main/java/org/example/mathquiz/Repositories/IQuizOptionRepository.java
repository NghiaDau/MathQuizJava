package org.example.mathquiz.Repositories;

import org.example.mathquiz.Entities.QuizOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IQuizOptionRepository extends JpaRepository<QuizOption, String> {
}
