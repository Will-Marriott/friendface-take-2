package com.scottlogic.grad_training.friendface;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @GetMapping
    public String index() {
        return "Sup";
    }
}