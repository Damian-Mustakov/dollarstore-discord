import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { DiscordUser, ChannelMember, Channel, RoleType } from '../../interfaces/models';
import { ChannelService } from '../../services/channel.service';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-channel-settings',
  imports: [CommonModule, FormsModule],
  templateUrl: './channel-settings.component.html',
  styleUrl: './channel-settings.component.scss',
})
export class ChannelSettingsComponent implements OnInit {
  channelId!: number;
  currentUser!: DiscordUser | null;
  channel!: Channel | null;
  channelMembers: ChannelMember[] = [];
  newChannelName: string = '';
  userSearchQuery: string = '';
  userSearchResults: DiscordUser[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private channelService: ChannelService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.currentUser = this.userService.getUserFromSession();

    this.route.params.subscribe((params) => {
      this.channelId = +params['id'];
      this.loadChannel();
      this.loadChannelMembers();
    });
    if (!this.currentUser) {
      console.error('No user found in session');
      return;
    }
  }

  loadChannel(): void {
    this.channelService.getChannelById(this.channelId).subscribe({
      next: (data) => {
        this.channel = data;
      },
      error: (err) => {
        console.error('Error fetching channel:', err);
      },
    });
  }

  loadChannelMembers(): void {
    this.channelService.getChannelMembers(this.channelId).subscribe({
      next: (data) => {
        this.channelMembers = data;
      },
      error: (err) => {
        console.error('Error fetching channel members:', err);
      },
    });
  }

  renameChannel(): void {
    if (!this.currentUser || !this.channel || !this.newChannelName.trim())
      return;

    this.channelService
      .renameChannel(this.channelId, this.currentUser.id, this.newChannelName)
      .subscribe({
        next: () => {
          alert('Channel renamed successfully');
          if (this.channel) {
            this.channel.name = this.newChannelName;
          }
          this.newChannelName = '';
        },
        error: (err) => {
          alert('Failed to rename channel.');
          console.error('Error renaming channel:', err);
        },
      });
  }

  isUserOwner(): boolean {
    return this.channel?.owner.id === this.currentUser?.id;
  }

  confirmDeleteChannel(): void {
    if (
      confirm(
        'Are you sure you want to delete this channel? This action cannot be undone.'
      )
    ) {
      this.deleteChannel();
    }
  }

  promoteUserToAdmin(userId: number): void {
    if (!this.currentUser || !this.channel) return;

    if (!confirm(`Are you sure you want to promote this user to Admin?`)) {
      return;
    }

    this.channelService
      .promoteMember(this.channelId, this.currentUser.id, userId)
      .subscribe({
        next: () => {
          alert('User promoted to Admin successfully!');
          this.loadChannelMembers();
        },
        error: (err) => {
          alert('Failed to promote user.');
          console.error('Error promoting user:', err);
        },
      });
  }

  removeUserFromChannel(userId: number): void {
    if (!this.currentUser || !this.channel) return;

    if (
      !confirm(`Are you sure you want to remove this user from the channel?`)
    ) {
      return;
    }

    this.channelService
      .removeMember(this.channelId, this.currentUser.id, userId)
      .subscribe({
        next: () => {
          alert('User removed successfully!');
          this.loadChannelMembers();
        },
        error: (err) => {
          alert('Failed to remove user.');
          console.error('Error removing user:', err);
        },
      });
  }

  addUserToChannel(userId: number): void {
    if (!this.currentUser) return;

    this.channelService
      .addMember(this.channelId, this.currentUser.id, userId)
      .subscribe({
        next: () => {
          this.loadChannelMembers();
          this.userSearchQuery = '';
          this.userSearchResults = [];
        },
        error: (err) => {
          alert('Failed to add user.');
          console.error('Error adding user:', err);
        },
      });
  }

  searchUsers(): void {
    if (!this.userSearchQuery.trim()) {
      this.userSearchResults = [];
      return;
    }

    this.userService
      .searchUsers(this.userSearchQuery, this.currentUser!.id)
      .subscribe({
        next: (users) => {
          this.userSearchResults = users;
        },
        error: (err) => {
          console.error('Error searching users:', err);
        },
      });
  }

  deleteChannel(): void {
    if (!this.currentUser || !this.channel) return;

    this.channelService
      .deleteChannel(this.channelId, this.currentUser.id)
      .subscribe({
        next: () => {
          alert('Channel deleted successfully');
          this.router.navigate(['/channels']);
        },
        error: (err) => {
          alert('Failed to delete channel.');
          console.error('Error deleting channel:', err);
        },
      });
  }

  canManageUsers(membership: ChannelMember): boolean {
    if(membership.role === RoleType.OWNER || membership.role === RoleType.ADMIN) return false;

    return this.currentUser?.id === this.channel?.owner.id;
  }
}
