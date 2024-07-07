package org.example.mathquiz.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.example.mathquiz.Service.UserService;
import org.example.mathquiz.validators.annotations.ValidEmail;
import org.springframework.beans.factory.annotation.Autowired;


@RequiredArgsConstructor
public class ValidEmailValidator implements ConstraintValidator<ValidEmail, String> {
    @Autowired
    private UserService userService;
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (userService == null)
            return true;
        return userService.findByEmail(email) == null ;
    }
}