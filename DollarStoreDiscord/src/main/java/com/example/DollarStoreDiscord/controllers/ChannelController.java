package com.example.DollarStoreDiscord.controllers;

import com.example.DollarStoreDiscord.dtos.ChannelDTO;
import com.example.DollarStoreDiscord.dtos.ChannelMemberDTO;
import com.example.DollarStoreDiscord.services.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController {

    private final ChannelService channelService;

    @PostMapping("/create-channel")
    public ResponseEntity<?> createChannel(
            @RequestParam Integer ownerId,
            @RequestParam String channelName) {
        try {
            ChannelDTO createdChannel = channelService.createChannel(channelName, ownerId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating channel: " + ex.getMessage());
        }
    }


    @DeleteMapping("/{channelId}/delete")
    public ResponseEntity<?> deleteChannel(@PathVariable Integer channelId, @RequestParam Integer userId) {
        try {
            channelService.deleteChannel(channelId, userId);
            return ResponseEntity.status(HttpStatus.OK).body("Channel deleted successfully.");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error deleting channel: " + ex.getMessage());
        }
    }

    @PostMapping("/{channelId}/add-member")
    public ResponseEntity<?> addMemberToChannel(
            @PathVariable Integer channelId,
            @RequestParam Integer executorId,
            @RequestParam Integer newMemberId) {
        try {
            channelService.addMemberToChannel(channelId, executorId, newMemberId);
            return ResponseEntity.status(HttpStatus.OK).body("User added to the channel successfully.");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error adding user to the channel: " + ex.getMessage());
        }
    }

    @DeleteMapping("/{channelId}/remove-member")
    public ResponseEntity<?> removeMemberFromChannel(
            @PathVariable Integer channelId,
            @RequestParam Integer executorId,
            @RequestParam Integer memberId) {
        try {
            channelService.removeMemberFromChannel(channelId, executorId, memberId);
            return ResponseEntity.status(HttpStatus.OK).body("User removed from the channel successfully.");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error removing user from the channel: " + ex.getMessage());
        }
    }
    @PutMapping("/{channelId}/promote-member")
    public ResponseEntity<?> promoteMemberToAdmin(
            @PathVariable Integer channelId,
            @RequestParam Integer executorId,
            @RequestParam Integer memberId) {
        try {
            channelService.promoteMemberToAdmin(channelId, executorId, memberId);
            return ResponseEntity.status(HttpStatus.OK).body("User promoted to ADMIN successfully.");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error promoting user: " + ex.getMessage());
        }
    }

    @GetMapping("/{channelId}/members")
    public ResponseEntity<?> getChannelMembers(@PathVariable Integer channelId) {
        try {
            List<ChannelMemberDTO> members = channelService.getChannelMembers(channelId);
            if (members.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No members found in this channel.");
            }
            return ResponseEntity.status(HttpStatus.OK).body(members);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error fetching channel members: " + ex.getMessage());
        }
    }

    @PutMapping("/{channelId}/change-name")
    public ResponseEntity<?> changeChannelName(
            @PathVariable Integer channelId,
            @RequestParam Integer ownerId,
            @RequestParam String newName) {
        try {
            channelService.changeChannelName(channelId, ownerId, newName);
            Map<String, String> response = new HashMap<>();
            response.put("message", " Channel renamed successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error changing channel name: " + ex.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getGroupChannelsForUser(@PathVariable Integer userId) {
        try {
            List<ChannelDTO> channels = channelService.getGroupChannelsForUser(userId);
            if (channels.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(channels);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error fetching channels: " + ex.getMessage());
        }
    }

    @GetMapping("/{channelId}")
    public ResponseEntity<?> getChannelById(@PathVariable Integer channelId) {
        try {
            ChannelDTO channelDTO = channelService.getChannelById(channelId);
            return ResponseEntity.status(HttpStatus.OK).body(channelDTO);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + ex.getMessage());
        }
    }


}
