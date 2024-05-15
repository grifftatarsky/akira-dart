package com.gpt.akiradart.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jboss.aerogear.security.otp.api.Base32;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "user_account")
public class User {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(unique = true, nullable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String firstName;

  private String lastName;

  private String email;

  @Column(length = 60)
  private String password;

  private boolean enabled;

  private boolean isUsing2FA;

  private String secret;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
  private Collection<Role> roles;

  public User() {
    this.secret = Base32.random();
    this.enabled = false;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((email == null) ? 0 : email.hashCode());
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
    User user = (User) obj;
    return email.equals(user.email);
  }

  @Override
  public String toString() {
    return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email +
        ", enabled=" + enabled + ", isUsing2FA=" + isUsing2FA + ", secret=" + secret + ", roles=" + roles + "]";
  }
}