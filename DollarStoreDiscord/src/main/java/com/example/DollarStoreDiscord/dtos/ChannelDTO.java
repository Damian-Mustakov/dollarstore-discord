package com.example.DollarStoreDiscord.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelDTO {
    private Integer id;
    private String name;
    private DiscordUserDTO owner;
    private Boolean isFriendChat;
    private Boolean isDeleted;
}
