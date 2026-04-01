package com.MovieTalk.MT.controller;

import com.MovieTalk.MT.entity.User;
import com.MovieTalk.MT.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{id}/active")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<User> updateUserActiveStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> request) {
        Boolean active = request.get("active");
        if (active == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            User updatedUser = userService.updateUserActiveStatus(id, active);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
