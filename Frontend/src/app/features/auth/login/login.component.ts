import { Component, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService, LoginData, AuthResponse } from '../../../core/auth/auth.service'; // Importar el servicio y los modelos
import { environment } from '../../../../environments/environment';
import Swal from 'sweetalert2';



@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
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
    console.log('📝 [LOGIN COMPONENT] onSubmit ejecutado. Formulario válido:', this.loginForm.valid);

    if (this.loginForm.valid) {
      const formData: LoginData = this.loginForm.value;
      console.log('📤 [LOGIN COMPONENT] Enviando credenciales para email:', formData.email);

      this.authService.login(formData).subscribe({
        next: (response: AuthResponse) => {
          console.log('📥 [LOGIN COMPONENT] Respuesta recibida en LoginComponent:', response);

          if (response && response.token && response.success !== false) {
            console.log('📢 [LOGIN COMPONENT] Emitiendo evento loginSuccess...');
            this.loginSuccess.emit(response);

            const userRole = this.authService.getUserRole();
            console.log('👤 [LOGIN COMPONENT] Rol del usuario obtenido:', userRole);

            let targetRoute = '/public';
            if (userRole === 'ROLE_SUPERADMIN') {
              targetRoute = '/superadmin/usuarios';
            } else if (userRole === 'ROLE_ADMIN') {
              targetRoute = '/admin/panel';
            }

            console.log(`🚀 [LOGIN COMPONENT] Iniciando redirección a: ${targetRoute}`);
            this.router.navigate([targetRoute]).then((success) => {
              if (success) {
                console.log(`✅ [LOGIN COMPONENT] Redirección exitosa a ${targetRoute}`);
              } else {
                console.error(`❌ [LOGIN COMPONENT] Redirección a ${targetRoute} fue CANCELADA o RECHAZADA (posible Guard o condición de ruta).`);
              }
            }).catch((err) => {
              console.error(`💥 [LOGIN COMPONENT] Error en navegación hacia ${targetRoute}:`, err);
            });
          } else {
            const errorMessage =
              response?.message || 'Credenciales incorrectas. Por favor, inténtelo de nuevo.';
            console.warn('⚠️ [LOGIN COMPONENT] Login no exitoso:', errorMessage, response);
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
          console.error('💥 [LOGIN COMPONENT] Error capturado en subscripción de login:', error);
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
      console.warn('⚠️ [LOGIN COMPONENT] Intento de submit con formulario inválido:', this.loginForm.value);
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