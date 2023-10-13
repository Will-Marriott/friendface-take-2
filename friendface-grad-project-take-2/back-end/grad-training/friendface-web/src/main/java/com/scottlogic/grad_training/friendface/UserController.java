package com.scottlogic.grad_training.friendface;

import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {
    private final UserService userService;

//    private final UserRepository userRepository;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.findAll();
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestParam @NotBlank String username) {
        User user = new User();
        user.setUsername(username);
        userService.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
