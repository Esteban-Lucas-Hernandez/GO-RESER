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
  private baseUrl = `${environment.apiUrl}/admin/profile`;

  constructor(private http: HttpClient, private authService: AuthService) {}

  private getAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken();

    let headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }

    return headers;
  }

  getProfile(): Observable<UsuarioDTO> {
    const headers = this.getAuthHeaders();
    return this.http.get<UsuarioDTO>(this.baseUrl, { headers }).pipe(catchError(this.handleError));
  }

  updateProfile(data: ActualizarPerfilDTO): Observable<UsuarioDTO> {
    const headers = this.getAuthHeaders();
    return this.http
      .put<UsuarioDTO>(this.baseUrl, data, { headers })
      .pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'Ocurrió un error desconocido';
    if (error.error instanceof ErrorEvent) {
      errorMessage = `Error: ${error.error.message}`;
    } else {
      errorMessage = `Código de error: ${error.status}\nMensaje: ${error.message}`;
    }
    return throwError(errorMessage);
  }
}