package com.gravin.MovieJava.common.validation.annotation;

import com.gravin.MovieJava.common.validation.validator.FileNotEmptyValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FileNotEmptyValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FileNotEmpty {
    String message() default "File must not empty";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
