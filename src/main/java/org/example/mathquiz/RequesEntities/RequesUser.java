package org.example.mathquiz.RequesEntities;

import lombok.*;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequesUser {
    private String userName;
    private String passwordHash;
    private String repasswordHash;
    private String email;
    private String phoneNumber;
    private String avatarUrl;
    private String fullName;
}
