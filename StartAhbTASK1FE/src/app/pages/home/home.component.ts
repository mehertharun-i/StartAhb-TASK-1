import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  currentUser: string | null = null;
  isLoggedIn: boolean = false;

  constructor(public auth: AuthService, private router: Router) {
    this.currentUser = auth.currentUser$.getValue();
    this.isLoggedIn = auth.isLoggedIn();
  }

  ngOnInit(): void {
    // Subscribe to user changes
    this.auth.currentUser$.subscribe(user => {
      this.currentUser = user;
    });

    this.auth.isLoggedIn$.subscribe(loggedIn => {
      this.isLoggedIn = loggedIn;
    });
  }

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
