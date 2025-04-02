package com.uni.bankproject.service;

import com.uni.bankproject.dto.LoginRequest;
import com.uni.bankproject.dto.RegisterRequest;
import com.uni.bankproject.entity.User;
import com.uni.bankproject.repository.UserRepository;
import com.uni.bankproject.utils.JwtUtils;
import com.uni.bankproject.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @Autowired
    public AuthService(JwtUtils jwtUtils, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String login(LoginRequest request) {
        //Validate the user
        validateLoginRequest(request);
        String username = request.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("User with username " + username + " not found")
        );

        //Check if the password is correct
        if (!passwordEncoder.matches(request.getPassword(), user.getUserKey())) {
            throw new IllegalArgumentException("Invalid password");
        }

        //Generate a token
        return jwtUtils.getToken(user);
    }

    @Transactional
    public String register(RegisterRequest request) {
        //Validate the user
        validateRegisterRequest(request);

        //Check if the user already exists
        if (userRepository.findById(request.getUserID()).isPresent()) {
            throw new IllegalArgumentException("User with username " + request.getUserName() + " already exists");
        }

        //Create a new user
        User user = new User();
        user.setUserID(request.getUserID());
        user.setUserName(request.getUserName());
        // Uses BCryptPasswordEncoder to encode the password --> Never decodes again (only compares)
        user.setUserKey(passwordEncoder.encode(request.getUserKey()));
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());


        try {
            user = userRepository.save(user);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        //Generate a token
        return jwtUtils.getToken(user);
    }

    private void validateRegisterRequest(RegisterRequest request) {
        if (!ValidationUtils.isUserIDValid(request.getUserID())) {
            throw new IllegalArgumentException("Invalid user ID: must be a 10-digit number");
        }
        if (!ValidationUtils.isUserNameValid(request.getUserName())) {
            throw new IllegalArgumentException("Invalid username: must be at least 3 characters long and contain only letters and numbers");
        }
        if (!ValidationUtils.isUserKeyValid(request.getUserKey())) {
            throw new IllegalArgumentException("Invalid password: must be a 4-digit number");
        }
        if (!ValidationUtils.isEmailValid(request.getEmail())) {
            throw new IllegalArgumentException("Invalid email: must follow the format example@domain.com");
        }
        if (!ValidationUtils.isPhoneNumberValid(request.getPhoneNumber())) {
            throw new IllegalArgumentException("Invalid phone number: must be a 10-digit number");
        }
        if (!ValidationUtils.isAddressValid(request.getAddress())) {
            throw new IllegalArgumentException("Invalid address: must contain only letters, numbers, spaces, commas, periods, and hyphens");
        }
    }

    private void validateLoginRequest(LoginRequest request) {
        if (!ValidationUtils.isUserNameValid(request.getUsername())) {
            throw new IllegalArgumentException("Invalid username: must be at least 3 characters long and contain only letters and numbers");
        }
        if (!ValidationUtils.isUserKeyValid(request.getPassword())) {
            throw new IllegalArgumentException("Invalid password: must be a 4-digit number");
        }
    }
}