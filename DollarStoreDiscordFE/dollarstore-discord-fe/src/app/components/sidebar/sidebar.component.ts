import { Component, Input, OnInit } from '@angular/core';
import { Channel, ChannelMember, DiscordUser, RoleType } from '../../interfaces/models';
import { ChannelService } from '../../services/channel.service';
import { UserService } from '../../services/user.service';
import { RouterModule, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FriendService } from '../../services/friend.service';
import { FormsModule } from '@angular/forms';
import { UserSearchResultComponent } from './components/user-search-result/user-search-result.component';
import { ChannelListItemComponent } from './components/channel-list-item/channel-list-item.component';

@Component({
  selector: 'app-sidebar',
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    UserSearchResultComponent,
    ChannelListItemComponent,
  ],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss',
})
export class SidebarComponent implements OnInit {
  user!: DiscordUser;
  newChannelName: string = '';
  searchQuery: string = '';
  searchPerformed: boolean = false;
  channels: Channel[] = [];
  friends: DiscordUser[] = [];
  searchResults: DiscordUser[] = [];
  channelMembersMap: { [channelId: number]: ChannelMember[] } = {};
  searchTimeout: any;

  constructor(
    private channelService: ChannelService,
    private userService: UserService,
    private friendService: FriendService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.retrieveUserFromSession();
    if (this.user) {
      this.loadChannels();
      this.loadFriends();
    }
  }

  loadChannels(): void {
    this.channelService.getChannelsForUser(this.user.id).subscribe({
      next: (data) => {
        if (data) {
          this.channels = data;
        }
        this.channels.forEach((channel) => this.loadChannelMembers(channel.id));
      },
      error: (err) => {
        console.error('Error fetching channels:', err);
      },
    });
  }

  loadFriends(): void {
    this.friendService.getFriends(this.user.id).subscribe({
      next: (data) => {
        if (data) {
          this.friends = data;
        }
      },
      error: (err) => {
        console.error('Error fetching friends:', err);
      },
    });
  }

  retrieveUserFromSession(): void {
    const storedUser = this.userService.getUserFromSession();
    if (storedUser) {
      this.user = storedUser;
    }
  }

  logout(): void {
    this.userService.clearStorage();
    this.router.navigate(['/login']);
  }

  selectChannel(channelId: number): void {
    this.router.navigate(['/channels', channelId]);
  }

  createChannel(): void {
    if (!this.newChannelName.trim()) return;

    this.channelService
      .createChannel(this.user.id, this.newChannelName)
      .subscribe({
        next: () => {
          this.loadChannels();
          this.newChannelName = '';
        },
        error: (err) => {
          console.error('Error creating channel:', err);
        },
      });
  }

  searchUsers(): void {
    if (!this.searchQuery.trim()) {
      this.searchPerformed = false;
      this.searchResults = []; 
      return;
    }
  
    clearTimeout(this.searchTimeout);
  
    this.searchTimeout = setTimeout(() => {
      this.searchPerformed = true;

      if (this.searchQuery.trim().length > 0) {
        this.userService.searchUsers(this.searchQuery, this.user.id).subscribe({
          next: (data) => {
            this.searchResults = data;
          },
          error: (err) => {
            console.error('Error searching users:', err);
            this.searchResults = [];
          },
        });
      }

    }, 1000);
  }
  

  addFriend(friendId: number): void {
    this.friendService.addFriend(this.user.id, friendId).subscribe({
      next: (_) => {
        this.loadFriends();
        this.searchResults = this.searchResults.filter(user => user.id !== friendId);
      },
      error: (err) => {
        console.error('Error creating channel:', err);
      },
    });
  }

  selectFriend(friendId: number) {
    this.friendService.addFriend(this.user.id, friendId).subscribe({
      next: (channel) => {
        this.router.navigate(['/channels', channel.id]);
      },
      error: (err) => {
        console.error('Error creating channel:', err);
      },
    });
  }

  openChannelSettings(channelId: number): void {
    this.router.navigate([`/channels/${channelId}/settings`]);
  }

  loadChannelMembers(channelId: number): void {
    this.channelService.getChannelMembers(channelId).subscribe({
      next: (members) => {
        this.channelMembersMap[channelId] = members;
      },
      error: (err) => {
        console.error(`Error fetching members for channel ${channelId}:`, err);
      },
    });
  }

  canUserManageChannel(channelId: number): boolean {
    const channelMembers = this.channelMembersMap[channelId];
    if (!channelMembers || !this.user) return false;
  
    return channelMembers.some(member =>
      member.user.id === this.user.id && (member.role === RoleType.OWNER || member.role === RoleType.ADMIN)
    );
  }
}
