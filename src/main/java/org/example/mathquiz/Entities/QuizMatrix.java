package org.example.mathquiz.Entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.mathquiz.RequesEntities.RequestModel;
import org.example.mathquiz.Service.QuizMatrixService;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;


@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuizMatrix {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne()
    @JoinColumn(name = "chapter_id")

    private Chapter chapter;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    private Integer defaultDuration;
    private String name;
    private Integer numOfQuiz;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updateDate;

    @OneToMany(mappedBy = "quizMatrix")
    private List<Quiz> quizs;

    @OneToMany(mappedBy = "quizMatrix")
    private List<Exams> exams;

    public static QuizMatrix fromRequestModel(RequestModel requestModel, List<Quiz> quizs) {
        QuizMatrix importQuestionVM = new QuizMatrix();

        importQuestionVM.setChapter(requestModel.getChapter());
        importQuestionVM.setQuizs(quizs);
        importQuestionVM.setCreateDate(new Date());
        importQuestionVM.setNumOfQuiz(quizs.size());
        importQuestionVM.setDefaultDuration(requestModel.getDefaultDuration());
        return importQuestionVM;
    }
}

