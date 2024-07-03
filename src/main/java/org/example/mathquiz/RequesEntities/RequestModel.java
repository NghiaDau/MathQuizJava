package org.example.mathquiz.RequesEntities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.mathquiz.Entities.Chapter;
import org.example.mathquiz.Entities.Grade;
import org.example.mathquiz.Entities.MathType;
import org.springframework.web.multipart.MultipartFile;

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
    private Integer defaultDuration;
    private Chapter chapter;
}
