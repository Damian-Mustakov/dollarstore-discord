import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Channel } from '../../../../interfaces/models';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-channel-list-item',
  imports: [CommonModule],
  templateUrl: './channel-list-item.component.html',
  styleUrl: './channel-list-item.component.scss'
})
export class ChannelListItemComponent {
  @Input() channel!: Channel;
  @Input() hasPermission!: boolean;
  @Output() selectChannel = new EventEmitter<number>();
  @Output() openSettings = new EventEmitter<number>();

  onSelectChannel(): void {
    this.selectChannel.emit(this.channel.id);
  }

  onOpenSettings(): void {
    this.openSettings.emit(this.channel.id);
  }
}
