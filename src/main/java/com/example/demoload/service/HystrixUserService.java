package com.example.demoload.service;

import com.example.demoload.model.User;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

public class HystrixUserService implements UserService{

  private final RestTemplate http;
  @Value("${external.service.url}")
  private String url;
  @Value("${external.service.user.path}")
  private String path;
  @Value("${hystrix.maxTimeExecute}")
  private int timeoutExecution;
  @Value("${hystrix.maxFailedExecutions}")
  private int maxFailedExecution;

  public HystrixUserService() {
    http = new RestTemplate();
  }

  @Override
  public User searchUser(String id) {
    return new UserCommand(id).execute();
  }

  private class UserCommand extends HystrixCommand<User> {
    private final String id;

    public UserCommand(String id) {
      super(
          Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(id))
              .andCommandKey(HystrixCommandKey.Factory.asKey(id))
              .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                  .withCircuitBreakerEnabled(true)
                  .withCircuitBreakerRequestVolumeThreshold(maxFailedExecution)
                  .withCircuitBreakerSleepWindowInMilliseconds(10000)
                  .withExecutionTimeoutInMilliseconds(timeoutExecution)
                  .withExecutionTimeoutEnabled(true))
      );
      this.id = id;
    }

    @Override
    protected User run() throws Exception {
      try {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        ResponseEntity<User> userEntity = http.getForEntity(url + path, User.class , params);
        return userEntity.getBody();
      } catch (Exception ex) {
        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    @Override
    protected User getFallback() {
      return new User();
    }
  }
}
