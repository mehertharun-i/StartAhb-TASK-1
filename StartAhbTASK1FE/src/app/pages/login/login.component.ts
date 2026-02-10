import { Component, signal, OnInit } from '@angular/core';
import { Router, RouterLink, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  form = new FormGroup({
    username: new FormControl('', [Validators.required, Validators.minLength(3)]),
    password: new FormControl('', [Validators.required, Validators.minLength(6)]),
  });

  loading = signal(false);
  error = signal<string | null>(null);
  success = signal<string | null>(null);
  showPassword = signal(false);

  constructor(
    private auth: AuthService, 
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    // Check for success message from signup redirect
    this.activatedRoute.queryParams.subscribe(params => {
      if (params['message']) {
        this.success.set(params['message']);
      }
    });
  }

  submit(): void {
    if (this.form.invalid) return;
    
    this.loading.set(true);
    this.error.set(null);

    const credentials = {
      username: this.form.value.username!,
      password: this.form.value.password!
    };

    this.auth.login(credentials).subscribe({
      next: () => {
        this.loading.set(false);
        this.router.navigate(['/home']);
      },
      error: (err) => {
        this.loading.set(false);
        const message = err?.error?.message || err?.error?.error || 'Login failed. Please try again.';
        this.error.set(message);
        console.error('Login error:', err);
      }
    });
  }

  togglePassword(): void {
    this.showPassword.set(!this.showPassword());
  }

  get usernameError(): string | null {
    const control = this.form.get('username');
    if (control?.hasError('required')) return 'Username is required';
    if (control?.hasError('minlength')) return 'Username must be at least 3 characters';
    return null;
  }

  get passwordError(): string | null {
    const control = this.form.get('password');
    if (control?.hasError('required')) return 'Password is required';
    if (control?.hasError('minlength')) return 'Password must be at least 6 characters';
    return null;
  }
}
