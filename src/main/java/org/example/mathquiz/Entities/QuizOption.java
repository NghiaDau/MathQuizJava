package org.example.mathquiz.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class QuizOption {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Boolean isCorrect;
    private String option;
    private String quizOptionImage;

    @ManyToOne
    @JsonBackReference(value = "quiz-quizOption")
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @JsonBackReference(value = "examDetail-selectedOption")
    @OneToOne(mappedBy = "selectedOption")
    private ExamDetail examDetail;
}
