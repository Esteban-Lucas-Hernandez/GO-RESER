import { HttpInterceptorFn, HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from './auth.service';
import { tap } from 'rxjs/operators';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();
  console.log(`🌐 [HTTP INTERCEPTOR] Enviando ${req.method} ${req.url} (Token presente: ${!!token})`);

  let authReq = req;
  if (token) {
    authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });
  }

  return next(authReq).pipe(
    tap({
      next: (event) => {
        if (event instanceof HttpResponse) {
          console.log(`📥 [HTTP INTERCEPTOR] Respuesta HTTP ${event.status} de ${req.method} ${req.url}`);
        }
      },
      error: (error) => {
        console.error(`💥 [HTTP INTERCEPTOR] Error HTTP ${error.status || 'desconocido'} en ${req.method} ${req.url}:`, error);
      },
    })
  );
};
