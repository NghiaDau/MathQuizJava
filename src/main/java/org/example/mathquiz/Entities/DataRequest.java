package org.example.mathquiz.Entities;

import com.nimbusds.jose.shaded.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DataRequest {
    private String stringValue;
    private List<Quiz> quizArray;
}
