import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../../services/user.service';
import { Router, RouterModule } from '@angular/router';
import { DiscordUser } from '../../../interfaces/models';

@Component({
  selector: 'app-register',
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
})
export class RegisterComponent {
  username: string = '';
  email: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(private userService: UserService, private router: Router) {}

  register(event: Event): void {
    event.preventDefault();

    if (!this.username || !this.email || !this.password) {
      this.errorMessage = 'All fields are required.';
      return;
    }

    const user = {
      email: this.email,
      username: this.username,
      password: this.password,
    };
    this.userService.register(user).subscribe({
      next: (user: DiscordUser) => {
        console.log('Registration successful:', user);
        this.userService.saveUserToSession(user); // Запазване в сесия
        this.router.navigate(['/channels']); // Пренасочване
      },
      error: (err) => {
        console.error('Registration error:', err);
        this.errorMessage = 'Registration failed. Please try again.';
      },
    });
  }
}
