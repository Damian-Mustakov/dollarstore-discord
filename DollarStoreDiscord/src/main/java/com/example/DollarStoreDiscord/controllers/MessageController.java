package com.example.DollarStoreDiscord.controllers;

import com.example.DollarStoreDiscord.dtos.MessageDTO;
import com.example.DollarStoreDiscord.services.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(
            @RequestParam Integer senderId,
            @RequestParam Integer channelId,
            @RequestParam String textMessage) {
        try {
            MessageDTO messageDTO = messageService.sendMessage(senderId, channelId, textMessage);
            return ResponseEntity.status(HttpStatus.OK).body(messageDTO);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + ex.getMessage());
        }
    }

    @GetMapping("/{channelId}")
    public ResponseEntity<?> getMessagesForChannel(@PathVariable Integer channelId) {
        try {
            List<MessageDTO> messages = messageService.getMessagesForChannel(channelId);
            if (messages.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(messages);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + ex.getMessage());
        }
    }
}
