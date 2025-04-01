package com.uni.bankproject.controller;

import com.uni.bankproject.dto.UserDTO;
import com.uni.bankproject.service.UserService;
import com.uni.bankproject.utils.JwtUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final JwtUtils jwtUtils;
    private final UserService userService;

    public HomeController(JwtUtils jwtUtils, UserService userService) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/home";
    }

    @GetMapping("/index")
    public String landing(Model model, @CookieValue(value = "jwt", defaultValue = "") String token) {
        if (isTokenValid(token)) {
            return "redirect:/index-u";
        }
        return "index";
    }

    @GetMapping("/home")
    public String home(Model model, @CookieValue(value = "jwt", defaultValue = "") String token) {
        if (isTokenValid(token)) {
            return "redirect:/home-u";
        }
        return "home";
    }

    @GetMapping("/home-u")
    public String homeUsers(Model model, @CookieValue(value = "jwt") String token) {
        if (!isTokenValid(token)) {
            return "redirect:/home";
        }
        UserDTO user = userService.getUserById(token);
        model.addAttribute("user", user);
        return "home-u";
    }

    @GetMapping("/index-u")
    public String landingUsers(Model model, @CookieValue(value = "jwt") String token) {
        if (!isTokenValid(token)) {
            return "redirect:/index";
        }
        UserDTO user = userService.getUserById(token);
        model.addAttribute("user", user);
        return "index-u";
    }

    // If True -> Wrong token or no token, If False -> Token is valid
    private boolean isTokenValid(String token) {
        try {
            if (token.isEmpty()) {
                return false;
            }
            if (jwtUtils.isTokenValid(token)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}