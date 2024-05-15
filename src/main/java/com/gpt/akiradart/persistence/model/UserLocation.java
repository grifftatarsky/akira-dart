package com.gpt.akiradart.persistence.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
public class UserLocation {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String country;

  private boolean enabled;

  @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
  @JoinColumn(nullable = false, name = "user_id")
  private User user;

  public UserLocation() {
    this.enabled = false;
  }

  public UserLocation(String country, User user) {
    this.country = country;
    this.user = user;
    this.enabled = false;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((country == null) ? 0 : country.hashCode());
    result = prime * result + (enabled ? 1231 : 1237);
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((user == null) ? 0 : user.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    UserLocation other = (UserLocation) obj;
    if (country == null) {
      if (other.country != null) {
        return false;
      }
    } else if (!country.equals(other.country)) {
      return false;
    }
    if (enabled != other.enabled) {
      return false;
    }
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
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
    return "UserLocation [id=" + id + ", country=" + country + ", enabled=" + enabled + ", user=" + user + "]";
  }
}
