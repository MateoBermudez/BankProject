package com.uni.bankproject.controller;

import com.uni.bankproject.dto.UserDTO;
import com.uni.bankproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home-admin")
    public String home(Model model, @CookieValue(value = "jwt", defaultValue = "") String token) {
        List<UserDTO> users = userService.getAllUsers(token);
        model.addAttribute("users", users);
        return "home-admin";
    }

    @GetMapping
    public String getUserById(@CookieValue(value = "jwt", defaultValue = "") String token, Model model) {
        UserDTO user = userService.getUserById(token);
        model.addAttribute("user", user);
        return "user";
    }

    @PostMapping("/delete")
    public String deleteUser(@CookieValue(value = "jwt", defaultValue = "") String token) {
        userService.deleteUser(token);
        return "redirect:/api/v1/auth/register";
    }

    @PostMapping("/update")
    public String updateUser(@CookieValue(value = "jwt", defaultValue = "") String token, @ModelAttribute UserDTO userDTO) {
        userService.updateUser(token, userDTO);
        return "redirect:/api/v1/user";
    }

    @PostMapping("/change-password")
    public String changePassword(@CookieValue(value = "jwt", defaultValue = "") String token,
                                 @RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword) {
        userService.changePassword(token, currentPassword, newPassword, confirmPassword);
        return "redirect:/api/v1/user";
    }
}
