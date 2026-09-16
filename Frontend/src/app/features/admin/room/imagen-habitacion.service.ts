import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../../../core/auth/auth.service';
import { ImagenHabitacionDTO } from './habitacion.interface';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ImagenHabitacionService {
  private baseUrl = `${environment.apiUrl}/admin/hoteles`;

  constructor(private http: HttpClient, private authService: AuthService) {}

  getImagenesByHabitacionId(
    hotelId: number,
    habitacionId: number
  ): Observable<ImagenHabitacionDTO[]> {
    const headers = this.getAuthHeaders();
    return this.http.get<ImagenHabitacionDTO[]>(
      `${this.baseUrl}/${hotelId}/habitaciones/${habitacionId}/imagenes`,
      { headers }
    );
  }

  createImagen(
    hotelId: number,
    habitacionId: number,
    imagenDTO: ImagenHabitacionDTO
  ): Observable<ImagenHabitacionDTO> {
    const headers = this.getAuthHeaders();
    return this.http.post<ImagenHabitacionDTO>(
      `${this.baseUrl}/${hotelId}/habitaciones/${habitacionId}/imagenes`,
      imagenDTO,
      { headers }
    );
  }

  deleteImagen(hotelId: number, habitacionId: number, imagenId: number): Observable<void> {
    const headers = this.getAuthHeaders();
    return this.http.delete<void>(
      `${this.baseUrl}/${hotelId}/habitaciones/${habitacionId}/imagenes/${imagenId}`,
      { headers }
    );
  }

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
}
