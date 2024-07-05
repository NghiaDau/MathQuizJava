package org.example.mathquiz.RequesEntities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestChangePassUser {
    private String id;
    private String oldPassword;
    private String newPassword;
    private String reNewPassword;
}
