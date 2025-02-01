package com.example.DollarStoreDiscord.repos;

import com.example.DollarStoreDiscord.models.Channel;
import com.example.DollarStoreDiscord.models.DiscordUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channel, Integer> {
    @Query("SELECT c FROM Channel c JOIN FETCH c.channelMembers WHERE c.id = :channelId")
    Optional<Channel> findByIdWithMembers(@Param("channelId") Integer channelId);

}
