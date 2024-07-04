package org.example.mathquiz.RequesEntities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.mathquiz.Entities.Exam;
import org.example.mathquiz.Entities.User;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestPushResult {
    private Exam exam;
    private User user;
    private Date startTime;
    private Integer totalQuiz;
}
