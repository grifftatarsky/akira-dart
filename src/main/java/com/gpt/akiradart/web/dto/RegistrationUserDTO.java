package com.gpt.akiradart.web.dto;

import com.gpt.akiradart.validation.PasswordMatches;
import com.gpt.akiradart.validation.ValidEmail;
import com.gpt.akiradart.validation.ValidPassword;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@PasswordMatches
public class RegistrationUserDTO {
  @NotNull
  @Size(min = 1, message = "{Size.userDto.firstName}")
  private String firstName;

  @NotNull
  @Size(min = 1, message = "{Size.userDto.lastName}")
  private String lastName;

  @ValidPassword
  private String password;

  @NotNull
  @Size(min = 1)
  private String matchingPassword;

  @ValidEmail
  @NotNull
  @Size(min = 1, message = "{Size.userDto.email}")
  private String email;

  private boolean isUsing2FA;

  private List<Integer> role;

  // TO STRING

  @Override
  public String toString() {
    return "RegistrationUserDTO [firstName="
        + firstName
        + ", lastName="
        + lastName
        + ", email="
        + email
        + ", isUsing2FA="
        + isUsing2FA
        + ", role="
        + role + "]";
  }
}
