package org.example.mathquiz.Repositories;

import org.example.mathquiz.Entities.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IChapterRepository extends JpaRepository<Chapter, String> {
    @Query("SELECT r from Chapter r where r.grade.id = ?1 and r.mathType.id = ?2")
    List<Chapter> findChapterbyGrade (String id, String mathType);
    @Query("select r from Chapter r where r.id =?1")
    Chapter findChapterById(String id);

    List<Chapter> findByGradeId(String gradeId);
    List<Chapter> findByGradeIdAndMathTypeId(String gradeId, String mathTypeId);
}
