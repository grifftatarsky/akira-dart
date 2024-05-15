package com.gpt.akiradart.web.dto;

import com.gpt.akiradart.validation.ValidPassword;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordDto {

  private String oldPassword;

  private String token;

  @ValidPassword
  private String newPassword;
}
