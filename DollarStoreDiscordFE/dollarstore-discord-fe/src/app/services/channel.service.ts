import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Channel, ChannelMember } from '../interfaces/models';

@Injectable({
  providedIn: 'root',
})
export class ChannelService {
  private readonly baseUrl = 'http://localhost:8543/api/channels';

  constructor(private http: HttpClient) {}


  createChannel(ownerId: number, channelName: string): Observable<Channel> {
    return this.http.post<Channel>(`${this.baseUrl}/create-channel`, null, { 
      params: { ownerId, channelName },
    });
  }

  deleteChannel(channelId: number, ownerId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${channelId}/delete`, {
      params: { userId: ownerId },
      responseType: 'text',
    });
  }

  addMember(channelId: number, executorId: number, newMemberId: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/${channelId}/add-member`, null, {
      params: { executorId, newMemberId },
      responseType: 'text',
    });
  }

  removeMember(channelId: number, executorId: number, memberId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${channelId}/remove-member`, {
      params: { executorId, memberId },
      responseType: 'text',
    });
  }

  promoteMember(channelId: number, executorId: number, memberId: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/${channelId}/promote-member`, null, {
      params: { executorId, memberId },
      responseType: 'text',
    });
  }

  getChannelMembers(channelId: number): Observable<ChannelMember[]> {
    return this.http.get<ChannelMember[]>(`${this.baseUrl}/${channelId}/members`);
  }

  renameChannel(channelId: number, ownerId: number, newName: string): Observable<any> {
    return this.http.put(`${this.baseUrl}/${channelId}/change-name`, null, {
      params: { ownerId, newName },
      responseType: 'text',
    });
  }

  getChannelsForUser(userId: number): Observable<Channel[]> {
    return this.http.get<Channel[]>(`${this.baseUrl}/user/${userId}`)
  }

  getChannelById(channelId: number): Observable<Channel> {
    return this.http.get<Channel>(`${this.baseUrl}/${channelId}`);
  }
}
