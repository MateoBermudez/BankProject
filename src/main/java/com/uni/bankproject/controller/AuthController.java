package com.uni.bankproject.controller;

import com.uni.bankproject.dto.LoginRequest;
import com.uni.bankproject.dto.RegisterRequest;
import com.uni.bankproject.service.AuthService;
import com.uni.bankproject.utils.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/auth")
public class AuthController {


    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest request, HttpServletResponse response) {
        String token = authService.login(request);
        Cookie cookie = CookieUtils.createCookie("jwt", token, 24 * 60 * 60);
        response.addCookie(cookie);
        response.addHeader("Set-Cookie", CookieUtils.addCookie(cookie, "Strict"));
        return "redirect:/api/v1/user";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterRequest request, HttpServletResponse response) {
        String token = authService.register(request);
        Cookie cookie = CookieUtils.createCookie("jwt", token, 24 * 60 * 60);
        response.addCookie(cookie);
        response.addHeader("Set-Cookie", CookieUtils.addCookie(cookie, "Strict"));
        return "redirect:/api/v1/user";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = CookieUtils.createCookie("jwt", "", 0);
        response.addCookie(cookie);
        response.addHeader("Set-Cookie", CookieUtils.addCookie(cookie, "Strict"));
        return "redirect:/api/v1/auth/login";
    }
}
