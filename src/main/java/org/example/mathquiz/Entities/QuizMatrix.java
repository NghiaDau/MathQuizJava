package org.example.mathquiz.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.mathquiz.RequesEntities.RequestModel;
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

    @ManyToOne
    @JsonBackReference(value = "chapter-quizMatrix")
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    private Integer defaultDuration;
    private String name;
    private boolean Status;
    private Integer numOfQuiz;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updateDate;

    @JsonManagedReference(value = "quizMatrix-quiz")
    @OneToMany(mappedBy = "quizMatrix")
    private List<Quiz> quizs;

    @JsonManagedReference(value = "quizMatrix-exam")
    @OneToMany(mappedBy = "quizMatrix")
    private List<Exam> exams;

    public static QuizMatrix fromRequestModel(RequestModel requestModel, List<Quiz> quizs) {
        QuizMatrix importQuestionVM = new QuizMatrix();
        importQuestionVM.setChapter(requestModel.getChapter());
        importQuestionVM.setName(requestModel.getName_quizMatrix());
        importQuestionVM.setQuizs(quizs);
        importQuestionVM.setCreateDate(new Date());
        importQuestionVM.setNumOfQuiz(quizs.size());
        importQuestionVM.setDefaultDuration(requestModel.getDefaultDuration());
        return importQuestionVM;
    }
}
