package com.gpt.akiradart.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ComponentScan({"com.gpt.akiradart.task"})
public class SpringTaskConfig {
}
