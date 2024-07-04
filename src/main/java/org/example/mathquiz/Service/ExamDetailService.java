package org.example.mathquiz.Service;

import lombok.RequiredArgsConstructor;
import org.example.mathquiz.Entities.ExamDetail;

import org.example.mathquiz.Repositories.IExamDetailRepository;
import org.example.mathquiz.RequesEntities.RequestPushExamDetailList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE,
        rollbackFor = {Exception.class, Throwable.class})
public class ExamDetailService {
    private final IExamDetailRepository examDetailRepository;
    public List<ExamDetail> getAllExamDetails() {
        return examDetailRepository.findAll();
    }
    public List<ExamDetail> getExamDetailsByExamId(String examId) {
        return examDetailRepository.findByExamId(examId);
    }
    public Optional<ExamDetail> getExamDetailById(String id) {
        return examDetailRepository.findById(id);
    }
    public ExamDetail addExamDetail(ExamDetail examDetail) {
        return examDetailRepository.save(examDetail);
    }

    public void addExamDetailList(RequestPushExamDetailList requestPushExamDetailList) {
        List<ExamDetail> examDetailList = new ArrayList<>();
        for (int i = 0; i < requestPushExamDetailList.getQuizList().size(); i++) {
            ExamDetail examDetail = new ExamDetail();
            examDetail.setExam(requestPushExamDetailList.getExam());
            examDetail.setQuiz(requestPushExamDetailList.getQuizList().get(i));
            examDetail.setSelectedOption(-1);
            examDetailList.add(examDetail);
            examDetailRepository.save(examDetail);
        }

    }
    public ExamDetail updateExamDetail(ExamDetail examDetail) {
        return examDetailRepository.save(examDetail);
    }
    public void deleteExamDetailById(String id) {
        examDetailRepository.deleteById(id);
    }


}
