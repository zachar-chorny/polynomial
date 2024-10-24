package com.example.polynomialproccesingapp.lib;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PolynomialValidator.class)
@Target({ ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPolynomial {
    String message() default "Invalid polynomial expression";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
