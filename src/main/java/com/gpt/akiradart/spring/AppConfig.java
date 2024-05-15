package com.gpt.akiradart.spring;

import com.gpt.akiradart.security.ActiveUserStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
  @Bean
  public ActiveUserStore activeUserStore() {
    return new ActiveUserStore();
  }
}