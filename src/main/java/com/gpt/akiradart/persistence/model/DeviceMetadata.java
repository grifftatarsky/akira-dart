package com.gpt.akiradart.persistence.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Date;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class DeviceMetadata {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private Long userId;
  private String deviceDetails;
  private String location;
  private Date lastLoggedIn;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DeviceMetadata that = (DeviceMetadata) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(userId, that.userId) &&
        Objects.equals(deviceDetails, that.deviceDetails) &&
        Objects.equals(location, that.location) &&
        Objects.equals(lastLoggedIn, that.lastLoggedIn);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, userId, deviceDetails, location, lastLoggedIn);
  }

  @Override
  public String toString() {
    return "DeviceMetadata{" +
        "id=" + id +
        ", userId=" + userId +
        ", deviceDetails='" + deviceDetails + '\'' +
        ", location='" + location + '\'' +
        ", lastLoggedIn=" + lastLoggedIn +
        '}';
  }
}