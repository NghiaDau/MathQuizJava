package org.example.mathquiz.Repositories;

import org.example.mathquiz.Entities.QuizMatrix;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IQuizMatrixRepository extends JpaRepository<QuizMatrix, String> {
    default List<QuizMatrix> findAllQuizMatrices(Integer pageNo,
                                    Integer pageSize,
                                    String sortBy) {
        return findAll(PageRequest.of(pageNo,
                pageSize,
                Sort.by(sortBy)))
                .getContent();
    }
}
