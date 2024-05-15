package com.gpt.akiradart.captcha;

import com.gpt.akiradart.web.error.ReCaptchaInvalidException;

public interface ICaptchaService {

  default void processResponse(final String response) throws ReCaptchaInvalidException {
  }

  default void processResponse(final String response, String action)
      throws ReCaptchaInvalidException {
  }

  String getReCaptchaSite();

  String getReCaptchaSecret();
}
