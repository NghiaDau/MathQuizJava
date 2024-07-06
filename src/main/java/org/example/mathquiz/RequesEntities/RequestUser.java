package org.example.mathquiz.RequesEntities;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.mathquiz.validators.annotations.ValidEmail;
import org.example.mathquiz.validators.annotations.ValidPhone;
import org.example.mathquiz.validators.annotations.ValidUserName;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestUser {
    @ValidUserName
    @Size(min = 1, max = 50, message = "Tên Tài Khoản phải có từ 1 đến 50 ký tự")
    private String userName;
    @Pattern(regexp =  "^.*(?=.{8,})(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!#@$%&? \"]).*$", message = "Mặt khẩu không hợp lệ")
    private String passwordHash;
    private String repasswordHash;
    @Size(min = 1, max = 50, message = "Email phải có từ 1 đến 50 ký tự")
    @Email(message = "Không đúng định dạng Email")
    @ValidEmail
    private String email;
    @Length(min = 10, max = 10, message = "Phone phải có 10 số")
    @Pattern(regexp = "^[0-9]*$", message = "Phone phải là số")
    @ValidPhone
    private String phoneNumber;
    private String avatarUrl;
    private String fullName;
}
