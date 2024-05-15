package com.gpt.akiradart.security;

import com.gpt.akiradart.persistence.dao.token.PasswordResetTokenRepository;
import com.gpt.akiradart.persistence.model.token.PasswordResetToken;
import jakarta.transaction.Transactional;
import java.util.Calendar;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserSecurityService implements ISecurityUserService {

  private final PasswordResetTokenRepository passwordTokenRepository;

  @Override
  public String validatePasswordResetToken(String token) {
    final PasswordResetToken passToken = passwordTokenRepository.findByToken(token);

    return !isTokenFound(passToken) ? "invalidToken"
        : isTokenExpired(passToken) ? "expired"
            : null;
  }

  private boolean isTokenFound(PasswordResetToken passToken) {
    return passToken != null;
  }

  private boolean isTokenExpired(PasswordResetToken passToken) {
    final Calendar cal = Calendar.getInstance();
    return passToken.getExpiryDate().before(cal.getTime());
  }
}