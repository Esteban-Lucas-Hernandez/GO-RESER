import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';

@Injectable({
  providedIn: 'root',
})
export class AdminGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean {
    console.log('🛡️ [ADMIN GUARD] Evaluando canActivate()...');

    if (this.authService.isAuthenticated()) {
      const userRole = this.authService.getUserRole();
      console.log('🛡️ [ADMIN GUARD] Usuario autenticado. Rol detectado:', userRole);

      if (userRole === 'ROLE_ADMIN') {
        console.log('✅ [ADMIN GUARD] Acceso permitido a ruta de administrador.');
        return true;
      } else if (userRole === 'ROLE_SUPERADMIN') {
        console.warn('⚠️ [ADMIN GUARD] Rol es ROLE_SUPERADMIN. Redirigiendo a /superadmin...');
        this.router.navigate(['/superadmin']).then((success) => {
          console.log('🧭 [ADMIN GUARD] Redirección a /superadmin resultado:', success);
        });
        return false;
      } else {
        console.error('⛔ [ADMIN GUARD] Acceso DENEGADO. Rol del usuario no es admin:', userRole, 'Redirigiendo a /public...');
        this.router.navigate(['/public']).then((success) => {
          console.log('🧭 [ADMIN GUARD] Redirección a /public resultado:', success);
        });
        return false;
      }
    } else {
      console.error('⛔ [ADMIN GUARD] Usuario no autenticado (no hay token válido). Redirigiendo a /login...');
      this.router.navigate(['/login']).then((success) => {
        console.log('🧭 [ADMIN GUARD] Redirección a /login resultado:', success);
      });
      return false;
    }
  }
}
