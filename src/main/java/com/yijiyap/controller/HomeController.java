package com.yijiyap.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// Makes this a Rest API
@RestController
public class HomeController {

    // Waits for GET request
    @GetMapping
    public String home() {
        return "Welcome to trading platform!";
    }
}
