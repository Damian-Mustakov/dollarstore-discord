package com.example.DollarStoreDiscord.controllers;

import com.example.DollarStoreDiscord.dtos.ChannelDTO;
import com.example.DollarStoreDiscord.dtos.DiscordUserDTO;
import com.example.DollarStoreDiscord.services.FriendChatService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/friends")
public class FriendChatController {

    private final FriendChatService friendChatService;

    @PostMapping("/{userId}/add-friend")
    public ResponseEntity<?> addFriend( @PathVariable Integer userId,  @RequestParam Integer friendId) {
        try {
            ChannelDTO channelDTO = friendChatService.addFriend(userId, friendId);
            return ResponseEntity.status(HttpStatus.OK).body(channelDTO);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error adding friend: " + ex.getMessage());
        }
    }

    @GetMapping("/{userId}/list")
    public ResponseEntity<?> getFriends(@PathVariable Integer userId) {
        try {
            List<DiscordUserDTO> friends = friendChatService.getFriendsForUser(userId);
            if (friends.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(friends);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + ex.getMessage());
        }
    }
}
