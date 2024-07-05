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
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JsonBackReference(value = "difficulty-quiz")
    @JoinColumn(name = "difficulty_id")
    private Difficulty difficulty;

    @ManyToOne
    @JsonBackReference(value = "quizMatrix-quiz")
    @JoinColumn(name = "quiz_matrix_id", nullable = false)
    private QuizMatrix quizMatrix;

    private String image;
    private String imageSolution;
    private String solution;
    private String statement;

    @JsonManagedReference(value = "quiz-quizOption")
    @OneToMany(mappedBy = "quiz")
    private List<QuizOption> quizOptions;

    @JsonManagedReference(value = "quiz-examDetail")
    @OneToMany(mappedBy = "quiz")
    private List<ExamDetail> examDetails;
}
