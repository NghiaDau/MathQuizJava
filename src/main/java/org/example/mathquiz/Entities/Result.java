package org.example.mathquiz.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JsonBackReference(value = "user-result")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JsonBackReference(value = "exam-result")
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    private Integer correctAnswers;
    private Date endTime;
    private Double score;
    private Date startTime;
    private Integer totalQuiz;

}
