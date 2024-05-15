package com.gpt.akiradart.persistence.model.token;

import com.gpt.akiradart.persistence.model.UserLocation;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class NewLocationToken {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String token;

  @OneToOne(targetEntity = UserLocation.class, fetch = FetchType.EAGER)
  @JoinColumn(nullable = false, name = "user_location_id")
  private UserLocation userLocation;

  public NewLocationToken(final String token) {
    this.token = token;
  }

  public NewLocationToken(final String token, final UserLocation userLocation) {
    this.token = token;
    this.userLocation = userLocation;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((token == null) ? 0 : token.hashCode());
    result = prime * result + ((userLocation == null) ? 0 : userLocation.hashCode());
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
    NewLocationToken other = (NewLocationToken) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    if (token == null) {
      if (other.token != null) {
        return false;
      }
    } else if (!token.equals(other.token)) {
      return false;
    }
    if (userLocation == null) {
      return other.userLocation == null;
    } else {
      return userLocation.equals(other.userLocation);
    }
  }

  @Override
  public String toString() {
    return "NewLocationToken [id=" + id + ", token=" + token + ", userLocation=" + userLocation + "]";
  }
}