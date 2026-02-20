package com.chakray.exercise.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AndresFormatValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AndresFormat {
    String message() default "El número de teléfono debe tener 10 dígitos";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
