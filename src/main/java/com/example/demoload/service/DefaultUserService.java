package com.example.demoload.service;

import com.example.demoload.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

public class DefaultUserService implements UserService {

  private final RestTemplate http;
  @Value("${external.service.url}")
  private String url;
  @Value("${external.service.user.path}")
  private String path;


  public DefaultUserService() {
    this.http = new RestTemplate();
  }

  @Override
  public User searchUser(String id) {
    try {
      Map<String, String> params = new HashMap<>();
      params.put("id", id);
      ResponseEntity<User> userEntity = http.getForEntity(url + path, User.class , params);
      return userEntity.getBody();
    } catch (Exception ex) {
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
