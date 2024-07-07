package org.example.mathquiz.validators.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.mathquiz.validators.ValidPhoneValidator;


import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidPhoneValidator.class)
@Documented

public @interface ValidPhone {
    String message() default "Phone đã tồn tại";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
