package com.example.DollarStoreDiscord.services;

import com.example.DollarStoreDiscord.dtos.ChannelDTO;
import com.example.DollarStoreDiscord.dtos.ChannelMemberDTO;
import com.example.DollarStoreDiscord.dtos.DiscordUserDTO;
import com.example.DollarStoreDiscord.enums.RoleType;
import com.example.DollarStoreDiscord.models.Channel;
import com.example.DollarStoreDiscord.models.ChannelMember;
import com.example.DollarStoreDiscord.models.DiscordUser;
import com.example.DollarStoreDiscord.repos.ChannelRepository;
import com.example.DollarStoreDiscord.repos.ChannelMemberRepository;
import com.example.DollarStoreDiscord.repos.DiscordUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final ChannelMemberRepository channelMemberRepository;
    private final DiscordUserRepository discordUserRepository;

    public ChannelService(ChannelRepository channelRepository,
                          ChannelMemberRepository channelMemberRepository,
                          DiscordUserRepository discordUserRepository) {
        this.channelRepository = channelRepository;
        this.channelMemberRepository = channelMemberRepository;
        this.discordUserRepository = discordUserRepository;
    }

    public ChannelDTO createChannel(String name, Integer ownerId) {

        DiscordUser owner = discordUserRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found."));

        Channel channel = new Channel();
        channel.setName(name);
        channel.setIsDeleted(false);
        channel.setOwner(owner);
        channel.setIsFriendChat(false);
        Channel savedChannel = channelRepository.save(channel);

        ChannelMember channelMember = new ChannelMember();
        channelMember.setChannel(savedChannel);
        channelMember.setUser(owner);
        channelMember.setRole(RoleType.OWNER);

        channelMemberRepository.save(channelMember);

        return mapToChannelDTO(savedChannel);
    }

    public void deleteChannel(Integer channelId, Integer userId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found."));

        if (!channel.getOwner().getId().equals(userId)) {
            throw new RuntimeException("Only the owner can delete the channel.");
        }

        boolean isOwner = channelMemberRepository.existsByChannelIdAndUserIdAndRole(channelId, userId, RoleType.OWNER);

        if (!isOwner) {
            throw new RuntimeException("User is not the owner of this channel and cannot delete it.");
        }

        channel.setIsDeleted(true);
        channelRepository.save(channel);
    }


    public void addMemberToChannel(Integer channelId, Integer executorId, Integer newMemberId) {
        boolean isExecutorAuthorized = channelMemberRepository.existsByChannelIdAndUserIdAndRole(channelId, executorId, RoleType.OWNER) ||
                channelMemberRepository.existsByChannelIdAndUserIdAndRole(channelId, executorId, RoleType.ADMIN);

        if (!isExecutorAuthorized) {
            throw new RuntimeException("You do not have permission to add members to this channel.");
        }

        boolean isAlreadyMember = channelMemberRepository.existsByChannelIdAndUserIdAndRole(channelId, newMemberId, RoleType.GUEST) ||
                channelMemberRepository.existsByChannelIdAndUserIdAndRole(channelId, newMemberId, RoleType.ADMIN) ||
                channelMemberRepository.existsByChannelIdAndUserIdAndRole(channelId, newMemberId, RoleType.OWNER);

        if (isAlreadyMember) {
            throw new RuntimeException("User is already a member of the channel.");
        }

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found."));

        DiscordUser newMember = discordUserRepository.findById(newMemberId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        ChannelMember channelMember = new ChannelMember();
        channelMember.setChannel(channel);
        channelMember.setUser(newMember);
        channelMember.setRole(RoleType.GUEST);

        channelMemberRepository.save(channelMember);
    }

    public void removeMemberFromChannel(Integer channelId, Integer executorId, Integer memberId) {
        boolean isOwner = channelMemberRepository.existsByChannelIdAndUserIdAndRole(channelId, executorId, RoleType.OWNER);

        if (!isOwner) {
            throw new RuntimeException("Only the owner can remove members from the channel.");
        }

        ChannelMember channelMember = channelMemberRepository.findByChannelIdAndUserId(channelId, memberId)
                .orElseThrow(() -> new RuntimeException("User is not a member of this channel."));

        if (!channelMember.getRole().equals(RoleType.GUEST)) {
            throw new RuntimeException("Only guests can be removed from the channel.");
        }

        channelMemberRepository.delete(channelMember);
    }

    public void promoteMemberToAdmin(Integer channelId, Integer executorId, Integer memberId) {
        boolean isOwner = channelMemberRepository.existsByChannelIdAndUserIdAndRole(channelId, executorId, RoleType.OWNER);

        if (!isOwner) {
            throw new RuntimeException("Only the owner can promote members.");
        }

        ChannelMember channelMember = channelMemberRepository.findByChannelIdAndUserId(channelId, memberId)
                .orElseThrow(() -> new RuntimeException("User is not a member of this channel."));

        if (!channelMember.getRole().equals(RoleType.GUEST)) {
            throw new RuntimeException("Only guests can be promoted to ADMIN.");
        }

        channelMember.setRole(RoleType.ADMIN);
        channelMemberRepository.save(channelMember);

    }

    public List<ChannelMemberDTO> getChannelMembers(Integer channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found."));

        List<ChannelMember> channelMembers = channelMemberRepository.findByChannel(channel);

        return channelMembers.stream()
                .map(this::mapToChannelMemberDTO)
                .toList();
    }

    public void changeChannelName(Integer channelId, Integer userId, String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("Channel name cannot be empty.");
        }

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found."));

        List<ChannelMember> channelMembers = channelMemberRepository.findByChannel(channel);


        ChannelMember channelMember = channelMembers.stream()
                .filter(member -> member.getUser().getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User is not a member of this channel."));

        if (!(channel.getOwner().getId().equals(userId) || channelMember.getRole() == RoleType.ADMIN)) {
            throw new RuntimeException("Only the channel owner or an admin can change the channel name.");
        }

        channel.setName(newName);
        channelRepository.save(channel);
    }

    public List<ChannelDTO> getGroupChannelsForUser(Integer userId) {
        DiscordUser user = discordUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));


        List<ChannelMember> channelMemberships = channelMemberRepository.findByUser(user);


        return channelMemberships.stream()
                .map(ChannelMember::getChannel)
                .filter(channel -> !channel.getIsDeleted())
                .filter((channel -> !channel.getIsFriendChat()))
                .map(this::mapToChannelDTO)
                .toList();
    }

    public ChannelDTO getChannelById(Integer channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found."));
        return mapToChannelDTO(channel);
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

    private ChannelMemberDTO mapToChannelMemberDTO(ChannelMember channelMember) {
        ChannelMemberDTO channelMemberDTO = new ChannelMemberDTO();
        channelMemberDTO.setId(channelMember.getId());

        ChannelDTO channelDTO = mapToChannelDTO(channelMember.getChannel());
        channelMemberDTO.setChannel(channelDTO);

        channelMemberDTO.setRole(channelMember.getRole());

        DiscordUserDTO userDTO = new DiscordUserDTO();
        userDTO.setId(channelMember.getUser().getId());
        userDTO.setUsername(channelMember.getUser().getUsername());
        userDTO.setEmail(channelMember.getUser().getEmail());
        userDTO.setIsDeleted(channelMember.getUser().getIsDeleted());
        channelMemberDTO.setUser(userDTO);

        return channelMemberDTO;
    }

}
