package org.example.mathquiz.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JsonBackReference(value = "user-exam")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JsonBackReference(value = "quizMatrix-exam")
    @JoinColumn(name = "quiz_matrix_id", nullable = false)
    private QuizMatrix quizMatrix;

    private Integer duration;
    private Boolean isCustomExam;
    private String name;
    private Integer numberOfCorrectAnswer;
    private Integer numberOfQuiz;

    @OneToMany(mappedBy = "exam")
    @JsonManagedReference(value = "exam-examDetail")
    private List<ExamDetail> examDetails;

    @JsonManagedReference(value = "exam-result")
    @OneToMany(mappedBy = "exam")
    private List<Result> results;

}
