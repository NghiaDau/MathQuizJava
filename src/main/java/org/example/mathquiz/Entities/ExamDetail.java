package org.example.mathquiz.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class ExamDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JsonBackReference(value = "exam-examDetail")
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @ManyToOne
    @JsonBackReference(value = "quiz-examDetail")
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @OneToOne
    @JsonManagedReference(value = "examDetail-selectedOption")
    @JoinColumn(name = "quizOption_id", nullable = true)
    private QuizOption selectedOption;

}
