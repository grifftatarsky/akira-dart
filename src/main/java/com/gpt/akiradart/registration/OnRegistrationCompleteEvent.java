package com.gpt.akiradart.registration;

import com.gpt.akiradart.persistence.model.User;
import java.util.Locale;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OnRegistrationCompleteEvent extends ApplicationEvent {

  private final Locale locale;
  private final User user;

  public OnRegistrationCompleteEvent(final User user, final Locale locale) {
    super(user);
    this.user = user;
    this.locale = locale;
  }
}
