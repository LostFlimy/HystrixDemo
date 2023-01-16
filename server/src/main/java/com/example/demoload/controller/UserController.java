package com.example.demoload.controller;

import com.example.demoload.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
  @Autowired
  private UserService userService;

  @GetMapping(path = "/user/{id}")
  public String getUser(@PathVariable String id) {
    return userService.searchUser(id).toString();
  }
}
