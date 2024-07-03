package org.example.mathquiz.RequesEntities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.mathquiz.Entities.MathType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestChapterJson {
    private String id;
    private String Name;
    private MathType mathType;
}
