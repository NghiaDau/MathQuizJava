package org.example.mathquiz.RequesEntities;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequesUpdateUser {
    private String id;
    private String email;
    private String phoneNumber;
    private String avatarUrl;
    private String fullName;
}
