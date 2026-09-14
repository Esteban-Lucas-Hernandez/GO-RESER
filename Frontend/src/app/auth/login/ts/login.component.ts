import { Component, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService, LoginData, AuthResponse } from '../../auth.service'; // Importar el servicio y los modelos
import { environment } from '../../../../environments/environment';
import Swal from 'sweetalert2';



@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: '../html/login.component.html',
  styleUrls: ['../css/login.component.css'],
})
export class LoginComponent {
  loginForm: FormGroup;
  @Output() loginSuccess = new EventEmitter<AuthResponse>();
  @Output() registerRequested = new EventEmitter<void>();

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    // Inyectar el servicio y el router
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      const formData: LoginData = this.loginForm.value;

      this.authService.login(formData).subscribe({
        next: (response: AuthResponse) => {
          if (response && response.token && response.success !== false) {
            this.loginSuccess.emit(response);

            const userRole = this.authService.getUserRole();
            if (userRole === 'ROLE_SUPERADMIN') {
              this.router.navigate(['/superadmin']);
            } else if (userRole === 'ROLE_ADMIN') {
              this.router.navigate(['/admin/panel']);
            } else {
              this.router.navigate(['/public']);
            }
          } else {
            const errorMessage =
              response?.message || 'Credenciales incorrectas. Por favor, inténtelo de nuevo.';
            Swal.fire({
              position: 'top-end',
              icon: 'error',
              title: errorMessage,
              showConfirmButton: false,
              timer: 3000,
            });
          }
        },
        error: (error: any) => {
          const errorMessage =
            error?.error?.message ||
            'Error en el servidor. Por favor, inténtelo de nuevo más tarde.';
          Swal.fire({
            position: 'top-end',
            icon: 'error',
            title: errorMessage,
            showConfirmButton: false,
            timer: 3000,
          });
        },
      });
    } else {
      Object.keys(this.loginForm.controls).forEach((key) => {
        const control = this.loginForm.get(key);
        control?.markAsTouched();
      });
    }
  }

  goToRegistro() {
    if (this.registerRequested.observers.length > 0) {
      this.registerRequested.emit();
      return;
    }

    this.router.navigate(['/registro']);
  }

  // Método para iniciar sesión con Google
  loginWithGoogle() {
    // Redirigir al endpoint de autenticación de Google
    window.location.href = `${environment.apiUrl}/oauth2/authorization/google`;
  }
}