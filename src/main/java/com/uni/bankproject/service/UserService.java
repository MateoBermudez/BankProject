package com.uni.bankproject.service;

import com.uni.bankproject.dto.UserDTO;
import com.uni.bankproject.entity.User;
import com.uni.bankproject.mapper.UserMapper;
import com.uni.bankproject.repository.UserRepository;
import com.uni.bankproject.utils.JwtUtils;
import com.uni.bankproject.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserService(UserRepository userRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

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
}
