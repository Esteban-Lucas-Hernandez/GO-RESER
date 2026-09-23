import { Component, OnInit, AfterViewInit, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { HotelService, Habitacion, Hotel, Resena } from '../../hotel.service';
import { AuthService } from '../../../../core/auth/auth.service';
import { FormsModule } from '@angular/forms';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import Swal from 'sweetalert2';

// Importaciones para Leaflet
import * as L from 'leaflet';

import { defaultIcon } from '../../../../core/config/leaflet.config';

// Importar el componente de navegación
import { NavComponent } from '../../../../shared/components/nav/nav.component';
import { Nav1Component } from '../../../../shared/components/nav-alt/nav-alt.component';
// Importar el componente de footer
import { FooterComponent } from '../../../../shared/components/footer/footer.component';

@Component({
  selector: 'app-habitaciones',
  standalone: true,
  imports: [CommonModule, FormsModule, Nav1Component, FooterComponent],
  templateUrl: './rooms.component.html',
  styleUrls: ['./rooms.component.css'],
})
export class HabitacionesComponent implements OnInit, AfterViewInit {
  habitaciones: Habitacion[] = [];
  habitacionesPaginadas: Habitacion[] = [];
  resenas: Resena[] = [];
  resenasPaginadas: Resena[] = []; // Nueva propiedad para las reseñas paginadas
  loading = false;
  error: string | null = null;
  hotelId: number | null = null;
  hotel: Partial<Hotel> = {};
  currentUserId: number | null = null;

  // Variables para paginación de habitaciones
  currentPage = 1;
  itemsPerPage = 3; // Cambiado de 6 a 3 habitaciones por página
  itemsPerPageMobile = 4; // 4 habitaciones por página en móviles
  totalPages = 0;

  // Variables para paginación de reseñas
  currentResenaPage = 1;
  resenasPerPage = 4; // Cambiar de 2 a 4 reseñas por página
  totalResenasPages = 0;

  // Variables para el modal de edición
  showEditModal = false;
  selectedResena: Resena | null = null;
  editComentario: string = '';
  editCalificacion: number = 5;

  // Variables para el modal de creación
  showCreateModal = false;
  newComentario: string = '';
  newCalificacion: number = 5;

  // Variables para el mapa
  private map: any;
  private marker: any;

  // Variables para el filtro de búsqueda
  searchTerm: string = '';
  showSuggestions: boolean = false;
  filteredHabitaciones: Habitacion[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private hotelService: HotelService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    // Obtener el ID del usuario logueado
    const token = this.authService.getToken();
    if (token) {
      const decodedToken = this.authService.decodeToken(token);
      if (decodedToken && decodedToken.userId) {
        this.currentUserId = decodedToken.userId;
      }
    }

    this.route.params.subscribe((params) => {
      this.hotelId = +params['id'];
      if (this.hotelId) {
        this.loadHabitaciones(this.hotelId);
        this.loadResenas(this.hotelId);
      }
    });

    // Forzar el scroll al inicio de la página al cargar
    if (typeof window !== 'undefined') {
      window.scrollTo(0, 0);
    }

    // Establecer el número de habitaciones por página según el tamaño de la pantalla
    this.setItemsPerPage();
  }

  ngAfterViewInit(): void {
    // Inicializar el mapa después de que la vista se haya cargado
    this.initMap();
  }

  // Método para actualizar las habitaciones paginadas
  updatePaginatedRooms(): void {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    this.habitacionesPaginadas = this.habitaciones.slice(startIndex, endIndex);
  }

  // Método para establecer el número de habitaciones por página según el tamaño de la pantalla
  setItemsPerPage(): void {
    if (typeof window !== 'undefined') {
      const screenWidth = window.innerWidth;
      // En pantallas menores a 1024px, mostrar 4 habitaciones por página
      if (screenWidth < 1024) {
        this.itemsPerPage = this.itemsPerPageMobile;
      } else {
        // En pantallas mayores o iguales a 1024px, mantener 3 habitaciones por página
        this.itemsPerPage = 3;
      }

      // Recalcular la paginación
      this.totalPages = Math.ceil(this.habitaciones.length / this.itemsPerPage);
      this.updatePaginatedRooms();
    }
  }

  // Escuchar cambios en el tamaño de la ventana
  @HostListener('window:resize', ['$event'])
  onResize(event: any): void {
    this.setItemsPerPage();
  }

  // Método para actualizar las reseñas paginadas
  updatePaginatedResenas(): void {
    const startIndex = (this.currentResenaPage - 1) * this.resenasPerPage;
    const endIndex = startIndex + this.resenasPerPage;
    this.resenasPaginadas = this.resenas.slice(startIndex, endIndex);
  }

  // Método para cambiar de página de habitaciones
  changePage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
      this.updatePaginatedRooms();
    }
  }

  // Método para cambiar de página de reseñas
  changeResenaPage(page: number): void {
    if (page >= 1 && page <= this.totalResenasPages) {
      this.currentResenaPage = page;
      this.updatePaginatedResenas();
    }
  }

  // Método para ir a la página anterior de habitaciones
  previousPage(): void {
    if (this.currentPage > 1) {
      this.changePage(this.currentPage - 1);
    }
  }

  // Método para ir a la página siguiente de habitaciones
  nextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.changePage(this.currentPage + 1);
    }
  }

  // Método para ir a la página anterior de reseñas
  previousResenaPage(): void {
    if (this.currentResenaPage > 1) {
      this.changeResenaPage(this.currentResenaPage - 1);
    }
  }

  // Método para ir a la página siguiente de reseñas
  nextResenaPage(): void {
    if (this.currentResenaPage < this.totalResenasPages) {
      this.changeResenaPage(this.currentResenaPage + 1);
    }
  }

  loadHabitaciones(hotelId: number): void {
    this.loading = true;
    this.error = null;

    if (!hotelId || hotelId <= 0) {
      this.error = 'ID de hotel inválido';
      this.loading = false;
      return;
    }

    forkJoin({
      habitaciones: this.hotelService.getHabitacionesByHotelId(hotelId).pipe(
        catchError((err) => {
          console.warn('Error al cargar habitaciones', err);
          return of([] as Habitacion[]);
        })
      ),
      hotel: this.hotelService.getHotelById(hotelId).pipe(
        catchError((err) => {
          console.warn('Error al cargar hotel', err);
          return of(null as Hotel | null);
        })
      ),
    }).subscribe({
      next: ({ habitaciones, hotel }: any) => {
        this.habitaciones = habitaciones;
        this.filteredHabitaciones = [...habitaciones];

        if (hotel) {
          this.hotel = hotel;
        } else if (habitaciones.length > 0) {
          const primeraHabitacion = habitaciones[0];
          this.hotel = {
            id: primeraHabitacion.idHotel,
            nombre: primeraHabitacion.hotelNombre,
            email: primeraHabitacion.email,
            descripcion: primeraHabitacion.descripcionHotel,
            checkIn: primeraHabitacion.checkIn,
            checkOut: primeraHabitacion.checkOut,
            createdAt: primeraHabitacion.createdAt,
            updatedAt: primeraHabitacion.updatedAt,
            imagenUrl: primeraHabitacion.hotelImagenUrl,
            estrellas: primeraHabitacion.estrellas,
            politicaCancelacion: primeraHabitacion.politicaCancelacion,
            ciudad: {
              id: 0,
              nombre: primeraHabitacion.ciudadNombre,
              departamento: {
                id: 0,
                nombre: primeraHabitacion.departamentoNombre,
              },
            },
          };
        }

        this.setItemsPerPage();
        this.updatePaginatedRooms();

        // Ocultar pantalla de carga
        this.loading = false;

        // Inicializar o posicionar mapa una vez que el DOM está listo
        setTimeout(() => {
          this.updateMapPosition();
        }, 150);
      },
      error: (err: any) => {
        if (err.status === 404) {
          this.error = 'No se encontraron datos para este hotel.';
        } else {
          this.error = 'No se pudo conectar con el servidor para cargar las habitaciones. Verifique su conexión.';
        }
        this.loading = false;
      },
    });
  }

  loadResenas(hotelId: number): void {
    this.hotelService.getResenasByHotelId(hotelId).subscribe({
      next: (data: Resena[]) => {
        this.resenas = data;

        this.totalResenasPages = Math.ceil(this.resenas.length / this.resenasPerPage);
        this.updatePaginatedResenas();
      },
      error: (err: any) => {
        Swal.fire({
          position: 'top-end',
          icon: 'error',
          title: 'Error al cargar reseñas',
          showConfirmButton: false,
          timer: 3000,
        });
      },
    });
  }

  // Verificar si una reseña pertenece al usuario actual
  isUserReview(resena: Resena): boolean {
    return this.currentUserId !== null && resena.idUsuario === this.currentUserId;
  }

  // Abrir modal de creación
  openCreateModal(): void {
    this.newComentario = '';
    this.newCalificacion = 5;
    this.showCreateModal = true;
  }

  // Cerrar modal de creación
  closeCreateModal(): void {
    this.showCreateModal = false;
  }

  // Guardar nueva reseña
  saveNewResena(): void {
    if (!this.newComentario.trim()) {
      Swal.fire({
        position: 'top-end',
        icon: 'error',
        title: 'Por favor ingrese un comentario',
        showConfirmButton: false,
        timer: 3000,
      });
      return;
    }

    if (this.newCalificacion < 1 || this.newCalificacion > 5) {
      Swal.fire({
        position: 'top-end',
        icon: 'error',
        title: 'La calificación debe estar entre 1 y 5',
        showConfirmButton: false,
        timer: 3000,
      });
      return;
    }

    if (this.hotelId) {
      const newResena = {
        comentario: this.newComentario,
        calificacion: this.newCalificacion,
      };

      this.hotelService.createResena(this.hotelId!, newResena).subscribe({
        next: (response: Resena) => {
          this.resenas.push(response);

          this.totalResenasPages = Math.ceil(this.resenas.length / this.resenasPerPage);
          this.updatePaginatedResenas();

          this.closeCreateModal();

          if (this.hotelId) {
            this.loadResenas(this.hotelId);
          }

          Swal.fire({
            position: 'top-end',
            icon: 'success',
            title: 'Reseña creada correctamente',
            showConfirmButton: false,
            timer: 2000,
          });
        },
        error: (err: any) => {
          let errorMessage = 'Error al crear la reseña';
          if (err.status === 0 || err.status === 401 || err.status === 403) {
            errorMessage = 'Para dejar una reseña primero reserve una habitacion de este hotel';
          } else if (err.error && err.error.message) {
            errorMessage = err.error.message;
          } else if (err.message) {
            errorMessage = err.message;
          }
          Swal.fire({
            position: 'top-end',
            icon: 'error',
            title: errorMessage,
            showConfirmButton: false,
            timer: 3000,
          });
        },
      });
    }
  }

  // Abrir modal de edición
  editarResena(resena: Resena): void {
    this.selectedResena = resena;
    this.editComentario = resena.comentario;
    // Asegurarse de que la calificación esté en el rango válido
    this.editCalificacion =
      resena.calificacion >= 1 && resena.calificacion <= 5 ? resena.calificacion : 5;
    this.showEditModal = true;
  }

  // Cerrar modal de edición
  closeEditModal(): void {
    this.showEditModal = false;
    this.selectedResena = null;
  }

  // Guardar cambios en la reseña
  saveResenaChanges(): void {
    if (!this.editComentario.trim()) {
      Swal.fire({
        position: 'top-end',
        icon: 'error',
        title: 'Por favor ingrese un comentario',
        showConfirmButton: false,
        timer: 3000,
      });
      return;
    }

    if (this.editCalificacion < 1 || this.editCalificacion > 5) {
      Swal.fire({
        position: 'top-end',
        icon: 'error',
        title: 'La calificación debe estar entre 1 y 5',
        showConfirmButton: false,
        timer: 3000,
      });
      return;
    }

    if (this.selectedResena) {
      const updatedResena = {
        comentario: this.editComentario,
        calificacion: this.editCalificacion,
      };

      this.hotelService.updateResena(this.selectedResena.idResena, updatedResena).subscribe({
        next: (response: any) => {
          const index = this.resenas.findIndex((r) => r.idResena === this.selectedResena!.idResena);
          if (index !== -1) {
            this.resenas[index] = { ...this.resenas[index], ...updatedResena };

            const paginatedIndex = this.resenasPaginadas.findIndex(
              (r) => r.idResena === this.selectedResena!.idResena
            );
            if (paginatedIndex !== -1) {
              this.resenasPaginadas[paginatedIndex] = {
                ...this.resenasPaginadas[paginatedIndex],
                ...updatedResena,
              };
            }
          }

          this.closeEditModal();

          if (this.hotelId) {
            this.loadResenas(this.hotelId);
          }

          Swal.fire({
            position: 'top-end',
            icon: 'success',
            title: 'Reseña actualizada correctamente',
            showConfirmButton: false,
            timer: 2000,
          });
        },
        error: (err: any) => {
          Swal.fire({
            position: 'top-end',
            icon: 'error',
            title: 'Error al actualizar la reseña',
            showConfirmButton: false,
            timer: 3000,
          });
        },
      });
    }
  }

  // Eliminar una reseña
  deleteResena(idResena: number): void {
    Swal.fire({
      title: '¿Estás seguro?',
      text: 'No podrás revertir esto',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Sí, eliminar',
    }).then((result) => {
      if (result.isConfirmed) {
        this.hotelService.deleteResena(idResena).subscribe({
          next: () => {
            this.resenas = this.resenas.filter((resena) => resena.idResena !== idResena);

            this.totalResenasPages = Math.ceil(this.resenas.length / this.resenasPerPage);
            this.updatePaginatedResenas();

            if (this.hotelId) {
              this.loadResenas(this.hotelId);
            }

            Swal.fire({
              position: 'top-end',
              icon: 'success',
              title: 'Reseña eliminada correctamente',
              showConfirmButton: false,
              timer: 2000,
            });
          },
          error: (err: any) => {
            Swal.fire({
              position: 'top-end',
              icon: 'error',
              title: 'Error al eliminar la reseña',
              showConfirmButton: false,
              timer: 3000,
            });
          },
        });
      }
    });
  }

  volverAlListado(): void {
    this.router.navigate(['/public']);
  }

  verDetalles(habitacionId: number): void {
    if (this.hotelId) {
      this.router.navigate(['/detalle-habitacion', this.hotelId, habitacionId]);
    }
  }

  // Función auxiliar para obtener una calificación válida (entre 0 y 5)
  getValidCalificacion(calificacion: number | undefined | null): number {
    if (calificacion === undefined || calificacion === null || isNaN(calificacion)) {
      return 0;
    }
    return Math.max(0, Math.min(5, Math.floor(calificacion)));
  }

  // Función auxiliar para obtener el número de estrellas vacías
  getEmptyStars(calificacion: number | undefined | null): number {
    const validCalificacion = this.getValidCalificacion(calificacion);
    return Math.max(0, 5 - validCalificacion);
  }

  // Inicializar el mapa
  private initMap(): void {
    const mapElement = document.getElementById('map');
    if (!mapElement) {
      return;
    }
    if (this.map) {
      this.map.remove();
      this.map = null;
    }
    // Crear el mapa centrado en una posición por defecto
    this.map = L.map('map').setView([4.5709, -74.2973], 6); // Coordenadas por defecto para Colombia

    // Agregar capa de mapa base
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap contributors',
    }).addTo(this.map);

    // Usar el ícono configurado
    this.marker = L.marker([4.5709, -74.2973], { icon: defaultIcon }).addTo(this.map);
  }

  // Actualizar la posición del mapa
  private updateMapPosition(): void {
    if (!this.map) {
      this.initMap();
    }
    if (this.map && this.marker) {
      let lat = 4.5709;
      let lng = -74.2973;

      if (this.hotel.latitud && this.hotel.longitud) {
        lat = this.hotel.latitud;
        lng = this.hotel.longitud;
      } else if (this.habitaciones.length > 0 && this.habitaciones[0].latitud && this.habitaciones[0].longitud) {
        lat = this.habitaciones[0].latitud;
        lng = this.habitaciones[0].longitud;
      } else {
        lat = 4.711 + (Math.random() - 0.5) * 0.1;
        lng = -74.0721 + (Math.random() - 0.5) * 0.1;
      }

      this.map.setView([lat, lng], 15);
      this.marker.setLatLng([lat, lng]);

      setTimeout(() => {
        if (this.map) {
          this.map.invalidateSize();
        }
      }, 200);
    }
  }

  // Métodos para el filtro de búsqueda
  onSearchInput(): void {
    if (this.searchTerm.trim() === '') {
      // Si el término de búsqueda está vacío, mostrar todas las habitaciones
      this.filteredHabitaciones = [...this.habitaciones];
      this.showSuggestions = false;
      // Actualizar la paginación para mostrar todas las habitaciones
      this.totalPages = Math.ceil(this.habitaciones.length / this.itemsPerPage);
      this.currentPage = 1;
      this.updatePaginatedRooms();
    } else {
      // Filtrar las habitaciones basadas en el término de búsqueda
      this.filteredHabitaciones = this.habitaciones.filter(
        (habitacion) =>
          habitacion.numero.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
          habitacion.categoria.nombre.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
          habitacion.descripcion.toLowerCase().includes(this.searchTerm.toLowerCase())
      );

      this.showSuggestions = this.filteredHabitaciones.length > 0;
    }
  }

  onSearchBlur(): void {
    // Pequeño retraso para permitir que se ejecute el mousedown en las sugerencias
    setTimeout(() => {
      this.showSuggestions = false;
    }, 200);
  }

  selectHabitacion(habitacion: Habitacion): void {
    // Seleccionar una habitación específica y mostrar solo esa en la lista
    this.habitacionesPaginadas = [habitacion];
    this.searchTerm = `Habitación ${habitacion.numero}`;
    this.showSuggestions = false;

    // Actualizar la paginación
    this.totalPages = 1;
    this.currentPage = 1;
  }

  // Método para limpiar el filtro y mostrar todas las habitaciones
  clearFilter(): void {
    this.searchTerm = '';
    this.filteredHabitaciones = [...this.habitaciones];
    this.showSuggestions = false;

    // Actualizar la paginación
    this.totalPages = Math.ceil(this.habitaciones.length / this.itemsPerPage);
    this.currentPage = 1;
    this.updatePaginatedRooms();
  }

  // Control de estado de carga de imágenes
  imagenesCargadas: { [url: string]: boolean } = {};

  onImagenCargada(url: string | null | undefined): void {
    if (url) {
      this.imagenesCargadas[url] = true;
    }
  }

  esImagenCargada(url: string | null | undefined): boolean {
    return url ? !!this.imagenesCargadas[url] : false;
  }

  onImagenError(url: string | null | undefined): void {
    if (url) {
      this.imagenesCargadas[url] = true;
    }
  }
}