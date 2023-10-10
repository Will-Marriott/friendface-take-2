package com.scottlogic.grad_training.friendface;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    @RestController
    @RequestMapping("users")
    public class UserController {

        private final UserRepository userRepository;

        public UserController(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        @GetMapping
        public List<User> getUsers() {
            return userRepository.findAll();
        }
    }
}
