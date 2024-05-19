package com.gpt.akiradart.security.location;

import com.gpt.akiradart.persistence.model.token.NewLocationToken;
import java.util.Locale;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class OnDifferentLocationLoginEvent extends ApplicationEvent {

  private final Locale locale;
  private final String username;
  private final String ip;
  private final NewLocationToken token;
  private final String appUrl;

  public OnDifferentLocationLoginEvent(
      Locale locale,
      String username,
      String ip,
      NewLocationToken token,
      String appUrl
  ) {
    super(token);
    this.locale = locale;
    this.username = username;
    this.ip = ip;
    this.token = token;
    this.appUrl = appUrl;
  }
}
