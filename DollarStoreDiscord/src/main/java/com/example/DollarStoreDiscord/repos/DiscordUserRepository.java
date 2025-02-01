package com.example.DollarStoreDiscord.repos;

import com.example.DollarStoreDiscord.models.DiscordUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiscordUserRepository extends JpaRepository<DiscordUser, Integer> {
    List<DiscordUser> findByUsernameContainingIgnoreCase(String username);
    Optional<DiscordUser> findByEmail(String email);
    Optional<DiscordUser> findByEmailAndPassword(String email, String password);
}
