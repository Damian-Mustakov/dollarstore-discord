import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Message } from '../interfaces/models';

@Injectable({
  providedIn: 'root',
})
export class MessageService {
  private readonly baseUrl = 'http://localhost:8543/api/messages';

  constructor(private http: HttpClient) {}

  sendMessage(senderId: number, channelId: number, textMessage: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/send`, null, {
      params: { senderId, channelId, textMessage },
    });
  }

  getMessages(channelId: number): Observable<Message[]> {
    return this.http.get<Message[]>(`${this.baseUrl}/${channelId}`);
  }
}
