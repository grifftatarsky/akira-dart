package com.gpt.akiradart.persistence.dao;

import com.gpt.akiradart.persistence.model.User;
import com.gpt.akiradart.persistence.model.UserLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLocationRepository extends JpaRepository<UserLocation, Long> {
  UserLocation findByCountryAndUser(String country, User user);
}