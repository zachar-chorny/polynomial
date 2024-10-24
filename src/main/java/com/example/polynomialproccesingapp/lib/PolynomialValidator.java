package com.example.polynomialproccesingapp.lib;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PolynomialValidator implements ConstraintValidator<ValidPolynomial, String> {
    private static final String VALID_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ+-*/()^ .";

    @Override
    public void initialize(ValidPolynomial constraintAnnotation) {
    }

    @Override
    public boolean isValid(String polynomial, ConstraintValidatorContext context) {
        if (polynomial == null || polynomial.isEmpty()) {
            return false;
        }
        for (char c : polynomial.toCharArray()) {
            if (VALID_CHARS.indexOf(c) == -1) {
                return false;
            }
        }
        int balance = 0;
        for (char c : polynomial.toCharArray()) {
            if (c == '(') balance++;
            if (c == ')') balance--;
            if (balance < 0) return false;
        }
        return balance == 0;
    }

}
