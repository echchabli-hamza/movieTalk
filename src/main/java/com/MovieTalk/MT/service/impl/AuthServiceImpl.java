package com.MovieTalk.MT.service.impl;



import com.MovieTalk.MT.dto.LoginRequest;
import com.MovieTalk.MT.dto.LoginResponse;
import com.MovieTalk.MT.dto.RegisterRequest;
import com.MovieTalk.MT.entity.User;
import com.MovieTalk.MT.entity.enmus.Role;
import com.MovieTalk.MT.repository.UserRepository;
import com.MovieTalk.MT.security.JwtUtils;
import com.MovieTalk.MT.service.AuthService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtils.generateJwtToken(user.getEmail());
        return new LoginResponse(token, user.getRole().name());
    }

    @Override
    public LoginResponse register(RegisterRequest request) {


        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }


        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.VIEWER);


        userRepository.save(user);


        String token = jwtUtils.generateJwtToken(user.getEmail());
        return new LoginResponse(token, user.getRole().name());
    }

}

