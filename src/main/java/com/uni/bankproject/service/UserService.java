package com.uni.bankproject.service;

import com.uni.bankproject.dto.UserDTO;
import com.uni.bankproject.entity.User;
import com.uni.bankproject.mapper.UserMapper;
import com.uni.bankproject.repository.UserRepository;
import com.uni.bankproject.utils.JwtUtils;
import com.uni.bankproject.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, JwtUtils jwtUtils, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers(String token) {

        String username = jwtUtils.getUsernameFromToken(token);

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("User with username " + username + " not found")
        );

        // Check if the user is an admin
        if (!user.isAdmin()) {
            throw new RuntimeException("User with username " + username + " is not an admin");
        }

        return userRepository.findAll().stream()
                .map(UserMapper.INSTANCE::mapToDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserDTO getUserById(String token) {

        String usernameFromToken = jwtUtils.getUsernameFromToken(token);

        return userRepository.findByUsername(usernameFromToken).map(UserMapper.INSTANCE::mapToDTO).orElseThrow(
                () -> new RuntimeException("User with username " + usernameFromToken + " not found")
        );
    }

    @Transactional
    public void deleteUser(String token) {

        String usernameFromToken = jwtUtils.getUsernameFromToken(token);

        User user = userRepository.findByUsername(usernameFromToken).orElseThrow(
                () -> new RuntimeException("User with username " + usernameFromToken + " not found"));

        userRepository.deleteById(user.getUserID());
    }

    @Transactional
    public void updateUser(String token, UserDTO userDTO) {

        String usernameFromToken = jwtUtils.getUsernameFromToken(token);

        User user = userRepository.findByUsername(usernameFromToken).orElseThrow(
                () -> new RuntimeException("User with username " + usernameFromToken + " not found"));

        // Validate userDTO fields
        validateUserDTO(userDTO);

        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setAddress(userDTO.getAddress());

        userRepository.save(user);
    }

    private void validateUserDTO(UserDTO userDTO) {
        if (!ValidationUtils.isUserNameValid(userDTO.getUserName())) {
            throw new IllegalArgumentException("Invalid username: must be at least 3 characters long and contain only letters and numbers");
        }
        if (!ValidationUtils.isEmailValid(userDTO.getEmail())) {
            throw new IllegalArgumentException("Invalid email: must follow the format example@domain.com");
        }
        if (!ValidationUtils.isPhoneNumberValid(userDTO.getPhoneNumber())) {
            throw new IllegalArgumentException("Invalid phone number: must be a 10-digit number");
        }
        if (!ValidationUtils.isAddressValid(userDTO.getAddress())) {
            throw new IllegalArgumentException("Invalid address: must contain only letters, numbers, spaces, commas, periods, and hyphens");
        }
    }

    public void changePassword(String token, String currentPassword, String newPassword, String confirmPassword) {
        String usernameFromToken = jwtUtils.getUsernameFromToken(token);

        User user = userRepository.findByUsername(usernameFromToken).orElseThrow(
                () -> new RuntimeException("User with username " + usernameFromToken + " not found"));

        if (!passwordEncoder.matches(currentPassword, user.getUserKey())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }

        if (!ValidationUtils.isUserKeyValid(newPassword)) {
            throw new IllegalArgumentException("Invalid password: must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character");
        }

        user.setUserKey(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void checkIfUserIsAdmin(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("User with username " + username + " not found")
        );

        if (!user.isAdmin()) {
            throw new RuntimeException("User with username " + username + " is not an admin");
        }
    }
}
