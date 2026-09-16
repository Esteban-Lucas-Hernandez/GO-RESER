import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from '../../../core/auth/auth.service';
import { UsuarioDTO } from '../../../shared/models/user.model';
import { ActualizarPerfilDTO } from '../../../shared/models/update-profile.dto';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class PerfilService {
  constructor(private http: HttpClient, private authService: AuthService) {}
  
  private getProfileUrl(): string {
    const role = this.authService.getUserRole();
    if (role === 'ROLE_SUPERADMIN') {
      return `${environment.apiUrl}/superadmin/profile`;
    } else if (role === 'ROLE_ADMIN') {
      return `${environment.apiUrl}/admin/profile`;
    } else {
      return `${environment.apiUrl}/user/profile`;
    }
  }

  // Método privado para obtener headers de autenticación
  private getAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    console.log('PerfilService: Obteniendo token de autenticación');

    let headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    if (token) {
      console.log('PerfilService: Token encontrado:', token.substring(0, 20) + '...');
      headers = headers.set('Authorization', `Bearer ${token}`);

      // Decodificar el token para verificar su contenido
      try {
        const decodedToken = this.authService.decodeToken(token);
        console.log('PerfilService: Token decodificado:', decodedToken);
        if (decodedToken && decodedToken.roles) {
          console.log('PerfilService: Roles en el token:', decodedToken.roles);
        }
      } catch (e) {
        console.error('PerfilService: Error al decodificar el token:', e);
      }
    } else {
      console.warn('PerfilService: No se encontró token de autenticación');
    }

    // Mostrar los headers que se van a enviar
    console.log('PerfilService: Headers a enviar:', {
      'Content-Type': headers.get('Content-Type'),
      Authorization: headers.get('Authorization'),
    });

    return headers;
  }

  // Obtener información del usuario actual
  getProfile(): Observable<UsuarioDTO> {
    const url = this.getProfileUrl();
    const headers = this.getAuthHeaders();
    console.log('PerfilService: Realizando solicitud GET a', url);

    return this.http.get<UsuarioDTO>(url, { headers }).pipe(catchError(this.handleError));
  }

  // Actualizar información del usuario
  updateProfile(data: ActualizarPerfilDTO): Observable<UsuarioDTO> {
    const url = this.getProfileUrl();
    const headers = this.getAuthHeaders();
    console.log('PerfilService: Realizando solicitud PUT a', url, 'con datos:', data);

    return this.http
      .put<UsuarioDTO>(url, data, { headers })
      .pipe(catchError(this.handleError));
  }

  // Manejo de errores
  private handleError(error: HttpErrorResponse) {
    console.error('PerfilService: Error en la solicitud HTTP:', error);

    // Devolver un observable con un mensaje de error amigable
    let errorMessage = 'Ocurrió un error desconocido';
    if (error.error instanceof ErrorEvent) {
      // Error del lado del cliente
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Error del servidor
      errorMessage = `Código de error: ${error.status}\nMensaje: ${error.message}`;
    }

    return throwError(errorMessage);
  }
}