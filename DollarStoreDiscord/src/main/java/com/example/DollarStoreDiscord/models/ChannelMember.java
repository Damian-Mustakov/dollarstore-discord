package com.example.DollarStoreDiscord.models;

import com.example.DollarStoreDiscord.enums.RoleType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name="channel_members")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private DiscordUser user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType role;
}
