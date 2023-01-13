package com.example.demoload;

import com.example.demoload.service.DefaultUserService;
import com.example.demoload.service.HystrixUserService;
import com.example.demoload.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@org.springframework.context.annotation.Configuration
public class Configuration {
  @Bean("userService")
  @Profile("hystrix")
  public UserService hystrixUserService() {
    return new HystrixUserService();
  }

  @Bean("userService")
  @Profile("default")
  public UserService defaultUserService() {
    return new DefaultUserService();
  }
}
