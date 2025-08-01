package com.example.pushnotification.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for validating time format in HHMM format.
 *
 * <p>This annotation ensures that the provided value follows the expected time format. It can be
 * applied to fields and method parameters.
 *
 * @author Ramji Yadav
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TimeFormatValidator.class)
public @interface ValidTimeFormat {
  String message() default "Invalid time format. Expected format: HHMM";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}