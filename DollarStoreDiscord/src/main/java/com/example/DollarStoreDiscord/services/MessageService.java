package com.example.DollarStoreDiscord.services;

import com.example.DollarStoreDiscord.dtos.ChannelDTO;
import com.example.DollarStoreDiscord.dtos.DiscordUserDTO;
import com.example.DollarStoreDiscord.dtos.MessageDTO;
import com.example.DollarStoreDiscord.models.Channel;
import com.example.DollarStoreDiscord.models.DiscordUser;
import com.example.DollarStoreDiscord.models.Message;
import com.example.DollarStoreDiscord.repos.ChannelRepository;
import com.example.DollarStoreDiscord.repos.DiscordUserRepository;
import com.example.DollarStoreDiscord.repos.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class MessageService {
    private final DiscordUserRepository discordUserRepository;
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;


    public MessageDTO sendMessage(Integer senderId, Integer channelId, String textMessage) {
        DiscordUser sender = discordUserRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found."));

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found."));

        Message message = new Message();
        message.setSender(sender);
        message.setChannel(channel);
        message.setTextMessage(textMessage);
        message.setTimestamp(LocalDateTime.now());

        Message savedMessage = messageRepository.save(message);

        return mapToMessageDTO(savedMessage);
    }


    public List<MessageDTO> getMessagesForChannel(Integer channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found."));

        List<Message> messages = messageRepository.findByChannelOrderByTimestampAsc(channel);

        return messages.stream()
                .map(this::mapToMessageDTO)
                .toList();
    }

    private MessageDTO mapToMessageDTO(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(message.getId());
        messageDTO.setTextMessage(message.getTextMessage());
        messageDTO.setTimestamp(message.getTimestamp());

        DiscordUserDTO senderDTO = new DiscordUserDTO();
        DiscordUser sender = message.getSender();
        senderDTO.setId(sender.getId());
        senderDTO.setUsername(sender.getUsername());
        senderDTO.setEmail(sender.getEmail());
        senderDTO.setIsDeleted(sender.getIsDeleted());
        messageDTO.setSender(senderDTO);

        ChannelDTO channelDTO = new ChannelDTO();
        Channel channel = message.getChannel();
        channelDTO.setId(channel.getId());
        channelDTO.setName(channel.getName());
        channelDTO.setIsDeleted(channel.getIsDeleted());
        channelDTO.setIsFriendChat(channel.getIsFriendChat());
        messageDTO.setChannel(channelDTO);

        return messageDTO;
    }
}
