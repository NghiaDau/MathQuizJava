package org.example.mathquiz.Repositories;

import org.example.mathquiz.Entities.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IChapterRepository extends JpaRepository<Chapter, String> {
}
