package org.example.mathquiz.Controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class ReCaptchaResponse {
    private boolean success;
    private String hostname;
    private String challenge_ts;
    @JsonProperty("error-codes")
    private String []errorCode;
}
