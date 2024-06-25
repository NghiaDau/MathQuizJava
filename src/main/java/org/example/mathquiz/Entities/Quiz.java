package org.example.mathquiz.Entities;

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
    @JoinColumn(name = "difficulty_id")
    private Difficulty difficulty;

    @ManyToOne
    @JoinColumn(name = "quiz_matrix_id", nullable = false)
    private QuizMatrix quizMatrix;

    private String image;
    private String imageSolution;
    private String solution;
    private String statement;

    @OneToMany(mappedBy = "quiz")
    private List<QuizOption> quizOptions;

}