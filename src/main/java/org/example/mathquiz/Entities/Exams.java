package org.example.mathquiz.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Exams {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "quiz_matrix_id", nullable = false)
    private QuizMatrix quizMatrix;

    private Integer duration;
    private Boolean isCustomExam;
    private String name;
    private Integer numberOfCorrectAnswer;
    private Integer numberOfQuiz;

    @OneToMany(mappedBy = "exam")
    private List<ExamDetail> examDetails;

    @OneToMany(mappedBy = "exam")
    private List<Result> results;

}

