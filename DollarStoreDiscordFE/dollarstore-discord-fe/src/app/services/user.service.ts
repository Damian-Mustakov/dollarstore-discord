import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DiscordUser } from '../interfaces/models';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private readonly baseUrl = 'http://localhost:8543/api/users';

  constructor(private http: HttpClient, private router: Router) {}

  register(user: {
    email: string;
    username: string;
    password: string;
  }): Observable<DiscordUser> {
    return this.http.post<DiscordUser>(`${this.baseUrl}/register`, user);
  }

  login(email: string, password: string): Observable<DiscordUser> {
    return this.http.post<DiscordUser>(`${this.baseUrl}/login`, null, {
      params: { email, password },
    });
  }

  searchUsers(username: string, userId: number): Observable<DiscordUser[]> {
    return this.http.get<DiscordUser[]>(`${this.baseUrl}/search`, {
      params: { username, userId },
    });
  }

  saveUserToSession(user: DiscordUser): void {
    sessionStorage.setItem('discordUser', JSON.stringify(user));
  }

  getUserFromSession(): DiscordUser | null {
    const user = sessionStorage.getItem('discordUser');
    return user ? JSON.parse(user) : null;
  }

  clearStorage(): void {
    sessionStorage.removeItem('discordUser');
  }
}
