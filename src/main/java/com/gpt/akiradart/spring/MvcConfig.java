package com.gpt.akiradart.spring;

import com.gpt.akiradart.validation.EmailValidator;
import com.gpt.akiradart.validation.PasswordMatchesValidator;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@EnableWebMvc
@Configuration
@RequiredArgsConstructor
@ComponentScan(basePackages = {"com.gpt.akiradart.web"})
public class MvcConfig implements WebMvcConfigurer {

  // TODO: ANGULAR | I removed the html crap for thymeleaf that was here.

  private final MessageSource messageSource;

  @Override
  public void configureDefaultServletHandling(final DefaultServletHandlerConfigurer configurer) {
    configurer.enable();
  }

  @Override
  public void addResourceHandlers(final ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/resources/**").addResourceLocations("/", "/resources/");
  }

  @Override
  public void addInterceptors(final InterceptorRegistry registry) {
    final LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
    localeChangeInterceptor.setParamName("lang");
    registry.addInterceptor(localeChangeInterceptor);
  }

  // Beans

  @Bean
  public LocaleResolver localeResolver() {
    final CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
    cookieLocaleResolver.setDefaultLocale(Locale.ENGLISH);
    return cookieLocaleResolver;
  }

  @Bean
  public EmailValidator usernameValidator() {
    return new EmailValidator();
  }

  @Bean
  public PasswordMatchesValidator passwordMatchesValidator() {
    return new PasswordMatchesValidator();
  }

  @Bean
  @ConditionalOnMissingBean(RequestContextListener.class)
  public RequestContextListener requestContextListener() {
    return new RequestContextListener();
  }

  @Override
  public Validator getValidator() {
    LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
    validator.setValidationMessageSource(messageSource);
    return validator;
  }

  @Bean
  WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> enableDefaultServlet() {
    return (factory) -> factory.setRegisterDefaultServlet(true);
  }
}
