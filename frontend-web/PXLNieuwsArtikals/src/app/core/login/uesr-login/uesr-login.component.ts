import {Component, inject} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatCard, MatCardContent, MatCardHeader, MatCardTitle} from "@angular/material/card";
import {MatGridTile} from "@angular/material/grid-list";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatButton} from "@angular/material/button";
import {UserService} from "../../../shared/services/user.service";
import {CookieServicing} from "../../../shared/services/cookie.service";

@Component({
  selector: 'app-uesr-login',
  imports: [
    ReactiveFormsModule,
    MatCard,
    MatCardTitle,
    MatFormField,
    MatCardHeader,
    MatCardContent,
    MatLabel,
    MatInput,
    MatButton
  ],
  templateUrl: './uesr-login.component.html',
  standalone: true,
  styleUrl: './uesr-login.component.css'
})
export class UesrLoginComponent {
  loginForm: FormGroup;
  loginService: UserService = inject(UserService);

  successMessage: string | null = null;
  errorMessage: string | null = null;

  constructor(private fb: FormBuilder) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    });
  }
  onSubmit() {
    this.successMessage = null;
    this.errorMessage = null;

    if (this.loginForm.valid) {
      const { username, password } = this.loginForm.value;

      this.loginService.findUserByUsernameAndPassword(username, password).subscribe({
        next: (response) => {
          if (response != null) {
            CookieServicing.setCookie(response);
            this.successMessage = `Welcome, ${response.name}!`;
          } else {
            this.errorMessage = 'Invalid username or password.';
          }
        },
        error: (err) => {
          this.errorMessage = 'An error occurred during login. Please try again.';
          console.error('Login error:', err);
        },
      });
    } else {
      this.errorMessage = 'Form is invalid. Please fill out all fields correctly.';
    }
  }
}
