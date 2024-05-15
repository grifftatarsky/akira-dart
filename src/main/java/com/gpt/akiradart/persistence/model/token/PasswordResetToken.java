package com.gpt.akiradart.persistence.model.token;

import com.gpt.akiradart.persistence.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.util.Calendar;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetToken {

  private static final int EXPIRATION = 60 * 24;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String token;

  @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
  @JoinColumn(nullable = false, name = "user_id")
  private User user;

  private Date expiryDate;

  public PasswordResetToken(final String token) {
    this.token = token;
    this.expiryDate = calculateExpiryDate();
  }

  public PasswordResetToken(final String token, final User user) {
    this.token = token;
    this.user = user;
    this.expiryDate = calculateExpiryDate();
  }

  private Date calculateExpiryDate() {
    final Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(new Date().getTime());
    cal.add(Calendar.MINUTE, EXPIRATION);
    return new Date(cal.getTime().getTime());
  }

  public void updateToken(final String token) {
    this.token = token;
    this.expiryDate = calculateExpiryDate();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((expiryDate == null) ? 0 : expiryDate.hashCode());
    result = prime * result + ((token == null) ? 0 : token.hashCode());
    result = prime * result + ((user == null) ? 0 : user.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    PasswordResetToken other = (PasswordResetToken) obj;
    if (expiryDate == null) {
      if (other.expiryDate != null) {
        return false;
      }
    } else if (!expiryDate.equals(other.expiryDate)) {
      return false;
    }
    if (token == null) {
      if (other.token != null) {
        return false;
      }
    } else if (!token.equals(other.token)) {
      return false;
    }
    if (user == null) {
      return other.user == null;
    } else {
      return user.equals(other.user);
    }
  }

  @Override
  public String toString() {
    return "Token [String=" + token + "] [Expires=" + expiryDate + "]";
  }
}