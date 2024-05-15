package com.gpt.akiradart.security;

import com.gpt.akiradart.persistence.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Getter
@Setter
@RequiredArgsConstructor
@Component("myAuthenticationSuccessHandler")
public class MyCustomLoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
  private final ActiveUserStore activeUserStore;

  @Override
  public void onAuthenticationSuccess(final HttpServletRequest request,
      final HttpServletResponse response, final Authentication authentication) throws IOException {
    addWelcomeCookie(gerUserName(authentication), response);

    String userParameter = createEncodedUserParameter(authentication);
    redirectStrategy.sendRedirect(request, response, "/home?" + userParameter);

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
  }

  private String createEncodedUserParameter(Authentication authentication) throws UnsupportedEncodingException {
    User user = (User) authentication.getPrincipal();
    String userInfo = "User [id=" + user.getId() + ", firstName=" + user.getFirstName() +
        ", lastName=" + user.getLastName() + ", email=" + user.getEmail() +
        ", enabled=" + user.isEnabled() + ", isUsing2FA=" + user.isUsing2FA() +
        ", secret=" + user.getSecret() + ", roles=" + user.getRoles() + "]";
    return "user=" + URLEncoder.encode(userInfo, StandardCharsets.UTF_8.toString());
  }

  private String gerUserName(final Authentication authentication) {
    return ((User) authentication.getPrincipal()).getFirstName();
  }

  private void addWelcomeCookie(final String user, final HttpServletResponse response) {
    Cookie welcomeCookie = getWelcomeCookie(user);
    response.addCookie(welcomeCookie);
  }

  private Cookie getWelcomeCookie(final String user) {
    Cookie welcomeCookie = new Cookie("welcome", user);
    welcomeCookie.setMaxAge(60 * 60 * 24 * 30); // 30 days
    return welcomeCookie;
  }

  protected void clearAuthenticationAttributes(final HttpServletRequest request) {
    final HttpSession session = request.getSession(false);
    if (session == null) {
      return;
    }
    session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
  }
}