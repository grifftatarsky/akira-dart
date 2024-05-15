package com.gpt.akiradart.registration.listener;

import com.gpt.akiradart.persistence.model.User;
import com.gpt.akiradart.registration.OnRegistrationCompleteEvent;
import com.gpt.akiradart.service.UserService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
  private final UserService service;

  private final MessageSource messages;

  private final JavaMailSender mailSender;

  private final Environment env;

  // API

  @Override
  public void onApplicationEvent(final OnRegistrationCompleteEvent event) {
    this.confirmRegistration(event);
  }

  private void confirmRegistration(final OnRegistrationCompleteEvent event) {
    final User user = event.getUser();
    final String token = UUID.randomUUID().toString();
    service.createVerificationTokenForUser(user, token);

    final SimpleMailMessage email = constructEmailMessage(event, user, token);
    mailSender.send(email);
  }

  //

  private SimpleMailMessage constructEmailMessage(final OnRegistrationCompleteEvent event,
      final User user, final String token) {
    final String recipientAddress = user.getEmail();
    final String subject = "Registration Confirmation";
    // TODO: Add an actual solution for grabbing server URL.
    final String confirmationUrl = "PLACEHOLDER URL" + "/registrationConfirm?token=" + token;
    final String message = messages.getMessage("message.regSuccLink", null,
        "You registered successfully. To confirm your registration, please click on the below link.",
        event.getLocale());
    final SimpleMailMessage email = new SimpleMailMessage();
    email.setTo(recipientAddress);
    email.setSubject(subject);
    email.setText(message + " \r\n" + confirmationUrl);
    email.setFrom(env.getProperty("support.email"));
    return email;
  }
}
