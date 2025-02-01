package com.example.DollarStoreDiscord.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageDTO {
    private Integer id;
    private String textMessage;
    private LocalDateTime timestamp;
    private DiscordUserDTO sender;
    private ChannelDTO channel;
}
