package com.example.DollarStoreDiscord.repos;

import com.example.DollarStoreDiscord.models.DiscordUser;
import com.example.DollarStoreDiscord.models.FriendChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendChatRepository extends JpaRepository<FriendChat, Integer> {
    Optional<FriendChat> findBySenderAndReceiver(DiscordUser sender, DiscordUser receiver);

    @Query("SELECT DISTINCT f.receiver FROM FriendChat f WHERE f.sender.id = :userId " +
            "UNION " +
            "SELECT DISTINCT f.sender FROM FriendChat f WHERE f.receiver.id = :userId")
    List<DiscordUser> findFriendsForUser(@Param("userId") Integer userId);

}
