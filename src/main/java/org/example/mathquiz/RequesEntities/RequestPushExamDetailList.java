package org.example.mathquiz.RequesEntities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.mathquiz.Entities.Exam;
import org.example.mathquiz.Entities.Quiz;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestPushExamDetailList
{
    private Exam exam;
    private List<Quiz> quizList;
}
