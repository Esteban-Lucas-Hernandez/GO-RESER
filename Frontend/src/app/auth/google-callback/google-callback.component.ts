import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../auth.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-google-callback',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="callback-container">
      <h2>Procesando inicio de sesión con Google...</h2>
      <p>Por favor, espere mientras completamos la autenticación.</p>
      <div class="spinner" *ngIf="loading"></div>
    </div>
  `,
  styleUrls: ['./google-callback.component.css'],
})
export class GoogleCallbackComponent implements OnInit {
  loading = true;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      const token = params['token'];
      const userId = params['userId'];
      const email = params['email'];
      const fullName = params['fullName'];
      const fotoUrl = params['fotoUrl'];
      const success = params['success'] === 'true';

      if (success && token) {
        this.authService.saveToken(token);
        localStorage.setItem('userId', userId);
        localStorage.setItem('userEmail', email);
        localStorage.setItem('userName', fullName);
        if (fotoUrl) {
          localStorage.setItem('userFotoUrl', fotoUrl);
        }

        setTimeout(() => {
          const userRole = this.authService.getUserRole();
          if (userRole === 'ROLE_ADMIN') {
            this.router.navigate(['/admin/dashboard']);
          } else if (userRole === 'ROLE_SUPERADMIN') {
            this.router.navigate(['/superadmin/usuarios']);
          } else {
            this.router.navigate(['/public']);
          }
        }, 100);
      } else {
        Swal.fire({
          position: 'top-end',
          icon: 'error',
          title: 'La autenticación con Google falló. Por favor, inténtelo de nuevo.',
          showConfirmButton: false,
          timer: 3000,
        });
        this.router.navigate(['/login']);
      }
    });
  }
}