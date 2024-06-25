package org.example.mathquiz.Entities;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;


@Entity
public class QuizMatrix {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    private Date createDate;
    private Integer defaultDuration;
    private String name;
    private Integer numOfQuiz;
    private Date updateDate;

    @OneToMany(mappedBy = "quizMatrix")
    private List<Quiz> quizs;

    @OneToMany(mappedBy = "quizMatrix")
    private List<Exams> exams;

}

