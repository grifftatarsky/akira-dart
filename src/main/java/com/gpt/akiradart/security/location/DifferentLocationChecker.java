package com.gpt.akiradart.security.location;

import com.gpt.akiradart.persistence.model.token.NewLocationToken;
import com.gpt.akiradart.service.IUserService;
import com.gpt.akiradart.web.error.UnusualLocationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.stereotype.Component;

@Component
public class DifferentLocationChecker implements UserDetailsChecker {

  // TODO: Can't resolve autowired removal for now...
  @Autowired
  private IUserService userService;

  @Autowired
  private HttpServletRequest request;

  @Autowired
  private ApplicationEventPublisher eventPublisher;

  @Override
  public void check(UserDetails userDetails) {
    final String ip = getClientIP();
    final NewLocationToken token = userService.isNewLoginLocation(userDetails.getUsername(), ip);
    if (token != null) {
      final String appUrl = "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
      eventPublisher.publishEvent(new OnDifferentLocationLoginEvent(request.getLocale(), userDetails.getUsername(), ip, token, appUrl));
      throw new UnusualLocationException("unusual location");
    }
  }

  private String getClientIP() {
    final String xfHeader = request.getHeader("X-Forwarded-For");
    if (xfHeader == null || xfHeader.isEmpty() || !xfHeader.contains(request.getRemoteAddr())) {
      return request.getRemoteAddr();
    }
    return xfHeader.split(",")[0];
    // TODO: This is intriguing. Perhaps we can convert this to env vars.
    // return "128.101.101.101"; // for testing United States
    // return "41.238.0.198"; // for testing Egypt
  }
}
