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
}
