package com.gpt.akiradart.spring;

import com.gpt.akiradart.persistence.dao.UserRepository;
import com.gpt.akiradart.security.CustomRememberMeServices;
import com.gpt.akiradart.security.google2fa.CustomAuthenticationProvider;
import com.gpt.akiradart.security.google2fa.CustomWebAuthenticationDetailsSource;
import com.gpt.akiradart.security.location.DifferentLocationChecker;
import com.maxmind.geoip2.DatabaseReader;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final UserRepository userRepository;
  private final UserDetailsService userDetailsService;
  private final LogoutSuccessHandler myLogoutSuccessHandler;
  private final AuthenticationFailureHandler authenticationFailureHandler;
  private final AuthenticationSuccessHandler myAuthenticationSuccessHandler;
  private final CustomWebAuthenticationDetailsSource authenticationDetailsSource;

  @Bean
  public AuthenticationManager authManager(HttpSecurity http) throws Exception {
    return http.getSharedObject(AuthenticationManagerBuilder.class)
        .authenticationProvider(authProvider())
        .build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring()
        .requestMatchers(new AntPathRequestMatcher("/resources/**", "/h2/**"));
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/h2-console/**",
                "/login*", "/logout*", "/signin/**", "/signup/**",
                "/customLogin", "/user/registration*", "/registrationConfirm*",
                "/expiredAccount*", "/registration*", "/badUser*",
                "/user/resendRegistrationToken*", "/forgetPassword*",
                "/user/resetPassword*", "/user/savePassword*",
                "/updatePassword*", "/user/changePassword*",
                "/emailError*", "/resources/**", "/old/user/registration*",
                "/successRegister*", "/qrcode*", "/user/enableNewLoc*").permitAll()
            .anyRequest().authenticated()
        )
        .formLogin(formLogin -> formLogin
            .successHandler(myAuthenticationSuccessHandler)
            .failureHandler(authenticationFailureHandler)
            .authenticationDetailsSource(authenticationDetailsSource)
            .permitAll()
        )
        .logout(logout -> logout
            .logoutSuccessHandler(myLogoutSuccessHandler)
            .permitAll()
        )
        .csrf(csrf -> csrf
            .ignoringRequestMatchers("/h2-console/**", "/swagger-ui/**", "/v3/api-docs/**")
        )
        .headers(headers -> headers
            .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
        )
        .httpBasic(withDefaults());
    return http.build();
  }


  @Bean
  public SecurityExpressionHandler<FilterInvocation> customWebSecurityExpressionHandler() {
    DefaultWebSecurityExpressionHandler expressionHandler =
        new DefaultWebSecurityExpressionHandler();
    expressionHandler.setRoleHierarchy(roleHierarchy());
    return expressionHandler;
  }

  @Bean
  public DaoAuthenticationProvider authProvider() {
    final CustomAuthenticationProvider authProvider =
        new CustomAuthenticationProvider(userRepository);
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    authProvider.setPostAuthenticationChecks(differentLocationChecker());
    return authProvider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(11);
  }

  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }

  @Bean
  public RememberMeServices rememberMeServices() {
    return new CustomRememberMeServices(userRepository, "theKey", userDetailsService,
        new InMemoryTokenRepositoryImpl());
  }

  @Bean(name = "GeoIPCountry")
  public DatabaseReader databaseReader() throws IOException {
    final File resource = new File(Objects.requireNonNull(this.getClass()
            .getClassLoader()
            .getResource("maxmind/GeoLite2-Country.mmdb"))
        .getFile());
    return new DatabaseReader.Builder(resource).build();
  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
    String hierarchy = "ROLE_ADMIN > ROLE_STAFF \n ROLE_STAFF > ROLE_USER";
    roleHierarchy.setHierarchy(hierarchy);
    return roleHierarchy;
  }

  @Bean
  public HttpSessionEventPublisher httpSessionEventPublisher() {
    return new HttpSessionEventPublisher();
  }

  @Bean
  public DifferentLocationChecker differentLocationChecker() {
    return new DifferentLocationChecker();
  }
}
