package org.example.mathquiz.Repositories;

import org.example.mathquiz.Entities.Chapter;
import org.example.mathquiz.Entities.QuizMatrix;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IQuizMatrixRepository extends JpaRepository<QuizMatrix, String> {
    @Query("SELECT r from QuizMatrix r where r.chapter.id = ?1")
    List<QuizMatrix> findQuizMatricesByChapter (String id);
}
