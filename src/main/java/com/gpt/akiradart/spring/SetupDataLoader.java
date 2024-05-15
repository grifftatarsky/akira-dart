package com.gpt.akiradart.spring;

import com.gpt.akiradart.persistence.dao.PrivilegeRepository;
import com.gpt.akiradart.persistence.dao.RoleRepository;
import com.gpt.akiradart.persistence.dao.UserRepository;
import com.gpt.akiradart.persistence.model.Privilege;
import com.gpt.akiradart.persistence.model.Role;
import com.gpt.akiradart.persistence.model.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

  private boolean alreadySetup = false;

  private final UserRepository userRepository;

  private final RoleRepository roleRepository;

  private final PrivilegeRepository privilegeRepository;

  private final PasswordEncoder passwordEncoder;

  // API

  @Override
  @Transactional
  public void onApplicationEvent(final ContextRefreshedEvent event) {
    if (alreadySetup) {
      return;
    }

    // == create initial privileges
    final Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
    final Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
    final Privilege passwordPrivilege = createPrivilegeIfNotFound("CHANGE_PASSWORD_PRIVILEGE");

    // == create initial roles
    final List<Privilege> adminPrivileges =
        new ArrayList<>(Arrays.asList(readPrivilege, writePrivilege, passwordPrivilege));
    final List<Privilege> userPrivileges =
        new ArrayList<>(Arrays.asList(readPrivilege, passwordPrivilege));
    final Role adminRole = createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
    createRoleIfNotFound("ROLE_USER", userPrivileges);

    // == create initial user
    createUserIfNotFound("test@test.com", "Test", "Test", "test", new ArrayList<>(
        List.of(adminRole)));

    alreadySetup = true;
  }

  @Transactional
  public Privilege createPrivilegeIfNotFound(final String name) {
    Privilege privilege = privilegeRepository.findByName(name);
    if (privilege == null) {
      privilege = new Privilege(name);
      privilege = privilegeRepository.save(privilege);
    }
    return privilege;
  }

  @Transactional
  public Role createRoleIfNotFound(final String name, final Collection<Privilege> privileges) {
    Role role = roleRepository.findByName(name);
    if (role == null) {
      role = new Role(name);
    }
    role.setPrivileges(privileges);
    role = roleRepository.save(role);
    return role;
  }

  @Transactional
  public User createUserIfNotFound(final String email, final String firstName,
      final String lastName, final String password, final Collection<Role> roles) {
    User user = userRepository.findByEmail(email);
    if (user == null) {
      user = new User();
      user.setFirstName(firstName);
      user.setLastName(lastName);
      user.setPassword(passwordEncoder.encode(password));
      user.setEmail(email);
      user.setEnabled(true);
    }
    user.setRoles(roles);
    user = userRepository.save(user);
    return user;
  }
}
