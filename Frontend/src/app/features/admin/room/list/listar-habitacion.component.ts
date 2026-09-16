import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute, NavigationEnd } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HabitacionService } from '../room.service';
import { HotelAdminService, HotelDTO } from '../../hotels/hotel.service';
import { HabitacionDTO, CrearHabitacionDTO } from '../habitacion.interface';
import { CategoriaService, CategoriaHabitacionDTO } from '../../category/category.service';
import { filter } from 'rxjs/operators';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-listar-habitacion',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './listar-habitacion.component.html',
  styleUrls: ['./listar-habitacion.component.css'],
})
export class ListarHabitacionComponent implements OnInit {
  titulo = 'Gestión de Habitaciones';
  descripcion = 'Si quiere editar o eliminar un hotel por favor seleccione primero en el filtro';
  habitaciones: HabitacionDTO[] = [];
  hoteles: HotelDTO[] = [];
  categorias: CategoriaHabitacionDTO[] = [];
  loading = false;
  error: string | null = null;
  errorModal: string | null = null;
  hotelId: number | null = 0;

  mostrarModalCrear = false;
  nuevaHabitacion: CrearHabitacionDTO = {
    numero: '',
    capacidad: 0,
    precio: 0,
    descripcion: '',
    estado: 'disponible',
    categoriaId: 0,
    imagenUrl: '',
    imagenesUrls: [],
  };

  mostrarModalEditar = false;
  habitacionEditada: HabitacionDTO = {
    idHabitacion: undefined,
    idHotel: undefined,
    categoria: {
      id: undefined,
      nombre: '',
      descripcion: '',
      usuarioId: undefined,
    },
    numero: '',
    capacidad: 0,
    precio: 0,
    descripcion: '',
    estado: 'disponible',
    imagenUrl: '',
    imagenesUrls: [],
  };
  habitacionEnEdicion: HabitacionDTO | null = null;

