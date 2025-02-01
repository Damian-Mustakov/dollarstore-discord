package com.example.DollarStoreDiscord.services;

import com.example.DollarStoreDiscord.dtos.ChannelDTO;
import com.example.DollarStoreDiscord.dtos.DiscordUserDTO;
import com.example.DollarStoreDiscord.enums.RoleType;
import com.example.DollarStoreDiscord.models.Channel;
import com.example.DollarStoreDiscord.models.ChannelMember;
import com.example.DollarStoreDiscord.models.DiscordUser;
import com.example.DollarStoreDiscord.models.FriendChat;
import com.example.DollarStoreDiscord.repos.ChannelMemberRepository;
import com.example.DollarStoreDiscord.repos.ChannelRepository;
import com.example.DollarStoreDiscord.repos.DiscordUserRepository;
import com.example.DollarStoreDiscord.repos.FriendChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendChatService {
    private final FriendChatRepository friendChatRepository;
    private final DiscordUserRepository discordUserRepository;
    private final ChannelRepository channelRepository;
    private final ChannelMemberRepository channelMemberRepository;

    @Transactional
    public ChannelDTO addFriend(Integer senderId, Integer receiverId) {
        DiscordUser sender = discordUserRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Friendship sender not found."));
        DiscordUser receiver = discordUserRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Friendship receiver not found."));

        Optional<FriendChat> existingFriendChat = friendChatRepository.findBySenderAndReceiver(sender, receiver);
        if (existingFriendChat.isEmpty()) {
            existingFriendChat = friendChatRepository.findBySenderAndReceiver(receiver, sender);
        }

        if (existingFriendChat.isPresent()) {
            Channel sharedChannel = existingFriendChat.get().getChannel();

            boolean hasFriendInitiatedFriendship = existingFriendChat.get().getSender().getId().equals(receiverId);
            boolean hasCurrentUserInitiatedFriendship = friendChatRepository.findBySenderAndReceiver(receiver, sender).isPresent();

            if (hasFriendInitiatedFriendship && !hasCurrentUserInitiatedFriendship) {

                checkIfMember(sharedChannel, sender);
                checkIfMember(sharedChannel, receiver);

                FriendChat newFriendChat = new FriendChat();
                newFriendChat.setSender(sender);
                newFriendChat.setReceiver(receiver);
                newFriendChat.setChannel(sharedChannel);
                friendChatRepository.save(newFriendChat);
            }

            return mapToChannelDTO(existingFriendChat.get().getChannel());
        } else {

            Channel channel = new Channel();
            channel.setIsDeleted(false);
            String channelName = sender.getUsername() + "-" + receiver.getUsername();
            channel.setName(channelName);
            channel.setOwner(sender);
            channel.setIsFriendChat(true);
            channelRepository.save(channel);

            FriendChat friendship = new FriendChat();
            friendship.setSender(sender);
            friendship.setReceiver(receiver);
            friendship.setChannel(channel);
            friendChatRepository.save(friendship);

            addMembership(channel, sender);
            addMembership(channel, receiver);

            return mapToChannelDTO(channel);
        }
    }

    public List<DiscordUserDTO> getFriendsForUser(Integer userId) {
        List<DiscordUser> friends = friendChatRepository.findFriendsForUser(userId);

        return friends.stream()
                .map(this::mapToDiscordUserDTO)
                .toList();
    }



    private void checkIfMember(Channel channel, DiscordUser user) {
        boolean isAlreadyMember = channelMemberRepository.findByChannel(channel).stream()
                .anyMatch(m -> m.getUser().getId().equals(user.getId()));

        if (!isAlreadyMember) {
            addMembership(channel, user);
        }
    }

    private void addMembership(Channel channel, DiscordUser user) {
        ChannelMember channelMember = new ChannelMember();
        channelMember.setChannel(channel);
        channelMember.setUser(user);
        channelMember.setRole(RoleType.GUEST);
        channelMemberRepository.save(channelMember);
    }

    private DiscordUserDTO mapToDiscordUserDTO(DiscordUser user) {
        DiscordUserDTO userDTO = new DiscordUserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setIsDeleted(user.getIsDeleted());
        return userDTO;
    }

    private ChannelDTO mapToChannelDTO(Channel channel) {
        ChannelDTO channelDTO = new ChannelDTO();
        channelDTO.setId(channel.getId());
        channelDTO.setName(channel.getName());
        channelDTO.setIsDeleted(channel.getIsDeleted());
        channelDTO.setIsFriendChat(channel.getIsFriendChat());

        DiscordUserDTO ownerDTO = new DiscordUserDTO();
        ownerDTO.setId(channel.getOwner().getId());
        ownerDTO.setUsername(channel.getOwner().getUsername());
        ownerDTO.setEmail(channel.getOwner().getEmail());
        ownerDTO.setIsDeleted(channel.getOwner().getIsDeleted());
        channelDTO.setOwner(ownerDTO);

        return channelDTO;
    }
}
