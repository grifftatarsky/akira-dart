package com.gpt.akiradart.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;

@Configuration
public class OpenAPIConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Akira Dart Open API Utility")
            .version("1.0")
            .description("This utility allows for easy, quick testing of various akira-dart APIs.")
            .contact(new Contact()
                .name("Griffith Tatarsky")
                .email("griff@akira-app.io")
                .url("https://akira-app.io")
            )
        );
  }
}
