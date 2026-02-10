import { Component, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { FormGroup, FormControl, Validators, ReactiveFormsModule, AbstractControl } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-signup',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css'
})
export class SignupComponent {
  form = new FormGroup({
    firstName: new FormControl('', [Validators.required, Validators.minLength(2)]),
    lastName: new FormControl('', [Validators.required, Validators.minLength(2)]),
    email: new FormControl('', [Validators.required, Validators.email]),
    phoneNumber: new FormControl('', [Validators.required, Validators.pattern(/^[0-9]{10,}$/)]),
    dateOfBirth: new FormControl('', [Validators.required]),
    username: new FormControl('', [Validators.required, Validators.minLength(3)]),
    password: new FormControl('', [Validators.required, Validators.minLength(6)]),
    confirmPassword: new FormControl('', [Validators.required]),
    // Address fields (optional)
    houseNumber: new FormControl(''),
    streetName: new FormControl(''),
    areaName: new FormControl(''),
    landMarkName: new FormControl(''),
    districtName: new FormControl(''),
    stateName: new FormControl(''),
    countryName: new FormControl(''),
    pinCode: new FormControl('', [Validators.pattern(/^[0-9]*$/)]) // Optional but if provided must be numeric
  }, { validators: this.passwordMatchValidator });

  loading = signal(false);
  error = signal<string | null>(null);
  showPassword = signal(false);
  showConfirmPassword = signal(false);

  constructor(private auth: AuthService, private router: Router) {}

  private passwordMatchValidator(control: AbstractControl): {[key: string]: boolean} | null {
    const group = control as FormGroup;
    const password = group.get('password');
    const confirmPassword = group.get('confirmPassword');
    return password && confirmPassword && password.value === confirmPassword.value
      ? null
      : { passwordMismatch: true };
  }

  submit(): void {
    if (this.form.invalid) return;

    this.loading.set(true);
    this.error.set(null);

    // Build address object only if any address field is provided
    const addressData: any = {
      userHouseNumber: this.form.value.houseNumber || '',
      userStreetName: this.form.value.streetName || '',
      userAreaName: this.form.value.areaName || '',
      userLandMarkName: this.form.value.landMarkName || '',
      userDistrictName: this.form.value.districtName || '',
      userStateName: this.form.value.stateName || '',
      userCountryName: this.form.value.countryName || '',
      userPinCodeNumber: this.form.value.pinCode ? parseInt(this.form.value.pinCode) : 0
    };

    // Check if any address field has value
    const hasAddress = Object.values(addressData).some(val => val !== '' && val !== 0);

    const signupData = {
      userFirstName: this.form.value.firstName,
      userLastName: this.form.value.lastName,
      userEmail: this.form.value.email,
      userPhoneNumber: this.form.value.phoneNumber,
      userDateOfBirth: this.form.value.dateOfBirth,
      userLoginId: this.form.value.username,
      userPassword: this.form.value.password,
      addressClass: hasAddress ? [addressData] : [] // Include address if provided, otherwise empty array
    };

    this.auth.signup(signupData).subscribe({
      next: () => {
        this.loading.set(false);
        // Redirect to login page after successful signup
        this.router.navigate(['/login'], { 
          queryParams: { 
            message: 'Signup successful! Please login with your username and password.' 
          } 
        });
      },
      error: (err) => {
        this.loading.set(false);
        const message = err?.error?.message || err?.error?.error || 'Signup failed. Please try again.';
        this.error.set(message);
        console.error('Signup error:', err);
      }
    });
  }

  togglePassword(): void {
    this.showPassword.set(!this.showPassword());
  }

  toggleConfirmPassword(): void {
    this.showConfirmPassword.set(!this.showConfirmPassword());
  }

  get firstNameError(): string | null {
    const control = this.form.get('firstName');
    if (control?.hasError('required')) return 'First name is required';
    if (control?.hasError('minlength')) return 'First name must be at least 2 characters';
    return null;
  }

  get lastNameError(): string | null {
    const control = this.form.get('lastName');
    if (control?.hasError('required')) return 'Last name is required';
    if (control?.hasError('minlength')) return 'Last name must be at least 2 characters';
    return null;
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

  get confirmPasswordError(): string | null {
    const control = this.form.get('confirmPassword');
    if (control?.hasError('required')) return 'Please confirm your password';
    if (this.form.hasError('passwordMismatch')) return 'Passwords do not match';
    return null;
  }

  get emailError(): string | null {
    const control = this.form.get('email');
    if (control?.hasError('required')) return 'Email is required';
    if (control?.hasError('email')) return 'Please enter a valid email address';
    return null;
  }

  get phoneNumberError(): string | null {
    const control = this.form.get('phoneNumber');
    if (control?.hasError('required')) return 'Phone number is required';
    if (control?.hasError('pattern')) return 'Phone number must be at least 10 digits';
    return null;
  }

  get dateOfBirthError(): string | null {
    const control = this.form.get('dateOfBirth');
    if (control?.hasError('required')) return 'Date of birth is required';
    return null;
  }

  get pinCodeError(): string | null {
    const control = this.form.get('pinCode');
    if (control?.hasError('pattern')) return 'Pin code must contain only numbers';
    return null;
  }
}
