import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule, DatePipe, SlicePipe, NgStyle } from '@angular/common';
import { AuthService } from '../../../core/auth/auth.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';
import { Router } from '@angular/router';
import { ResenaService } from '../reviews/review.service';
import { Resena } from '../reviews/review.interface';
import { IngresosComponent } from './revenue/revenue.component';
import { PerfilService } from '../profile/profile.service';
import { UsuarioDTO } from '../../../shared/models/user.model';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-panel',
  standalone: true,
  imports: [CommonModule, SlicePipe, IngresosComponent],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
  providers: [DatePipe],
})
export class PanelComponent implements OnInit, OnDestroy {
  panelData: any = null;
  userInfo: any = null;
  ultimasResenas: Resena[] = [];
  categorias: any[] = [];
  isMobileView: boolean = false;
  private resizeSubscription!: Subscription;

  constructor(
    private authService: AuthService,
    private http: HttpClient,
    private router: Router,
    private resenaService: ResenaService,
    private perfilService: PerfilService
  ) {}

  ngOnInit(): void {
    this.loadUserInfoFromToken();
    this.loadPanelData();
    this.loadUltimasResenas();
    this.loadCategorias();
    this.loadUserProfile();
    this.checkScreenSize();
    this.resizeSubscription = this.onResize().subscribe(() => {
      this.checkScreenSize();
    });
  }

  ngOnDestroy(): void {
    if (this.resizeSubscription) {
      this.resizeSubscription.unsubscribe();
    }
  }

  private onResize(): Observable<Event> {
    return new Observable((observer) => {
      const handler = (event: Event) => observer.next(event);
      window.addEventListener('resize', handler);
      return () => window.removeEventListener('resize', handler);
    });
  }

  private checkScreenSize(): void {
    this.isMobileView = window.innerWidth < 560;
  }

  loadUserInfoFromToken(): void {
    const token = this.authService.getToken();
    if (token) {
      const decodedToken = this.authService.decodeToken(token);
      if (decodedToken) {
        this.userInfo = {
          username: decodedToken.username,
          email: decodedToken.sub,
          userId: decodedToken.userId,
          roles: decodedToken.roles,
        };
      }
    }
  }

  loadUserProfile(): void {
    this.perfilService.getProfile().subscribe({
      next: (profile: UsuarioDTO) => {
        this.userInfo = { ...this.userInfo, ...profile };
      },
      error: (error: any) => {
        console.error('Error loading user profile:', error);
      },
    });
  }

  loadPanelData(): void {
    const token = this.authService.getToken();
    if (token) {
      const headers = new HttpHeaders({
        Authorization: `Bearer ${token}`,
      });

      this.http.get(`${environment.apiUrl}/admin/dashboard`, { headers }).subscribe({
        next: (data: any) => {
          this.panelData = data;
        },
        error: (error: any) => {
          console.error('Error loading panel data:', error);
        },
      });
    }
  }

  loadUltimasResenas(): void {
    this.resenaService.getResenas().subscribe({
      next: (resenas: Resena[]) => {
        this.ultimasResenas = resenas
          .sort((a, b) => new Date(b.fechaResena).getTime() - new Date(a.fechaResena).getTime())
          .slice(0, 5);
      },
      error: (error: any) => {
        console.error('Error cargando últimas reseñas:', error);
      },
    });
  }

  loadCategorias(): void {
    const token = this.authService.getToken();
    if (token) {
      const headers = new HttpHeaders({
        Authorization: `Bearer ${token}`,
      });

      this.http.get<any[]>(`${environment.apiUrl}/admin/categoria`, { headers }).subscribe({
        next: (data: any[]) => {
          this.categorias = data;
        },
        error: (error: any) => {
          console.error('Error cargando categorías:', error);
        },
      });
    }
  }

  getCategoriaColor(index: number): string {
    const colores = [
      '#E3F2FD',
      '#B3E5FC',
      '#81D4FA',
      '#4FC3F7',
      '#29B6F6',
      '#039BE5',
      '#1E88E5',
      '#1565C0',
      '#0D47A1',
      '#263238',
    ];
    return colores[index % colores.length];
  }

  getBarColor(calificacion: number): string {
    if (calificacion === 5) {
      return 'linear-gradient(to top, #001f3f, #004c99)';
    } else if (calificacion === 4) {
      return 'linear-gradient(to top, #004c99, #0080ff)';
    } else if (calificacion === 3) {
      return 'linear-gradient(to top, #0080ff, #33ccff)';
    } else if (calificacion === 2) {
      return 'linear-gradient(to top, #33ccff, #66d9ff)';
    } else {
      return 'linear-gradient(to top, #66d9ff,rgb(139, 218, 245))';
    }
  }

  getPieSliceStyle(index: number, total: number): any {
    const rotate = index * (360 / total);
    return {
      transform: `rotate(${rotate}deg)`,
      'clip-path': 'polygon(50% 50%, 50% 0%, 100% 0%, 100% 100%, 50% 100%)',
    };
  }

  getPieSliceClip(index: number, total: number): string {
    if (total <= 1) {
      return 'polygon(0% 0%, 100% 0%, 100% 100%, 0% 100%)';
    }

    const sliceAngle = 360 / total;
    const startAngle = index * sliceAngle;
    const endAngle = (index + 1) * sliceAngle;

    const startRad = ((startAngle - 90) * Math.PI) / 180;
    const endRad = ((endAngle - 90) * Math.PI) / 180;

    const startX = 50 + 50 * Math.cos(startRad);
    const startY = 50 + 50 * Math.sin(startRad);
    const endX = 50 + 50 * Math.cos(endRad);
    const endY = 50 + 50 * Math.sin(endRad);

    if (sliceAngle > 180) {
      return `polygon(50% 50%, ${startX.toFixed(2)}% ${startY.toFixed(
        2
      )}%, 0% 0%, 0% 100%, 100% 100%, 100% 0%, ${endX.toFixed(2)}% ${endY.toFixed(2)}%)`;
    } else {
      return `polygon(50% 50%, ${startX.toFixed(2)}% ${startY.toFixed(2)}%, ${endX.toFixed(
        2
      )}% ${endY.toFixed(2)}%)`;
    }
  }

  verTodasLasResenas(): void {
    this.router.navigate(['/admin/resenas']);
  }

  verTodasLasCategorias(): void {
    this.router.navigate(['/admin/categoria/listar']);
  }

  logout(): void {
    this.authService.removeToken();
    this.router.navigate(['/public']);
  }
}