import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { AuthService } from '../../../auth/auth.service';
import { PerfilService } from '../../perfil/perfil.service';
import { UsuarioDTO } from '../../perfil/usuario.dto';
import { LoginComponent } from '../../../auth/login/ts/login.component';
import { RegistroComponent } from '../../../auth/registro/ts/registro.component';
import { DropdownMenuComponent } from '../../../public/dropdown-menu/ts/dropdown-menu.component';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-nav',
  standalone: true,
  imports: [CommonModule, LoginComponent, RegistroComponent, DropdownMenuComponent],
  templateUrl: '../html/nav.component.html',
  styleUrls: ['../css/nav.component.css'],
})
export class NavComponent implements OnInit {
closeMobileMenu() {
throw new Error('Method not implemented.');
}
  isAuthenticated = false;
  userInfo: any = null;
  showLoginModal = false;
  showRegistroModal = false;
  showMobileMenu = false;
  isSignUpActive = false; // Propiedad para controlar el estado del formulario
  private storageListener: any;

  constructor(
    private router: Router,
    private authService: AuthService,
    private perfilService: PerfilService
  ) {}

  ngOnInit(): void {
    // Verificar si el usuario está autenticado
    this.isAuthenticated = this.authService.isAuthenticated();

    // Si está autenticado, obtener la información del usuario del token
    if (this.isAuthenticated) {
      this.loadUserProfile();
    }

    // Escuchar cambios en el almacenamiento local para detectar inicio de sesión con Google
    this.storageListener = (event: StorageEvent) => {
      if (event.key === 'auth_token' && event.newValue) {
        // Se ha iniciado sesión, actualizar estado
        this.isAuthenticated = true;
        this.loadUserProfile();
      }
    };

    // Solo agregar el listener si estamos en el navegador
    if (typeof window !== 'undefined') {
      window.addEventListener('storage', this.storageListener);

      // Agregar listener para cambios en el tamaño de la ventana
      window.addEventListener('resize', this.onResize.bind(this));
    }

    // Escuchar eventos de navegación para actualizar la información del usuario
    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        // Verificar si el usuario está autenticado después de la navegación
        const currentlyAuthenticated = this.authService.isAuthenticated();
        if (currentlyAuthenticated && !this.isAuthenticated) {
          // El usuario acaba de iniciar sesión, cargar su información
          this.isAuthenticated = true;
          this.loadUserProfile();
        } else if (!currentlyAuthenticated && this.isAuthenticated) {
          // El usuario ha cerrado sesión, limpiar la información
          this.isAuthenticated = false;
          this.userInfo = null;
        }

        // Cerrar el menú móvil al cambiar de ruta
        this.showMobileMenu = false;
      });
  }

  onResize() {
    // Cerrar el menú móvil si la pantalla es más grande que 580px
    if (window.innerWidth > 580) {
      this.showMobileMenu = false;
    }
  }

  toggleMobileMenu() {
    this.showMobileMenu = !this.showMobileMenu;
  }

  // Método para alternar entre inicio de sesión y registro
  toggleSignUp() {
    this.isSignUpActive = true;
  }

  toggleSignIn() {
    this.isSignUpActive = false;
  }

  loadUserProfile(): void {
    this.perfilService.getProfile().subscribe({
      next: (data: UsuarioDTO) => {
        const token = this.authService.getToken();
        let username = 'Usuario';
        if (token) {
          const decodedToken = this.authService.decodeToken(token);
          username = decodedToken?.username || localStorage.getItem('userName') || 'Usuario';
        }

        const fotoUrlFromStorage = localStorage.getItem('userFotoUrl');

        this.userInfo = {
          username: username,
          ...data,
          fotoUrl:
            data.fotoUrl ||
            fotoUrlFromStorage ||
            'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png',
        };
      },
      error: (err) => {
        Swal.fire({
          position: 'top-end',
          icon: 'error',
          title: 'Error al cargar el perfil de usuario',
          showConfirmButton: false,
          timer: 3000,
        });
        const token = this.authService.getToken();
        let username = 'Usuario';
        if (token) {
          const decodedToken = this.authService.decodeToken(token);
          username = decodedToken?.username || localStorage.getItem('userName') || 'Usuario';
        }

        const fotoUrlFromStorage = localStorage.getItem('userFotoUrl');

        this.userInfo = {
          username: username,
          fotoUrl:
            fotoUrlFromStorage ||
            'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png',
        };
      },
    });
  }

  openLoginModal() {
    this.showLoginModal = true;
    this.showRegistroModal = false;
    this.isSignUpActive = false; // Resetear al abrir el modal
  }

  closeLoginModal() {
    this.showLoginModal = false;
  }

  openRegistroModal() {
    this.showRegistroModal = true;
    this.showLoginModal = false;
    this.isSignUpActive = true; // Activar el formulario de registro
  }

  closeRegistroModal() {
    this.showRegistroModal = false;
  }

  onLoginSuccess(event: any) {
    // Cerrar el modal
    this.closeLoginModal();

    // Actualizar el estado de autenticación
    this.isAuthenticated = true;

    // Cargar la información completa del perfil
    this.loadUserProfile();

    // Verificar el rol del usuario y redirigir según corresponda
    setTimeout(() => {
      const userRole = this.authService.getUserRole();
      if (userRole === 'ROLE_ADMIN') {
        // Redirigir a la página de administración
        this.router.navigate(['/admin/dashboard']);
      } else if (userRole === 'ROLE_SUPERADMIN') {
        // Redirigir a la página de superadministración
        this.router.navigate(['/superadmin/usuarios']);
      }
    }, 100);
  }

  onRegistroSuccess(event: any) {
    // Cambiar al formulario de inicio de sesión después del registro exitoso
    this.isSignUpActive = false;
    // Mostrar mensaje de éxito
    Swal.fire({
      position: 'top-end',
      icon: 'success',
      title: 'Registro exitoso. Ahora puedes iniciar sesión.',
      showConfirmButton: false,
      timer: 2000,
    });
  }

  // Método para desplazarse a la sección de búsqueda del hero
  scrollToHeroSearch(event: Event) {
    event.preventDefault();
    const heroSection = document.querySelector('.hero');
    if (heroSection) {
      // Calcular posición con offset para evitar que quede justo en el borde
      const yOffset = -80; // Ajustar para compensar el navbar fijo
      const y = heroSection.getBoundingClientRect().top + window.pageYOffset + yOffset;

      window.scrollTo({ top: y, behavior: 'smooth' });

      // Enfocar el input de búsqueda
      const searchInput = heroSection.querySelector('.search-input');
      if (searchInput) {
        setTimeout(() => {
          (searchInput as HTMLElement).focus();
        }, 800); // Ajustar tiempo para coincidir con la duración del scroll
      }
    }

    // Cerrar el menú móvil después de hacer clic
    this.showMobileMenu = false;
  }

  // Método para desplazarse al carrusel de hoteles
  scrollToHotels(event: Event) {
    event.preventDefault();
    const hotelsSection = document.querySelector('.hoteles-section');
    if (hotelsSection) {
      // Calcular posición con offset para evitar que quede justo en el borde
      const yOffset = -80; // Ajustar para compensar el navbar fijo
      const y = hotelsSection.getBoundingClientRect().top + window.pageYOffset + yOffset;

      window.scrollTo({ top: y, behavior: 'smooth' });
    }

    // Cerrar el menú móvil después de hacer clic
    this.showMobileMenu = false;
  }

  // Método para desplazarse a la sección de características/servicios
  scrollToFeatures(event: Event) {
    event.preventDefault();
    const featuresSection = document.querySelector('app-features-section');
    if (featuresSection) {
      const yOffset = -80;
      const y = featuresSection.getBoundingClientRect().top + window.pageYOffset + yOffset;
      window.scrollTo({ top: y, behavior: 'smooth' });
    }
    this.showMobileMenu = false;
  }

  // Método para desplazarse a la sección de información/nosotros
  scrollToAbout(event: Event) {
    event.preventDefault();
    const aboutSection = document.querySelector('app-about-section');
    if (aboutSection) {
      const yOffset = -80;
      const y = aboutSection.getBoundingClientRect().top + window.pageYOffset + yOffset;
      window.scrollTo({ top: y, behavior: 'smooth' });
    }
    this.showMobileMenu = false;
  }

  // Método para desplazarse al footer/contacto
  scrollToContact(event: Event) {
    event.preventDefault();
    const footer = document.querySelector('app-footer');
    if (footer) {
      const yOffset = -80;
      const y = footer.getBoundingClientRect().top + window.pageYOffset + yOffset;
      window.scrollTo({ top: y, behavior: 'smooth' });
    }
    this.showMobileMenu = false;
  }

  // Método para desplazarse a la sección de historia
  scrollToHistory(event: Event) {
    event.preventDefault();
    const historySection = document.querySelector('.story-text');
    if (historySection) {
      const yOffset = -80;
      const y = historySection.getBoundingClientRect().top + window.pageYOffset + yOffset;
      window.scrollTo({ top: y, behavior: 'smooth' });
    } else {
      // Si no se encuentra la historia, ir al about-section
      this.scrollToAbout(event);
    }
    this.showMobileMenu = false;
  }

  // Método para desplazarse a la sección de equipo
  scrollToTeam(event: Event) {
    event.preventDefault();
    const teamSection = document.querySelector('.team-section');
    if (teamSection) {
      const yOffset = -80;
      const y = teamSection.getBoundingClientRect().top + window.pageYOffset + yOffset;
      window.scrollTo({ top: y, behavior: 'smooth' });
    } else {
      // Si no se encuentra el equipo, ir al about-section
      this.scrollToAbout(event);
    }
    this.showMobileMenu = false;
  }

  // Método para desplazarse a la sección de testimonios/clientes
  scrollToTestimonials(event: Event) {
    event.preventDefault();
    const testimonialsSection = document.querySelector('app-testimonials-section');
    if (testimonialsSection) {
      const yOffset = -80;
      const y = testimonialsSection.getBoundingClientRect().top + window.pageYOffset + yOffset;
      window.scrollTo({ top: y, behavior: 'smooth' });
    }
    this.showMobileMenu = false;
  }

  // Método para desplazarse a la sección de newsletter/suscripción
  scrollToNewsletter(event: Event) {
    event.preventDefault();
    const newsletterSection = document.querySelector('app-newsletter-section');
    if (newsletterSection) {
      const yOffset = -80;
      const y = newsletterSection.getBoundingClientRect().top + window.pageYOffset + yOffset;
      window.scrollTo({ top: y, behavior: 'smooth' });
    }
    this.showMobileMenu = false;
  }
}