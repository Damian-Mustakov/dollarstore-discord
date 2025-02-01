package com.example.DollarStoreDiscord.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="friend_chats")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class FriendChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private DiscordUser sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private DiscordUser receiver;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;
}
