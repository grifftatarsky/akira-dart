package com.gpt.akiradart.security;

import com.gpt.akiradart.persistence.dao.UserRepository;
import com.gpt.akiradart.persistence.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Slf4j
public class CustomRememberMeServices extends PersistentTokenBasedRememberMeServices {

  private final UserRepository userRepository;

  private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
  private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource =
      new WebAuthenticationDetailsSource();
  private final PersistentTokenRepository tokenRepository;
  private final String key;

  public CustomRememberMeServices(UserRepository userRepository, String key,
      UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
    super(key, userDetailsService, tokenRepository);
    this.userRepository = userRepository;
    this.tokenRepository = tokenRepository;
    this.key = key;
  }

  @Override
  protected void onLoginSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication successfulAuthentication) {
    String username = ((User) successfulAuthentication.getPrincipal()).getEmail();
    log.debug("Creating new persistent login for user " + username);
    PersistentRememberMeToken persistentToken =
        new PersistentRememberMeToken(username, generateSeriesData(), generateTokenData(),
            new Date());
    try {
      tokenRepository.createNewToken(persistentToken);
      addCookie(persistentToken, request, response);
    } catch (Exception e) {
      log.error("Failed to save persistent token ", e);
    }
  }

  @Override
  protected Authentication createSuccessfulAuthentication(HttpServletRequest request,
      UserDetails user) {
    User authUser = userRepository.findByEmail(user.getUsername());
    RememberMeAuthenticationToken auth = new RememberMeAuthenticationToken(key, authUser,
        authoritiesMapper.mapAuthorities(user.getAuthorities()));
    auth.setDetails(authenticationDetailsSource.buildDetails(request));
    return auth;
  }

  private void addCookie(PersistentRememberMeToken token, HttpServletRequest request,
      HttpServletResponse response) {
    setCookie(new String[] {token.getSeries(), token.getTokenValue()}, getTokenValiditySeconds(),
        request, response);
  }
}
