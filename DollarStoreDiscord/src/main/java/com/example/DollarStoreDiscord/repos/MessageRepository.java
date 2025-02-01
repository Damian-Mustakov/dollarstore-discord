package com.example.DollarStoreDiscord.repos;

import com.example.DollarStoreDiscord.models.Channel;
import com.example.DollarStoreDiscord.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findByChannelOrderByTimestampAsc(Channel channel);
}
