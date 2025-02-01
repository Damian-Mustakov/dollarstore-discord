package com.example.DollarStoreDiscord.controllers;

import com.example.DollarStoreDiscord.dtos.DiscordUserDTO;
import com.example.DollarStoreDiscord.models.DiscordUser;
import com.example.DollarStoreDiscord.services.DiscordUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class DiscordUserController {

    private final DiscordUserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody DiscordUser userDTO) {
        try {
            DiscordUserDTO createdUser = userService.registerUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String email, @RequestParam String password) {
        try {
            Optional<DiscordUserDTO> userOptional = userService.loginUser(email, password);
            if (userOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(userOptional.get());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
            }
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + ex.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> findUsersByUsername(@RequestParam String username, @RequestParam Integer userId) {
        try {
            List<DiscordUserDTO> users = userService.findUsersByUsername(username, userId);
            if (users.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No users found with the provided username.");
            }
            return ResponseEntity.status(HttpStatus.OK).body(users);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + ex.getMessage());
        }
    }


}