import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Message } from '../../interfaces/models';
import { MessageService } from '../../services/message.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-chat',
  imports: [CommonModule, FormsModule],
  templateUrl: './chat.component.html',
  styleUrl: './chat.component.scss',
})
export class ChatComponent implements OnInit {
  messages: Message[] = [];
  channelId!: number;
  currentUserId!: number;
  newMessage: string = '';

  constructor(
    private route: ActivatedRoute,
    private messageService: MessageService,
    private userService: UserService,
  ) {}

  ngOnInit(): void {
    this.retrieveCurrentUser();
    this.route.params.subscribe((params) => {
      this.channelId = +params['id'];
      this.loadMessages();
    });
  }

  retrieveCurrentUser(): void {
    const user = this.userService.getUserFromSession();
    if (user) {
      this.currentUserId = user.id;
    }
  }

  loadMessages(): void {
    if (!this.channelId) return;

    this.messageService.getMessages(this.channelId).subscribe({
      next: (data) => {
        this.messages = data;
        console.log(this.messages);
      },
      error: (err) => {
        console.error('Error fetching messages:', err);
      },
    });
  }

  isCurrentUserMessage(message: Message): boolean {
    return message.sender.id === this.currentUserId;
  }

  sendMessage(): void {
    console.log('send message', this.newMessage);
    if (!this.newMessage.trim() || !this.currentUserId || !this.channelId)
      return;
    this.messageService
      .sendMessage(this.currentUserId, this.channelId, this.newMessage)
      .subscribe({
        next: () => {
          this.newMessage = '';
          this.loadMessages();
        },
        error: (err) => {
          console.error('Error sending message:', err);
        },
      });
  }
}
