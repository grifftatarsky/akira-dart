package com.gpt.akiradart.security;

public interface ISecurityUserService {
  String validatePasswordResetToken(String token);
}
