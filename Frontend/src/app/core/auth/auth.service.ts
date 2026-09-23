import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import Swal from 'sweetalert2';

export interface RegistroData {
  nombreCompleto: string;
  email: string;
  telefono: string;
  documento: string;
  contrasena: string;
  fotoUrl?: string;
}

export interface LoginData {
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  success?: boolean;
  message?: string;
  userId?: any;
  username?: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private baseUrl = `${environment.apiUrl}/auth`;
  private readonly TOKEN_KEY = 'auth_token';

  constructor(private http: HttpClient) {}

  registrar(data: RegistroData): Observable<any> {
    const url = `${this.baseUrl}/registro`;
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.post(url, data, { headers });
  }

  login(data: LoginData): Observable<AuthResponse> {
    const url = `${this.baseUrl}/login`;
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    console.log('📡 [AUTH SERVICE] Enviando POST a /auth/login con:', { email: data.email });

    return this.http.post<AuthResponse>(url, data, { headers }).pipe(
      tap({
        next: (response) => {
          console.log('✅ [AUTH SERVICE] Respuesta recibida de /auth/login:', response);
          if (response && response.token) {
            this.saveToken(response.token);
            console.log('💾 [AUTH SERVICE] Token guardado en localStorage exitosamente.');
          } else {
            console.warn('⚠️ [AUTH SERVICE] La respuesta no contenía token:', response);
          }
        },
        error: (error) => {
          console.error('❌ [AUTH SERVICE] Error HTTP en /auth/login:', error);
        },
      })
    );
  }

  saveToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  removeToken(): void {
    localStorage.removeItem(this.TOKEN_KEY);
  }

  isAuthenticated(): boolean {
    const token = this.getToken();
    const authenticated = !!token;
    console.log('🔍 [AUTH SERVICE] isAuthenticated():', authenticated, 'Token presente:', !!token);
    return authenticated;
  }

  // Función para decodificar el token JWT de manera segura (manejando Base64Url y padding)
  decodeToken(token: string): any {
    try {
      if (!token) {
        console.warn('⚠️ [AUTH SERVICE decodeToken] Token nulo o vacío.');
        return null;
      }

      const parts = token.split('.');
      if (parts.length !== 3) {
        console.error('❌ [AUTH SERVICE decodeToken] El token no tiene 3 partes JWT válidas:', parts.length);
        return null;
      }

      // Normalizar Base64Url a Base64 estándar
      let base64 = parts[1].replace(/-/g, '+').replace(/_/g, '/');
      while (base64.length % 4 !== 0) {
        base64 += '=';
      }

      // Decodificar Base64 a JSON compatible con UTF-8
      const jsonPayload = decodeURIComponent(
        atob(base64)
          .split('')
          .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
          .join('')
      );

      const decoded = JSON.parse(jsonPayload);
      console.log('🔓 [AUTH SERVICE decodeToken] Token decodificado correctamente:', decoded);
      return decoded;
    } catch (error) {
      console.error('❌ [AUTH SERVICE decodeToken] Error al decodificar token JWT:', error, 'Token:', token);
      Swal.fire({
        position: 'top-end',
        icon: 'error',
        title: 'Error al decodificar el token',
        showConfirmButton: false,
        timer: 3000,
      });
      return null;
    }
  }

  // Función para obtener el rol del usuario desde el token
  getUserRole(): string | null {
    const token = this.getToken();
    if (!token) {
      console.warn('⚠️ [AUTH SERVICE getUserRole] No hay token almacenado.');
      return null;
    }

    const decoded = this.decodeToken(token);
    if (decoded && decoded.roles && Array.isArray(decoded.roles) && decoded.roles.length > 0) {
      const role = decoded.roles[0];
      console.log('👑 [AUTH SERVICE getUserRole] Rol obtenido del token:', role, 'Lista de roles:', decoded.roles);
      return role;
    }

    console.error('❌ [AUTH SERVICE getUserRole] No se encontraron roles en el payload del token:', decoded);
    return null;
  }
}