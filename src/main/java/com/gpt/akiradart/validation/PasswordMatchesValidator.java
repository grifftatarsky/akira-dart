package com.gpt.akiradart.validation;

import com.gpt.akiradart.web.dto.RegistrationUserDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

  @Override
  public void initialize(final PasswordMatches constraintAnnotation) {
    //
  }

  @Override
  public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
    final RegistrationUserDTO user = (RegistrationUserDTO) obj;
    return user.getPassword().equals(user.getMatchingPassword());
  }
}
