package org.example.mathquiz.RequesEntities;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.mathquiz.Entities.QuizMatrix;
import org.example.mathquiz.Entities.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestPushExam {
    private User user;
    private QuizMatrix quizMatrix;
    private Integer duration;
    private Boolean isCustomExam;
    private int numOfQuiz;
    private String name;
}
