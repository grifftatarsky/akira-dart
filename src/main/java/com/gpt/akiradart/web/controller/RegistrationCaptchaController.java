package com.gpt.akiradart.web.controller;

import com.gpt.akiradart.captcha.CaptchaServiceV3;
import com.gpt.akiradart.captcha.ICaptchaService;
import com.gpt.akiradart.persistence.model.User;
import com.gpt.akiradart.registration.OnRegistrationCompleteEvent;
import com.gpt.akiradart.service.IUserService;
import com.gpt.akiradart.web.dto.RegistrationUserDTO;
import com.gpt.akiradart.web.util.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RegistrationCaptchaController {

  private final IUserService userService;
  private final ICaptchaService captchaService;
  private final ICaptchaService captchaServiceV3;
  private final ApplicationEventPublisher eventPublisher;

  // Registration with captcha (v2?)
  @PostMapping("/user/registrationCaptcha")
  public GenericResponse captchaRegisterUserAccount(
      @Valid final RegistrationUserDTO accountDto,
      final HttpServletRequest request
  ) {

    final String response = request.getParameter("g-recaptcha-response");
    captchaService.processResponse(response);

    return registerNewUserHandler(accountDto, request);
  }

  // Registration with reCaptchaV3
  @PostMapping("/user/registrationCaptchaV3")
  public GenericResponse captchaV3RegisterUserAccount(
      @Valid final RegistrationUserDTO accountDto,
      final HttpServletRequest request
  ) {

    final String response = request.getParameter("response");
    captchaServiceV3.processResponse(response, CaptchaServiceV3.REGISTER_ACTION);

    return registerNewUserHandler(accountDto, request);
  }

  private GenericResponse registerNewUserHandler(final RegistrationUserDTO accountDto, final HttpServletRequest request) {
    log.debug("Registering user account with information: {}", accountDto);

    final User registered = userService.registerNewUserAccount(accountDto);
    eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), getAppUrl(request)));
    return new GenericResponse("success");
  }

  private String getAppUrl(HttpServletRequest request) {
    return "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
  }
}
