package org.example.mathquiz.Service;

import lombok.RequiredArgsConstructor;
import org.example.mathquiz.Entities.Result;

import org.example.mathquiz.Repositories.IResultRepository;
import org.example.mathquiz.RequesEntities.RequestPushResult;
import org.example.mathquiz.RequesEntities.RequestUpdateResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE,
        rollbackFor = {Exception.class, Throwable.class})
public class ResultService {
    private final IResultRepository resultRepository;
    public List<Result> getAllResults() {
        return resultRepository.findAll().stream()
                .sorted(Comparator.comparing(Result::getEndTime))
                .collect(Collectors.toList());
    }
    public Optional<Result> getResultById(String id) {
        return resultRepository.findById(id);
    }
    public Result addResult(RequestPushResult requestPushResult) {
        Result result = new Result();
        result.setUser(requestPushResult.getUser());
        result.setExam(requestPushResult.getExam());
        result.setTotalQuiz(requestPushResult.getTotalQuiz());
        result.setStartTime(requestPushResult.getStartTime());
        return resultRepository.save(result);
    }
    public Result updateResult(RequestUpdateResult requestUpdateResult) {
        Result result = getResultById(requestUpdateResult.getId()).orElseThrow(()-> new IllegalArgumentException("Result not found!"));
        result.setScore(requestUpdateResult.getScore());
        result.setEndTime(requestUpdateResult.getEndTime());
        result.setCorrectAnswers(requestUpdateResult.getCorrectAnswers());
        result.setId(requestUpdateResult.getId());
        return resultRepository.save(result);
    }
    public void deleteResultById(String id) {
        resultRepository.deleteById(id);
    }
    public Result findByExamId(String examId) {
        return resultRepository.findResultByExamId(examId);
    }
    public List<Result> findByUserId(String userId) {
        return resultRepository.findResultByUserId(userId);
    }
}
