package com.uni.bankproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(Model model) {
        // You can add model attributes here to populate the Thymeleaf template,
        // For example, you could add user data or account data from your service layer
        return "home";
    }
}