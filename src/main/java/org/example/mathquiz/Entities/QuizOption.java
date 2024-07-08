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
public class QuizOption {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Boolean isCorrect;
    private String option;
    @Column(length = 3000)
    private String quizOptionImage;

    @ManyToOne
    @JsonBackReference(value = "quiz-quizOption")
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @JsonManagedReference(value = "quizOption-examDetail")
    @OneToMany(mappedBy = "selectedOption")
    private List<ExamDetail> examDetails;
}
