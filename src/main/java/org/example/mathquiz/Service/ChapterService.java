package org.example.mathquiz.Service;

import lombok.RequiredArgsConstructor;
import org.example.mathquiz.Entities.Chapter;

import org.example.mathquiz.Repositories.IChapterRepository;
import org.example.mathquiz.RequesEntities.RequestModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE,
        rollbackFor = {Exception.class, Throwable.class})
public class ChapterService {
    private final IChapterRepository chapterRepository;
    public List<Chapter> getAllChapters() {
        return chapterRepository.findAll();
    }
    public Optional<Chapter> getChapterById(String id) {
        return chapterRepository.findById(id);
    }
    public Chapter getChapterByIdNon(String id) {
        return chapterRepository.findChapterById(id);
    }
    public List<Chapter> getChapterbyGrade(String id){
        return chapterRepository.findChapterbyGrade(id);
    }
    public Chapter addChapter(Chapter chapter) {
        try {
            return chapterRepository.save(chapter);
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    public Chapter updateChapter(Chapter chapter) {
        return chapterRepository.save(chapter);
    }
    public void deleteChapterById(String id) {
        chapterRepository.deleteById(id);
    }
    public List<Chapter> findByGradeId(String gradeId) {
        return chapterRepository.findByGradeId(gradeId).stream()
                .sorted((chapter1, chapter2) -> chapter1.getName().compareToIgnoreCase(chapter2.getName()))
                .collect(Collectors.toList());
    }

    public List<Chapter> findByGradeAndMathType(String gradeId, String mathTypeId) {
        if (mathTypeId != null) {
            return chapterRepository.findByGradeIdAndMathTypeId(gradeId, mathTypeId).stream()
                    .sorted((chapter1, chapter2) -> chapter1.getName().compareToIgnoreCase(chapter2.getName()))
                    .collect(Collectors.toList());
        } else {
            return chapterRepository.findByGradeId(gradeId).stream()
                    .sorted((chapter1, chapter2) -> chapter1.getName().compareToIgnoreCase(chapter2.getName()))
                    .collect(Collectors.toList());
        }
    }
}
