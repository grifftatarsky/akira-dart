package com.gpt.akiradart.web.controller;

import com.gpt.akiradart.security.ActiveUserStore;
import com.gpt.akiradart.service.IUserService;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

  private final ActiveUserStore activeUserStore;

  private final IUserService userService;

  @GetMapping("/loggedUsers")
  public String getLoggedUsers(final Locale locale, final Model model) {
    model.addAttribute("users", activeUserStore.getUsers());
    return "users";
  }

  @GetMapping("/loggedUsersFromSessionRegistry")
  public String getLoggedUsersFromSessionRegistry(final Locale locale, final Model model) {
    model.addAttribute("users", userService.getUsersFromSessionRegistry());
    return "users";
  }
}
