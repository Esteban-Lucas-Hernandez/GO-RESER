import Swal from 'sweetalert2';
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HotelAdminService } from './hotel.service';
import { HotelDTO } from './hotel.service';
import { EditarHotelComponent } from './edit/editar-hotel.component';
import { CrearHotelComponent } from './create/crear-hotel.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import {
  faPhone,
  faEnvelope,
  faMapMarkerAlt,
  faCity,
  faMap,
  faStar,
  faSignInAlt,
  faSignOutAlt,
  faImage,
  faCalendarPlus,
  faHistory,
  faEdit,
  faTrash,
} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-hoteles',
  standalone: true,
  imports: [CommonModule, EditarHotelComponent, CrearHotelComponent, FontAwesomeModule],
  templateUrl: './hotels.component.html',
  styleUrls: ['./hotels.component.css'],
})
export class HotelesComponent implements OnInit {
  faPhone = faPhone;
  faEnvelope = faEnvelope;
  faMapMarkerAlt = faMapMarkerAlt;
  faCity = faCity;
  faMap = faMap;
  faStar = faStar;
  faSignInAlt = faSignInAlt;
  faSignOutAlt = faSignOutAlt;
  faImage = faImage;
  faCalendarPlus = faCalendarPlus;
  faHistory = faHistory;
  faEdit = faEdit;
  faTrash = faTrash;

  titulo = 'Gestión de Hoteles';
  descripcion = 'Aquí puedes administrar los hoteles del sistema.';
  hoteles: HotelDTO[] = [];
  hotelAEditar: HotelDTO | null = null;
  mostrandoFormularioCrear = false;

  mostrandoModalConfirmacion = false;
  mensajeModal = '';
  hotelAEliminar: { id: number; nombre: string } | null = null;
  tieneHabitaciones = false;

  constructor(private hotelService: HotelAdminService) { }

  ngOnInit(): void {
    this.cargarHoteles();
  }

  cargarHoteles(): void {
    this.hotelService.getHoteles().subscribe({
      next: (data: any) => {
        this.hoteles = data;
      },
      error: (error: any) => {
        Swal.fire({
          position: 'top-end',
          icon: 'error',
          title: 'Error al cargar hoteles',
          showConfirmButton: false,
          timer: 3000,
        });
      },
    });
  }

  eliminarHotel(id: number, nombre: string): void {
    this.hotelAEliminar = { id, nombre };

    this.hotelService.verificarHabitacionesHotel(id).subscribe({
      next: (tieneHabitaciones: any) => {
        this.tieneHabitaciones = tieneHabitaciones;

        if (tieneHabitaciones) {
          this.mensajeModal =
            'Pueden haber habitaciones y reservas asociadas. ¿Desea eliminar este hotel?';
        } else {
          this.mensajeModal =
            'No hay habitaciones ni reservas relacionadas. ¿Desea eliminar este hotel?';
        }

        this.mostrandoModalConfirmacion = true;
      },
      error: (error: any) => {
        Swal.fire({
          position: 'top-end',
          icon: 'error',
          title: 'Error al verificar habitaciones',
          showConfirmButton: false,
          timer: 3000,
        });
        this.mensajeModal = '¿Desea eliminar este hotel?';
        this.mostrandoModalConfirmacion = true;
      },
    });
  }

  confirmarEliminacion(): void {
    if (this.hotelAEliminar) {
      this.hotelService.eliminarHotelCascade(this.hotelAEliminar.id).subscribe({
        next: () => {
          this.hoteles = this.hoteles.filter((hotel) => hotel.id !== this.hotelAEliminar!.id);
          Swal.fire({
            position: 'top-end',
            icon: 'success',
            title: `El hotel "${this.hotelAEliminar!.nombre}" ha sido eliminado correctamente.`,
            showConfirmButton: false,
            timer: 2000,
          });
          this.cerrarModalConfirmacion();
        },
        error: (error: any) => {
          Swal.fire({
            position: 'top-end',
            icon: 'error',
            title: 'Error al eliminar el hotel. Por favor, inténtalo de nuevo.',
            showConfirmButton: false,
            timer: 3000,
          });
          this.cerrarModalConfirmacion();
        },
      });
    }
  }

  cerrarModalConfirmacion(): void {
    this.mostrandoModalConfirmacion = false;
    this.hotelAEliminar = null;
    this.tieneHabitaciones = false;
    this.mensajeModal = '';
  }

  editarHotel(hotel: HotelDTO): void {
    this.hotelAEditar = hotel;
  }

  cerrarEdicion(): void {
    this.hotelAEditar = null;
  }

  guardarHotel(hotelActualizado: any): void {
    let hotelData: HotelDTO;

    if (hotelActualizado && hotelActualizado.data) {
      hotelData = hotelActualizado.data;
    } else if (hotelActualizado && hotelActualizado.id) {
      hotelData = hotelActualizado;
    } else {
      Swal.fire({
        position: 'top-end',
        icon: 'error',
        title: 'Error: Datos del hotel no válidos. Por favor, inténtalo de nuevo.',
        showConfirmButton: false,
        timer: 3000,
      });
      return;
    }

    if (hotelData.id === undefined || hotelData.id === null) {
      Swal.fire({
        position: 'top-end',
        icon: 'error',
        title: 'Error: ID de hotel no válido. Por favor, inténtalo de nuevo.',
        showConfirmButton: false,
        timer: 3000,
      });
      return;
    }

    this.hotelService.actualizarHotel(hotelData.id, hotelData).subscribe({
      next: (hotel: any) => {
        Swal.fire({
          position: 'top-end',
          icon: 'success',
          title: 'Hotel actualizado correctamente.',
          showConfirmButton: false,
          timer: 2000,
        });
        this.cerrarEdicion();
        this.cargarHoteles();
      },
      error: (error: any) => {
        Swal.fire({
          position: 'top-end',
          icon: 'error',
          title: 'Error al actualizar el hotel. Por favor, inténtalo de nuevo.',
          showConfirmButton: false,
          timer: 3000,
        });
      },
    });
  }

  mostrarFormularioCrear(): void {
    this.mostrandoFormularioCrear = true;
  }

  cerrarFormularioCrear(): void {
    this.mostrandoFormularioCrear = false;
  }

  hotelCreado(nuevoHotel: HotelDTO): void {
    this.cerrarFormularioCrear();
    Swal.fire({
      position: 'top-end',
      icon: 'success',
      title: 'Hotel creado correctamente.',
      showConfirmButton: false,
      timer: 2000,
    });
    this.cargarHoteles();
  }
}