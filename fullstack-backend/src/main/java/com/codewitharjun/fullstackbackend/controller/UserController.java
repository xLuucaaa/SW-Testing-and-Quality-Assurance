package com.codewitharjun.fullstackbackend.controller;

import org.springframework.web.bind.annotation.*;
import com.codewitharjun.fullstackbackend.model.User;
import com.codewitharjun.fullstackbackend.service.UserService;
import java.util.List;

/* Created by Arjun Gautam */
@RestController
@CrossOrigin("http://localhost:3000")
public class UserController {
    private final UserService _userService;

    public UserController(UserService userService) {
        this._userService = userService;
    }

    @GetMapping("/users")
    List<User> getAllUsers() {
        return _userService.getAll();
    }

    @GetMapping("/user/{id}")
    User getUserById(@PathVariable Long id) {
        return _userService.get(id);
    }

    @PostMapping("/user")
    User addUser(@RequestBody User user) {
        return _userService.add(user);
    }

    @PutMapping("/user/{id}")
    User updateUser(@RequestBody User user, @PathVariable Long id) {
        return _userService.update(user, id);
    }

    @DeleteMapping("/user/{id}")
    String deleteUser(@PathVariable Long id){
        return _userService.delete(id);
    }

    @GetMapping("/users/sort")
    List<User> getAllUsersSorted(@RequestParam String sortBy, @RequestParam String sortDirection) {
        return _userService.sort(sortBy, sortDirection);
    }
}
