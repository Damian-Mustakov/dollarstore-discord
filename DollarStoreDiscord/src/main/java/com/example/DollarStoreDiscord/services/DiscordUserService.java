package com.example.DollarStoreDiscord.services;

import com.example.DollarStoreDiscord.dtos.DiscordUserDTO;
import com.example.DollarStoreDiscord.models.DiscordUser;
import com.example.DollarStoreDiscord.repos.DiscordUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiscordUserService {
    private final DiscordUserRepository userRepository;

    public DiscordUserService(DiscordUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public DiscordUserDTO registerUser(DiscordUser user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use.");
        }

        return mapToDTO(userRepository.save(user));
    }

    public Optional<DiscordUserDTO> loginUser(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password)
                .map(this::mapToDTO);
    }

    public List<DiscordUserDTO> findUsersByUsername(String username, Integer userId) {
        List<DiscordUser> users = userRepository.findByUsernameContainingIgnoreCase(username);

        return users.stream()
                .filter(user -> !user.getId().equals(userId))
                .map(this::mapToDTO)
                .toList();
    }

    private DiscordUserDTO mapToDTO(DiscordUser user) {
        DiscordUserDTO dto = new DiscordUserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setIsDeleted(user.getIsDeleted());
        return dto;
    }
}
