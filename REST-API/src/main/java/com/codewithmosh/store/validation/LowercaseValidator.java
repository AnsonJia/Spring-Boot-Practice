package com.codewithmosh.store.validation;

import jakarta.validation.ConstraintValidator;

//implementation of Lowercase declaration
public class LowercaseValidator implements ConstraintValidator<Lowercase, String> {//generic interface with <our custom annotation, type of data we want to apply it on>
    @Override //implement our validation logic
    public boolean isValid(String value, jakarta.validation.ConstraintValidatorContext context) {
        if (value == null) return true; //null values are considered valid (lowercase doesn't apply to null objects)
        return value.equals(value.toLowerCase()); //check if the value is equal to its lowercase version
    }
}