  mostrandoModalConfirmacion = false;
  mensajeModal = '';
  habitacionAEliminar: { id: number; numero: string } | null = null;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private habitacionService: HabitacionService,
    private hotelService: HotelAdminService,
    private categoriaService: CategoriaService
  ) {}

  ngOnInit(): void {
    this.loadHoteles();
    this.loadCategorias();
    this.loadTodasLasHabitaciones();

    this.route.params.subscribe((params) => {
      if (params['hotelId']) {
        this.hotelId = +params['hotelId'];
        this.loadHabitaciones(this.hotelId);
      }
    });

    this.router.events.pipe(filter((event) => event instanceof NavigationEnd)).subscribe(() => {
      if (this.router.url === '/admin/habitacion/listar') {
        this.hotelId = 0;
        this.loadTodasLasHabitaciones();
      }
    });
  }

  loadHoteles(): void {
    this.hotelService.getHoteles().subscribe({
      next: (data: HotelDTO[]) => {
        this.hoteles = data;
      },
      error: (err: any) => {
        Swal.fire({
          position: 'top-end',
          icon: 'error',
          title: 'No se pudieron cargar los hoteles',
          showConfirmButton: false,
          timer: 3000,
        });
      },
    });
  }

  loadCategorias(): void {
    this.categoriaService.getCategorias().subscribe({
      next: (data: CategoriaHabitacionDTO[]) => {
        this.categorias = data;
      },
      error: (err: any) => {
        Swal.fire({
          position: 'top-end',
          icon: 'error',
          title: 'No se pudieron cargar las categorías',
          showConfirmButton: false,
          timer: 3000,
        });
      },
    });
  }

  loadTodasLasHabitaciones(): void {
    this.loading = true;
    this.error = null;

    this.habitacionService.getTodasLasHabitaciones().subscribe({
      next: (data: HabitacionDTO[]) => {
        this.habitaciones = data;
        this.loading = false;
      },
      error: (err: any) => {
        this.loading = false;
        Swal.fire({
          position: 'top-end',
          icon: 'error',
          title: 'No se pudieron cargar las habitaciones',
          showConfirmButton: false,
          timer: 3000,
        });
      },
    });
  }

  loadHabitaciones(hotelId: number): void {
    this.loading = true;
    this.error = null;

    this.habitacionService.getHabitacionesByHotelId(hotelId).subscribe({
      next: (data: HabitacionDTO[]) => {
        this.habitaciones = data;
        this.loading = false;
      },
      error: (err: any) => {
        this.loading = false;
        Swal.fire({
          position: 'top-end',
          icon: 'error',
          title: 'No se pudieron cargar las habitaciones',
          showConfirmButton: false,
          timer: 3000,
        });
      },
    });
  }

  onHotelChange(event: any): void {
    const selectedHotelId = +event.target.value;
    if (selectedHotelId > 0) {
      this.hotelId = selectedHotelId;
      this.router.navigate(['/admin/habitacion/listar', selectedHotelId]);
    } else {
      this.hotelId = 0;
      this.loadTodasLasHabitaciones();
    }
  }

  getHotelName(hotelId: number | undefined): string {
    if (!hotelId) return 'N/A';
    const hotel = this.hoteles.find((h) => h.id === hotelId);
    return hotel ? hotel.nombre : 'Hotel no encontrado';
  }

  verImagenes(habitacionId: number): void {
    if (this.hotelId) {
      this.router.navigate(['/admin/habitacion/imagenes', this.hotelId, habitacionId]);
    } else {
      const habitacion = this.habitaciones.find((h) => h.idHabitacion === habitacionId);
      if (habitacion && habitacion.idHotel) {
        this.router.navigate(['/admin/habitacion/imagenes', habitacion.idHotel, habitacionId]);
      } else {
        Swal.fire({
          position: 'top-end',
          icon: 'error',
          title: 'No se puede determinar el hotel de la habitación seleccionada',
          showConfirmButton: false,
          timer: 3000,
        });
      }
    }
  }

  abrirModalCreacion(): void {
    if (!this.hotelId || this.hotelId === 0) {
      Swal.fire({
        position: 'top-end',
        icon: 'error',
        title: 'Por favor, seleccione un hotel primero',
        showConfirmButton: false,
        timer: 3000,
      });
      return;
    }

    this.nuevaHabitacion = {
      numero: '',
      capacidad: 0,
      precio: 0,
      descripcion: '',
      estado: 'disponible',
      categoriaId: 0,
      imagenUrl: '',
      imagenesUrls: [],
    };
    this.mostrarModalCrear = true;
    this.errorModal = null;
  }

  cerrarModalCrear(): void {
    this.mostrarModalCrear = false;
    this.errorModal = null;
  }

  guardarNuevaHabitacion(): void {
    if (!this.hotelId || this.hotelId === 0) {
      this.errorModal = 'ID de hotel no válido.';
      return;
    }

    if (
      !this.nuevaHabitacion.numero ||
      !this.nuevaHabitacion.capacidad ||
      !this.nuevaHabitacion.precio ||
      !this.nuevaHabitacion.categoriaId
    ) {
      this.errorModal = 'Por favor, complete todos los campos obligatorios.';
      return;
    }

    this.loading = true;
    this.errorModal = null;

    this.habitacionService.createHabitacion(this.hotelId, this.nuevaHabitacion).subscribe({
      next: (habitacion: HabitacionDTO) => {
        this.habitaciones.push(habitacion);
        this.cerrarModalCrear();
        this.loading = false;
        Swal.fire({
          position: 'top-end',
          icon: 'success',
          title: 'Habitación creada con éxito',
          showConfirmButton: false,
          timer: 2000,
        });
      },
      error: (err: any) => {
        this.loading = false;
        Swal.fire({
          position: 'top-end',
          icon: 'error',
          title: 'No se pudo crear la habitación',
          showConfirmButton: false,
          timer: 3000,
        });
      },
    });
  }

  abrirModalEdicion(habitacion: HabitacionDTO): void {
    this.habitacionEnEdicion = habitacion;
    this.habitacionEditada = {
      ...habitacion,
      categoria: {
        ...habitacion.categoria,
      },
    };
    this.mostrarModalEditar = true;
    this.errorModal = null;
  }

  cerrarModalEditar(): void {
    this.mostrarModalEditar = false;
    this.habitacionEnEdicion = null;
    this.errorModal = null;
  }

  guardarHabitacionEditada(): void {
    if (!this.hotelId || this.hotelId === 0 || !this.habitacionEnEdicion) {
      this.errorModal = 'ID de hotel o habitación no válido.';
      return;
    }

    if (
      !this.habitacionEditada.numero ||
      !this.habitacionEditada.capacidad ||
      !this.habitacionEditada.precio
    ) {
      this.errorModal = 'Por favor, complete todos los campos obligatorios.';
      return;
    }

    this.loading = true;
    this.errorModal = null;

    const habitacionToUpdate: HabitacionDTO = {
      ...this.habitacionEditada,
      categoria: {
        ...this.habitacionEditada.categoria,
        id: this.habitacionEditada.categoria.id ? +this.habitacionEditada.categoria.id : undefined,
      },
    };

    this.habitacionService
      .updateHabitacion(this.hotelId, this.habitacionEnEdicion.idHabitacion!, habitacionToUpdate)
      .subscribe({
        next: (habitacionActualizada: HabitacionDTO) => {
          const index = this.habitaciones.findIndex(
            (h) => h.idHabitacion === this.habitacionEnEdicion!.idHabitacion
          );
          if (index !== -1) {
            this.habitaciones[index] = habitacionActualizada;
          }

          this.cerrarModalEditar();
          this.loading = false;
          Swal.fire({
            position: 'top-end',
            icon: 'success',
            title: 'Habitación actualizada con éxito',
            showConfirmButton: false,
            timer: 2000,
          });
        },
        error: (err: any) => {
          this.loading = false;
          Swal.fire({
            position: 'top-end',
            icon: 'error',
            title: 'No se pudo actualizar la habitación',
            showConfirmButton: false,
            timer: 3000,
          });
        },
      });
  }

  eliminarHabitacion(id: number, numero: string): void {
    this.habitacionAEliminar = { id, numero };
    this.mensajeModal = '¿Está seguro que desea eliminar esta habitación?';
    this.mostrandoModalConfirmacion = true;
  }

  confirmarEliminacion(): void {
    if (this.habitacionAEliminar) {
      const habitacionId = this.habitacionAEliminar.id;

      if (this.hotelId && this.hotelId > 0) {
        this.habitacionService.deleteHabitacion(this.hotelId, habitacionId).subscribe({
          next: () => {
            if (this.hotelId && this.hotelId > 0) {
              this.loadHabitaciones(this.hotelId);
            } else {
              this.loadTodasLasHabitaciones();
            }
            Swal.fire({
              position: 'top-end',
              icon: 'success',
              title: `La habitación "${this.habitacionAEliminar!.numero}" ha sido eliminada correctamente.`,
              showConfirmButton: false,
              timer: 2000,
            });
            this.cerrarModalConfirmacion();
          },
          error: (err: any) => {
            Swal.fire({
              position: 'top-end',
              icon: 'error',
              title: 'Error al eliminar la habitación. Por favor, inténtelo más tarde.',
              showConfirmButton: false,
              timer: 3000,
            });
            this.cerrarModalConfirmacion();
          },
        });
      } else {
        const habitacion = this.habitaciones.find((h) => h.idHabitacion === habitacionId);
        if (habitacion && habitacion.idHotel) {
          this.habitacionService.deleteHabitacion(habitacion.idHotel, habitacionId).subscribe({
            next: () => {
              this.loadTodasLasHabitaciones();
              Swal.fire({
                position: 'top-end',
                icon: 'success',
                title: `La habitación "${this.habitacionAEliminar!.numero}" ha sido eliminada correctamente.`,
                showConfirmButton: false,
                timer: 2000,
              });
              this.cerrarModalConfirmacion();
            },
            error: (err: any) => {
              Swal.fire({
                position: 'top-end',
                icon: 'error',
                title: 'Error al eliminar la habitación. Por favor, inténtelo más tarde.',
                showConfirmButton: false,
                timer: 3000,
              });
              this.cerrarModalConfirmacion();
            },
          });
        } else {
          Swal.fire({
            position: 'top-end',
            icon: 'error',
            title: 'No se puede determinar el hotel de la habitación seleccionada.',
            showConfirmButton: false,
            timer: 3000,
          });
          this.cerrarModalConfirmacion();
        }
      }
    }
  }

  cerrarModalConfirmacion(): void {
    this.mostrandoModalConfirmacion = false;
    this.habitacionAEliminar = null;
    this.mensajeModal = '';
  }

  volverAHotel(): void {
    this.router.navigate(['/admin/hoteles']);
  }
}