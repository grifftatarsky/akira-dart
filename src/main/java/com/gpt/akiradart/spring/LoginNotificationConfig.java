package com.gpt.akiradart.spring;

import com.maxmind.geoip2.DatabaseReader;
import java.io.File;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import ua_parser.Parser;

@Slf4j
@Configuration
public class LoginNotificationConfig {

  @Bean
  public Parser uaParser() {
    return new Parser();
  }

  @Bean(name = "GeoIPCity")
  public DatabaseReader databaseReader() throws IOException {
    File database = ResourceUtils
        .getFile("classpath:maxmind/GeoLite2-City.mmdb");
    DatabaseReader reader = new DatabaseReader.Builder(database)
        .build();
    log.info("The GeoLite database has been created from {}", database.getAbsolutePath());
    return reader;
  }
}
