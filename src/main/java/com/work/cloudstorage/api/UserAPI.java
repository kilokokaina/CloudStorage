package com.work.cloudstorage.api;

import com.work.cloudstorage.model.User;
import com.work.cloudstorage.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/user")
public class UserAPI {

    private final UserServiceImpl userService;

    @Autowired
    public UserAPI(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.save(user));
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getUser(@PathVariable(value = "id") User user) {
        return ResponseEntity.ok(user);
    }

}
