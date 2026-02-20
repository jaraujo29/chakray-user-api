package com.chakray.exercise.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AndresFormatValidator implements ConstraintValidator<AndresFormat, String> {

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {

        if (phone == null || phone.isBlank()) {
            return true;
        }
        String clean = phone.replaceAll("\\s+", "");
        return clean.matches("^(\\+\\d{1,3})?\\d{10}$");
    }
}
