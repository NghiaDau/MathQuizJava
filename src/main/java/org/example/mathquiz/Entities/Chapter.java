package org.example.mathquiz.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.mathquiz.RequesEntities.RequestModel;

import java.util.Date;
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
    @JsonBackReference(value = "grade-chapter")

    @JoinColumn(name = "grade_id", nullable = false)
    private Grade grade;

    @ManyToOne
    @JsonBackReference(value = "mathType-chapter")
    @JoinColumn(name = "math_type_id")
    private MathType mathType;

    @Column(nullable = false)
    private String name;

    @JsonManagedReference(value = "chapter-quizMatrix")
    @OneToMany(mappedBy = "chapter")
    @JsonIgnore
    private List<QuizMatrix> quizMatrices;

    public static Chapter fromRequestModel(RequestModel requestModel) {
        Chapter chapter = new Chapter();
        chapter.setGrade(requestModel.getGrade());
        chapter.setName(requestModel.getName());
        chapter.setMathType(requestModel.getMathType());
        return chapter;
    }
}
