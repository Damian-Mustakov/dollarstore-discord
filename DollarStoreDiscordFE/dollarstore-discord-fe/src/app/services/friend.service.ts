import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Channel, DiscordUser } from '../interfaces/models';

@Injectable({
  providedIn: 'root',
})
export class FriendService {
  private readonly baseUrl = 'http://localhost:8543/api/friends';

  constructor(private http: HttpClient) {}

  addFriend(userId: number, friendId: number): Observable<Channel> {
    return this.http.post<Channel>(`${this.baseUrl}/${userId}/add-friend`, null, {
      params: { friendId },
    });
  }

  getFriends(userId: number): Observable<DiscordUser[]> {
    return this.http.get<DiscordUser[]>(`${this.baseUrl}/${userId}/list`);
  }
}
