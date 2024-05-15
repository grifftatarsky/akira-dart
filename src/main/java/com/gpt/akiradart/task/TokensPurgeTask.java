package com.gpt.akiradart.task;

import com.gpt.akiradart.persistence.dao.token.PasswordResetTokenRepository;
import com.gpt.akiradart.persistence.dao.token.VerificationTokenRepository;
import java.time.Instant;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TokensPurgeTask {

  private final VerificationTokenRepository tokenRepository;

  private final PasswordResetTokenRepository passwordTokenRepository;

  @Scheduled(cron = "${purge.cron.expression}")
  public void purgeExpired() {

    Date now = Date.from(Instant.now());

    passwordTokenRepository.deleteAllExpiredSince(now);
    tokenRepository.deleteAllExpiredSince(now);
  }
}
