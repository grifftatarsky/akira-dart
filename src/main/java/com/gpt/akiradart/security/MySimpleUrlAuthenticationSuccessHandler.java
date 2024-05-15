package com.gpt.akiradart.security;

import com.gpt.akiradart.persistence.model.User;
import com.gpt.akiradart.service.DeviceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
//@Component("myAuthenticationSuccessHandler")
public class MySimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

  private final ActiveUserStore activeUserStore;

  private final DeviceService deviceService;

  private final Environment env;

  @Override
  public void onAuthenticationSuccess(final HttpServletRequest request,
      final HttpServletResponse response, final Authentication authentication) throws IOException {
    handle(request, response, authentication);
    final HttpSession session = request.getSession(false);
    if (session != null) {
      session.setMaxInactiveInterval(30 * 60);

      String username;
      if (authentication.getPrincipal() instanceof User) {
        username = ((User) authentication.getPrincipal()).getEmail();
      } else {
        username = authentication.getName();
      }
      LoggedUser user = new LoggedUser(username, activeUserStore);
      session.setAttribute("user", user);
    }
    clearAuthenticationAttributes(request);

    loginNotification(authentication, request);
  }

  private void loginNotification(Authentication authentication, HttpServletRequest request) {
    try {
      if (authentication.getPrincipal() instanceof User && isGeoIpLibEnabled()) {
        deviceService.verifyDevice(((User) authentication.getPrincipal()), request);
      }
    } catch (Exception e) {
      log.error("An error occurred while verifying device or location", e);
      throw new RuntimeException(e);
    }
  }

  protected void handle(final HttpServletRequest request, final HttpServletResponse response,
      final Authentication authentication) throws IOException {
    final String targetUrl = determineTargetUrl(authentication);

    if (response.isCommitted()) {
      log.debug("Response has already been committed. Unable to redirect to " + targetUrl);
      return;
    }
    redirectStrategy.sendRedirect(request, response, targetUrl);
  }

  protected String determineTargetUrl(final Authentication authentication) {
    boolean isUser = false;
    boolean isAdmin = false;
    final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    for (final GrantedAuthority grantedAuthority : authorities) {
      if (grantedAuthority.getAuthority().equals("READ_PRIVILEGE")) {
        isUser = true;
      } else if (grantedAuthority.getAuthority().equals("WRITE_PRIVILEGE")) {
        isAdmin = true;
        isUser = false;
        break;
      }
    }
    if (isUser) {
      String username;
      if (authentication.getPrincipal() instanceof User) {
        username = ((User) authentication.getPrincipal()).getEmail();
      } else {
        username = authentication.getName();
      }

      return "/homepage?user=" + username;
    } else if (isAdmin) {
      return "/console";
    } else {
      throw new IllegalStateException();
    }
  }

  protected void clearAuthenticationAttributes(final HttpServletRequest request) {
    final HttpSession session = request.getSession(false);
    if (session == null) {
      return;
    }
    session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
  }

  private boolean isGeoIpLibEnabled() {
    return Boolean.parseBoolean(env.getProperty("geo.ip.lib.enabled"));
  }
}