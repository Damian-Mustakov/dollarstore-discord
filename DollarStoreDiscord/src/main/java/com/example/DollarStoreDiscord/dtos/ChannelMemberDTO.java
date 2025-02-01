package com.example.DollarStoreDiscord.dtos;

import com.example.DollarStoreDiscord.enums.RoleType;
import lombok.Data;

@Data
public class ChannelMemberDTO {
    private Integer id;
    private ChannelDTO channel;
    private RoleType role;
    private DiscordUserDTO user;
}
