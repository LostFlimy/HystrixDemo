package com.example.servicewithdelay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
  @Value("${delay}")
  private int delay;

  @GetMapping(path = "/user")
  public User getUser(@RequestParam String id) {
    try {
      Thread.sleep(delay);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    return new User("alex" + id, "Alexandr");
  }
}
