package org.example.mathquiz.RequesEntities;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.mathquiz.validators.annotations.ValidEmail;
import org.example.mathquiz.validators.annotations.ValidPhone;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestUpdateUser {
    private String id;
    @Size(min = 1, max = 50, message = "Email phải có từ 1 đến 50 ký tự")
    @Email(message = "Không đúng định dạng Email")
    private String email;
    @Length(min = 10, max = 10, message = "Phone phải có 10 số")
    @Pattern(regexp = "^[0-9]*$", message = "Phone phải là số")
    private String phoneNumber;
    private String avatarUrl;
//    @Size(min = 1, max = 100, message = "Họ Tên có từ 1 đến 100 ký tự")
    private String fullName;
}
