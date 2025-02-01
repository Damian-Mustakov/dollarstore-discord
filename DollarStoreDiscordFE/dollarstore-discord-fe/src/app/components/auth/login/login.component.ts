import { Component } from '@angular/core';
import { UserService } from '../../../services/user.service';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(private userService: UserService, private router: Router) {}

  login(): void {

    if (!this.email || !this.password) {
      this.errorMessage = 'Please enter your email and password.';
      return;
    }

    this.userService.login(this.email, this.password).subscribe({
      next: (user) => {
        this.userService.saveUserToSession(user);
        this.router.navigate(['/channels']);
      },
      error: (err) => {
        console.error('Login error:', err);
        this.errorMessage = 'Invalid email or password.';
      },
    });
  }
}