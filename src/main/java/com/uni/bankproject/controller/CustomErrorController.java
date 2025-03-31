package com.uni.bankproject.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleErrorPage(@RequestParam(name = "message", required = false) String message, Model model) {
        model.addAttribute("errorMessage", Objects.requireNonNullElse(message, "An unexpected error occurred."));
        return "error";
    }

    public String getErrorPath() {
        return "/error";
    }
}
