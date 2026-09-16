import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../core/auth/auth.service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import {
  faHome,
  faBuilding,
  faTags,
  faBed,
  faCalendarAlt,
  faStar,
  faUser,
  faSignOutAlt,
  faArrowLeft,
} from '@fortawesome/free-solid-svg-icons';
import { NavbarStateService } from '../../services/navbar-state.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule, FontAwesomeModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent {
  @Input() role: 'admin' | 'superadmin' = 'admin';
  isCollapsed = false;

  faSignOutAlt = faSignOutAlt;
  faArrowLeft = faArrowLeft;

  constructor(
    private router: Router,
    private authService: AuthService,
    private navbarStateService: NavbarStateService
  ) {}

  get menuItems() {
    if (this.role === 'superadmin') {
      return [
        { name: 'Usuarios', route: '/superadmin/usuarios', icon: faUser },
        { name: 'Hoteles', route: '/superadmin/hoteles', icon: faBuilding },
        { name: 'Habitaciones', route: '/superadmin/habitaciones', icon: faBed },
        { name: 'Reservas', route: '/superadmin/reservas', icon: faCalendarAlt },
        { name: 'Perfil', route: '/superadmin/perfil', icon: faUser },
      ];
    }
    return [
      { name: 'Panel', route: '/admin/panel', icon: faHome },
      { name: 'Hoteles', route: '/admin/hoteles', icon: faBuilding },
      { name: 'Categorías', route: '/admin/categoria/listar', icon: faTags },
      { name: 'Habitaciones', route: '/admin/habitacion/listar/1', icon: faBed },
      { name: 'Reservas', route: '/admin/reservas', icon: faCalendarAlt },
      { name: 'Reseñas', route: '/admin/resenas', icon: faStar },
      { name: 'Perfil', route: '/admin/perfil', icon: faUser },
    ];
  }

  toggleCollapse(): void {
    this.isCollapsed = !this.isCollapsed;
    this.navbarStateService.setCollapsed(this.isCollapsed);
  }

  logout(): void {
    this.authService.removeToken();
    this.router.navigate(['/auth/login']);
  }
}
