package org.example.mathquiz.Entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.mathquiz.RequesEntities.RequestModel;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "grade_id", nullable = false)
    private Grade grade;

    @ManyToOne
    @JoinColumn(name = "math_type_id")
    private MathType mathType;

    @Column(nullable = false)
    private String name;

    @JsonBackReference
    @OneToMany(mappedBy = "chapter")
    private List<QuizMatrix> quizMatrices;


    public static Chapter fromRequestModel(RequestModel requestModel) {
        Chapter chapter = new Chapter();
        chapter.setGrade(requestModel.getGrade());
        chapter.setName(requestModel.getName());
        chapter.setMathType(requestModel.getMathType());
        return chapter;
    }
}

