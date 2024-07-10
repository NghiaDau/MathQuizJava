package org.example.mathquiz.Repositories;

import org.example.mathquiz.Entities.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IResultRepository extends JpaRepository<Result, String> {
    Result findResultByExamId(String examId);
    List<Result> findResultByUserId(String userId);
    Result findResultById(String id);
    @Query("select r,count(r) as takenTimes from Result r where YEAR(r.startTime) = YEAR(current_date) group by r.id order by takenTimes DESC")
    List<Object[]> findResultByCurrentYear();
    @Query("select r,count(r) as takenTimes from Result r where YEAR(r.startTime) = YEAR(current_date) and month(r.startTime) = month(current_date) group by r.id order by takenTimes DESC")
    List<Object[]> findResultByCurrentMonth();
    @Query("select r,count(r) as takenTimes from Result r where YEAR(r.startTime) = YEAR(current_date) and month(r.startTime) = month(current_date) and day(r.startTime) = day(current_date) group by r.id order by takenTimes DESC")
    List<Object[]> findResultByCurrentDay();

    @Query("select r from Result r where r.exam.id = ?1 order by r.endTime desc")
    List<Result> findAndOrderResultByExamId(String examId);

    @Query("select r from Result r where r.exam.id = ?1 and r.user.userName = ?2 order by r.endTime desc")
    List<Result> findAndOrderResultByExamIdAndUserName(String examId, String userName);
}
