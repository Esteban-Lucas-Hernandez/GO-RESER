import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';

@Injectable({
  providedIn: 'root',
})
export class SuperAdminGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean {
    console.log('🛡️ [SUPERADMIN GUARD] Evaluando canActivate()...');

    if (this.authService.isAuthenticated()) {
      const userRole = this.authService.getUserRole();
      console.log('🛡️ [SUPERADMIN GUARD] Usuario autenticado. Rol detectado:', userRole);

      if (userRole === 'ROLE_SUPERADMIN') {
        console.log('✅ [SUPERADMIN GUARD] Acceso permitido a ruta de SuperAdmin.');
        return true;
      } else {
        console.error('⛔ [SUPERADMIN GUARD] Acceso DENEGADO. Rol no es ROLE_SUPERADMIN:', userRole, 'Redirigiendo a /public...');
        this.router.navigate(['/public']).then((success) => {
          console.log('🧭 [SUPERADMIN GUARD] Redirección a /public resultado:', success);
        });
        return false;
      }
    } else {
      console.error('⛔ [SUPERADMIN GUARD] Usuario no autenticado (sin token). Redirigiendo a /login...');
      this.router.navigate(['/login']).then((success) => {
        console.log('🧭 [SUPERADMIN GUARD] Redirección a /login resultado:', success);
      });
      return false;
    }
  }
}
