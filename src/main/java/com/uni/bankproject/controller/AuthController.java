package com.uni.bankproject.controller;

import com.uni.bankproject.dto.LoginRequest;
import com.uni.bankproject.dto.RegisterRequest;
import com.uni.bankproject.exception.CustomAuthenticationException;
import com.uni.bankproject.service.AuthService;
import com.uni.bankproject.utils.CookieUtils;
import com.uni.bankproject.utils.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/auth")
public class AuthController {


    private final AuthService authService;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthController(AuthService authService, JwtUtils jwtUtils) {
        this.authService = authService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/login")
    public String login(@CookieValue(value = "jwt", defaultValue = "") String token) {
        try {
            if (!token.isEmpty() && jwtUtils.isTokenValid(token)) {
                return "redirect:/api/v1/user";
            }
        } catch (Exception e) {
            throw new CustomAuthenticationException(e);
        }
        return "login";
    }

    @GetMapping("/register")
    public String register(@CookieValue(value = "jwt", defaultValue = "") String token) {
        try {
            if (!token.isEmpty() && jwtUtils.isTokenValid(token)) {
                return "redirect:/api/v1/user";
            }
        } catch (Exception e) {
            throw new CustomAuthenticationException(e);
        }
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

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = CookieUtils.createCookie("jwt", "", 0);
        response.addCookie(cookie);
        response.addHeader("Set-Cookie", CookieUtils.addCookie(cookie, "Strict"));
        return "redirect:/api/v1/auth/login";
    }
}
