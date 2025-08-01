package com.example.pushnotification.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator to ensure that a time string follows the format {@code HHMM} and represents a valid
 * 24-hour time.
 *
 * <p>This class checks if the given string:
 *
 * <ul>
 *   <li>Is exactly 4 digits long.
 *   <li>Contains only numeric values.
 *   <li>Represents a valid hour between {@code 00} and {@code 23}.
 *   <li>Represents a valid minute between {@code 00} and {@code 59}.
 * </ul>
 *
 * The validation helps ensure proper formatting before processing time-related operations.
 *
 * @author  Ramji
 */
public class TimeFormatValidator implements ConstraintValidator<ValidTimeFormat, String> {

  /**
   * Validates whether the given time string follows the {@code HHMM} format and represents a valid
   * time.
   *
   * @param time the time string to be validated (expected format: {@code HHMM})
   * @param constraintValidatorContext provides contextual information during validation
   * @return {@code true} if the time is valid, {@code false} otherwise
   */
  @Override
  public boolean isValid(String time, ConstraintValidatorContext constraintValidatorContext) {
    // Ensure the input is exactly 4 digits
    if (time == null || time.length() != 4 || !time.matches("\\d{4}")) {
      return false;
    }

    try {
      int hours = Integer.parseInt(time.substring(0, 2)); // Extract hours
      int minutes = Integer.parseInt(time.substring(2, 4)); // Extract minutes

      // Validate time constraints
      return (hours >= 0 && hours <= 23) && (minutes >= 0 && minutes <= 59);
    } catch (NumberFormatException e) {
      return false;
    }
  }
}