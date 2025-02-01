import { Component, EventEmitter, Input, Output } from '@angular/core';
import { DiscordUser } from '../../../../interfaces/models';

@Component({
  selector: 'app-user-search-result',
  imports: [],
  templateUrl: './user-search-result.component.html',
  styleUrl: './user-search-result.component.scss'
})
export class UserSearchResultComponent {
  @Input() user!: DiscordUser;
  @Output() addFriend = new EventEmitter<number>();

  onAddFriend(): void {
    this.addFriend.emit(this.user.id);
  }
}
