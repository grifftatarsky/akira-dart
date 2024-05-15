package com.gpt.akiradart.captcha;

import com.gpt.akiradart.web.error.ReCaptchaInvalidException;
import com.gpt.akiradart.web.error.ReCaptchaUnavailableException;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

@Slf4j
@Service("captchaServiceV3")
public class CaptchaServiceV3 extends AbstractCaptchaService {

  public static final String REGISTER_ACTION = "register";

  public CaptchaServiceV3(HttpServletRequest request,
      CaptchaSettings captchaSettings,
      ReCaptchaAttemptService reCaptchaAttemptService,
      RestOperations restTemplate) {
    super(request, captchaSettings, reCaptchaAttemptService, restTemplate);
  }

  @Override
  public void processResponse(String response, final String action) throws
      ReCaptchaInvalidException {
    securityCheck(response);

    final URI verifyUri = URI.create(
        String.format(RECAPTCHA_URL_TEMPLATE, getReCaptchaSecret(), response, getClientIP()));
    try {
      final GoogleResponse googleResponse =
          restTemplate.getForObject(verifyUri, GoogleResponse.class);
      log.debug("Google's response: {} ", Objects.requireNonNull(googleResponse));

      if (!googleResponse.isSuccess()
          || !googleResponse.getAction().equals(action)
          || googleResponse.getScore() < captchaSettings.getThreshold()) {
        if (googleResponse.hasClientError()) {
          reCaptchaAttemptService.reCaptchaFailed(getClientIP());
        }
        throw new ReCaptchaInvalidException("reCaptcha was not successfully validated");
      }
    } catch (RestClientException rce) {
      throw new ReCaptchaUnavailableException(
          "Registration unavailable at this time.  Please try again later.", rce);
    }
    reCaptchaAttemptService.reCaptchaSucceeded(getClientIP());
  }
}
