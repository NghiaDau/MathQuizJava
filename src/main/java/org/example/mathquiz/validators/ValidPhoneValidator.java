package org.example.mathquiz.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.mathquiz.validators.annotations.ValidPhone;
import org.springframework.beans.factory.annotation.Autowired;
import org.example.mathquiz.Service.UserService;

public class ValidPhoneValidator implements ConstraintValidator<ValidPhone, String> {
    @Autowired
    private UserService userService;

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (userService == null)
            return true;
        return userService.findByPhone(phone) == null ;
    }
}
