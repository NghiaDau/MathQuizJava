package org.example.mathquiz.RequesEntities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestQuizMatrix {
    private String id;
    private String name;
    private boolean status;
    private Integer defaultDuration;
    // Constructors, getters, and setters
    public RequestQuizMatrix() {}

    public RequestQuizMatrix(String id, String name) {
        this.id = id;
        this.name = name;
    }

}
