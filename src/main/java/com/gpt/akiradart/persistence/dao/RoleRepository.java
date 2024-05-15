package com.gpt.akiradart.persistence.dao;

import com.gpt.akiradart.persistence.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  Role findByName(String name);

  @Override
  void delete(Role role);
}
