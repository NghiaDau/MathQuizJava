package org.example.mathquiz.Repositories;

import org.example.mathquiz.Entities.Chapter;
import org.example.mathquiz.Entities.QuizMatrix;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IQuizMatrixRepository extends JpaRepository<QuizMatrix, String> {
    @Query("SELECT r from QuizMatrix r where r.chapter.id = ?1")
    List<QuizMatrix> findQuizMatricesByChapter (String id);
    List<QuizMatrix> findByChapterId(String chapterId);

    @Query("select q.name,count(q) as takenTimes,q.id  from QuizMatrix q inner join Exam e on q.id = e.quizMatrix.id inner join Result r on r.exam.id = e.id where YEAR(r.startTime) = YEAR(current_date) group by q.id order by takenTimes DESC")
    List<Object[]> findQuizMatrixByCurrentYear();
    @Query("select q.name,count(q) as takenTimes,q.id  from QuizMatrix q inner join Exam e on q.id = e.quizMatrix.id inner join Result r on r.exam.id = e.id where YEAR(r.startTime) = YEAR(current_date) and month(r.startTime) = month(current_date) group by q.id order by takenTimes DESC")
    List<Object[]> findQuizMatrixByCurrentMonth();
    @Query("select q.name,count(q) as takenTimes,q.id  from QuizMatrix q inner join Exam e on q.id = e.quizMatrix.id inner join Result r on r.exam.id = e.id where YEAR(r.startTime) = YEAR(current_date) and month(r.startTime) = month(current_date) and day(r.startTime) = day(current_date) group by q.id order by takenTimes DESC")
    List<Object[]> findQuizMatrixByCurrentDay();

    @Query("select q.name,q.createDate,q.id from QuizMatrix q order by q.createDate desc")
    List<Object[]> findNewestQuizMatrix();

    @Query("select r.user.userName, r.score, r.endTime, r.id from QuizMatrix q inner join Exam e on q.id = e.quizMatrix.id inner join Result r on e.id = r.exam.id where q.id = ?1 and r.endTime is not null order by r.endTime desc")
    List<Object[]> findQuizMatrixResultByQuizMatrixId(String quizMatrixId);

    @Query("select r.user.userName,r.score,r.endTime,r.id from QuizMatrix q inner join Exam e on q.id = e.quizMatrix.id inner join Result r on e.id = r.exam.id where q.id =?1 and r.user.userName = ?2 and r.endTime is not null order by r.endTime desc")
    List<Object[]> findQuizMatrixResultByQuizMatrixIdAndUserName(String quizMatrixId, String userName);
}
