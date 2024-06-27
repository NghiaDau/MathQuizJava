package org.example.mathquiz.Service;

import lombok.RequiredArgsConstructor;
import org.example.mathquiz.Entities.Level;

import org.example.mathquiz.Repositories.ILevelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE,
        rollbackFor = {Exception.class, Throwable.class})
public class LevelService {
    private final ILevelRepository levelRepository;
    public List<Level> getAllLevels() {
        return levelRepository.findAll();
    }
    public Optional<Level> getLevelById(String id) {
        return levelRepository.findById(id);
    }
    public Level addLevel(Level quizMatrix) {
        return levelRepository.save(quizMatrix);
    }
    public Level updateLevel(Level quizMatrix) {
        return levelRepository.save(quizMatrix);
    }
    public void deleteLevelById(String id) {
        levelRepository.deleteById(id);
    }
}
