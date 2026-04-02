package com.MovieTalk.MT.controller;

import com.MovieTalk.MT.entity.User;
import com.MovieTalk.MT.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/profile")
public class UserProfileController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserProfileController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public User getProfile(Principal principal) {
        return userService.getUserByEmail(principal.getName());
    }

    @PatchMapping("/username")
    public Map<String, Object> updateUsername(@RequestBody Map<String, String> request, Principal principal) {
        String newUsername = request.get("username");
        User user = userService.getUserByEmail(principal.getName());
        user.setName(newUsername);
        User updated = userService.updateUser(user);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Username updated successfully");
        response.put("name", updated.getName());
        return response;
    }

    @PatchMapping("/password")
    public Map<String, String> updatePassword(@RequestBody Map<String, String> request, Principal principal) {
        String currentPassword = request.get("currentPassword");
        String newPassword = request.get("newPassword");
        
        User user = userService.getUserByEmail(principal.getName());
        
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.updateUser(user);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Password updated successfully");
        return response;
    }
}
