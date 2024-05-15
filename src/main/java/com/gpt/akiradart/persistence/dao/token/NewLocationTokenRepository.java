package com.gpt.akiradart.persistence.dao.token;

import com.gpt.akiradart.persistence.model.UserLocation;
import com.gpt.akiradart.persistence.model.token.NewLocationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewLocationTokenRepository extends JpaRepository<NewLocationToken, Long> {

  NewLocationToken findByToken(String token);

  NewLocationToken findByUserLocation(UserLocation userLocation);
}