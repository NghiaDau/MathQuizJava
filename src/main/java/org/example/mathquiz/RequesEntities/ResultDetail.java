package org.example.mathquiz.RequesEntities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.mathquiz.Entities.Quiz;
import org.example.mathquiz.Entities.QuizOption;
import org.example.mathquiz.Entities.Result;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultDetail {
    private Quiz quiz;
    private QuizOption yourQuizOption;
}
