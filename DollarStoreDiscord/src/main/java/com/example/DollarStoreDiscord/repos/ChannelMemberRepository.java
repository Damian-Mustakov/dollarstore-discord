package com.example.DollarStoreDiscord.repos;

import com.example.DollarStoreDiscord.enums.RoleType;
import com.example.DollarStoreDiscord.models.ChannelMember;
import com.example.DollarStoreDiscord.models.Channel;
import com.example.DollarStoreDiscord.models.DiscordUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChannelMemberRepository extends JpaRepository<ChannelMember, Integer> {

    List<ChannelMember> findByChannel(Channel channel);

    boolean existsByChannelIdAndUserIdAndRole(Integer channelId, Integer userId, RoleType roleType);

    Optional<ChannelMember> findByChannelIdAndUserId(Integer channelId, Integer memberId);

    List<ChannelMember> findByUser(DiscordUser user);
}
