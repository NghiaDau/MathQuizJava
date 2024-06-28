package org.example.mathquiz.Service;

import lombok.RequiredArgsConstructor;
import org.example.mathquiz.Entities.MathType;

import org.example.mathquiz.Repositories.IMathTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE,
        rollbackFor = {Exception.class, Throwable.class})
public class MathTypeService {
    private final IMathTypeRepository mathTypeRepository;
    public List<MathType> getAllMathTypes() {
        return mathTypeRepository.findAll();
    }
    public Optional<MathType> getMathTypeById(String id) {
        return mathTypeRepository.findById(id);
    }
    public MathType addMathType(MathType mathType) {
        return mathTypeRepository.save(mathType);
    }
    public MathType updateMathType(MathType mathType) {
        return mathTypeRepository.save(mathType);
    }
    public void deleteMathTypeById(String id) {
        mathTypeRepository.deleteById(id);
    }
}
