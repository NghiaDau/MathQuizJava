package org.example.mathquiz.RequesEntities;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.mathquiz.Entities.Chapter;
import org.example.mathquiz.Entities.Grade;
import org.example.mathquiz.Entities.MathType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestModel {
    private Grade grade;
    private MathType mathType;
    private String name;
    private String name_quizMatrix;
    private String chapter_id;
    @Min(value = 0, message = "Giá trị tối thiểu là 0")
    @Max(value = 999, message = "Giá trị tối đa là 999")
    private Integer defaultDuration;
    private Chapter chapter;
}
