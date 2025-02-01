package com.example.DollarStoreDiscord.dtos;

import lombok.Data;

@Data
public class DiscordUserDTO {
    private Integer id;
    private String username;
    private String email;
    private Boolean isDeleted;
}
