CREATE TABLE IF NOT EXISTS discord_users (
                               id INT AUTO_INCREMENT PRIMARY KEY,
                               username VARCHAR(50) NOT NULL UNIQUE,
                               password VARCHAR(100) NOT NULL,
                               email VARCHAR(100) NOT NULL UNIQUE,
                               is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS channels (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(100) NOT NULL UNIQUE,
                          owner_id INT NOT NULL,
                          is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                          is_friend_chat BOOLEAN NOT NULL DEFAULT FALSE,
                          CONSTRAINT fk_channel_owner FOREIGN KEY (owner_id) REFERENCES discord_users (id)
);


CREATE TABLE IF NOT EXISTS channel_members (
                                 id INT AUTO_INCREMENT PRIMARY KEY,
                                 channel_id INT NOT NULL,
                                 user_id INT NOT NULL,
                                 role VARCHAR(20) NOT NULL,
                                 CONSTRAINT fk_channel_member_channel FOREIGN KEY (channel_id) REFERENCES channels (id),
                                 CONSTRAINT fk_channel_member_user FOREIGN KEY (user_id) REFERENCES discord_users (id)
);

CREATE TABLE IF NOT EXISTS messages (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          text_message VARCHAR(1000) NOT NULL,
                          timestamp TIMESTAMP NOT NULL,
                          sender_id INT NOT NULL,
                          channel_id INT NOT NULL,
                          CONSTRAINT fk_message_sender FOREIGN KEY (sender_id) REFERENCES discord_users (id),
                          CONSTRAINT fk_message_channel FOREIGN KEY (channel_id) REFERENCES channels (id)
);

CREATE TABLE IF NOT EXISTS friend_chats (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              sender_id INT NOT NULL,
                              receiver_id INT NOT NULL,
                              channel_id INT,
                              CONSTRAINT fk_friend_chat_sender FOREIGN KEY (sender_id) REFERENCES discord_users (id),
                              CONSTRAINT fk_friend_chat_receiver FOREIGN KEY (receiver_id) REFERENCES discord_users (id),
                              CONSTRAINT fk_friend_chat_channel FOREIGN KEY (channel_id) REFERENCES channels (id)
);
