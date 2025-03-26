package com.uni.bankproject.controller;

import com.uni.bankproject.dto.UserDTO;
import com.uni.bankproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getUserById(@CookieValue(value = "jwt") String token, Model model) {
        UserDTO user = userService.getUserById(token);
        model.addAttribute("user", user);
        return "user";
    }

    @PostMapping("/delete")
    public String deleteUser(@CookieValue(value = "jwt") String token) {
        userService.deleteUser(token);
        return "redirect:/api/v1/auth/register";
    }

    @PostMapping("/update")
    public String updateUser(@CookieValue(value = "jwt") String token, @ModelAttribute UserDTO userDTO) {
        userService.updateUser(token, userDTO);
        return "redirect:/api/v1/user";
    }
}
