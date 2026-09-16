import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HotelAdminService, HotelDTO } from '../hotels/hotel.service';
import { ReservasService } from './bookings.service';
import { Reserva } from './booking.interface';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-reservas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './bookings.component.html',
  styleUrls: ['./bookings.component.css'],
})
export class ReservasComponent implements OnInit {
  hoteles: HotelDTO[] = [];
  hotelSeleccionado: number | null = null;
  reservas: Reserva[] = [];
  cargandoHoteles = false;
  cargandoReservas = false;

  constructor(private hotelService: HotelAdminService, private reservasService: ReservasService) {}

  ngOnInit(): void {
    this.cargarHoteles();
  }

  cargarHoteles(): void {
    this.cargandoHoteles = true;
    this.hotelService.getHoteles().subscribe({
      next: (data: HotelDTO[]) => {
        this.hoteles = data;
        this.cargandoHoteles = false;

        if (this.hoteles.length > 0) {
          this.hotelSeleccionado = this.hoteles[0].id;
          this.cargarReservasPorHotel(this.hoteles[0].id);
        }
      },
      error: (error: any) => {
        this.cargandoHoteles = false;
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

  onHotelChange(): void {
    if (this.hotelSeleccionado) {
      this.cargarReservasPorHotel(this.hotelSeleccionado);
    }
  }

  cargarReservasPorHotel(hotelId: number): void {
    this.cargandoReservas = true;
    this.reservasService.getReservasPorHotel(hotelId).subscribe({
      next: (data: Reserva[]) => {
        this.reservas = data;
        this.cargandoReservas = false;
      },
      error: (error: any) => {
        console.error('Error al cargar reservas:', error);
        this.reservas = [];
        this.cargandoReservas = false;
      },
    });
  }

  cargarTodasLasReservas(): void {
    this.cargandoReservas = true;
    this.reservasService.getReservas().subscribe({
      next: (data: Reserva[]) => {
        this.reservas = data;
        this.cargandoReservas = false;
      },
      error: (error: any) => {
        console.error('Error al cargar todas las reservas:', error);
        this.reservas = [];
        this.cargandoReservas = false;
      },
    });
  }

  descargarPdf(reservaId: number): void {
    if (!reservaId || reservaId <= 0) {
      alert('No se puede descargar el PDF: ID de reserva inválido.');
      return;
    }

    this.reservasService.getReservaPdf(reservaId).subscribe({
      next: (blob: Blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `reserva-${reservaId}.pdf`;
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
        document.body.removeChild(a);
      },
      error: (error: any) => {
        if (error.status === 403) {
          alert('No tiene permisos para descargar el PDF de esta reserva.');
        } else if (error.status === 404) {
          alert('No se encontró el PDF para esta reserva.');
        } else {
          alert('No se pudo descargar el PDF. Por favor, inténtelo más tarde.');
        }
      },
    });
  }

  descargarPdfPorHotel(): void {
    if (!this.hotelSeleccionado || this.hotelSeleccionado <= 0) {
      alert('Por favor, seleccione un hotel primero.');
      return;
    }

    this.reservasService.getReservasPorHotelPdf(this.hotelSeleccionado).subscribe({
      next: (blob: Blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `reservas-hotel-${this.hotelSeleccionado}.pdf`;
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
        document.body.removeChild(a);
      },
      error: (error: any) => {
        if (error.status === 403) {
          alert('No tiene permisos para descargar el PDF de las reservas de este hotel.');
        } else if (error.status === 404) {
          alert('No se encontró el PDF para las reservas de este hotel.');
        } else {
          alert('No se pudo descargar el PDF. Por favor, inténtelo más tarde.');
        }
      },
    });
  }

  eliminarReserva(idReserva: number): void {
    if (!idReserva || idReserva <= 0) {
      alert('No se puede eliminar la reserva: ID inválido.');
      return;
    }

    const confirmacion = confirm(
      '¿Está seguro de que desea eliminar esta reserva? Esta acción no se puede deshacer.'
    );
    if (!confirmacion) {
      return;
    }

    this.reservasService.eliminarReserva(idReserva).subscribe({
      next: () => {
        alert('Reserva eliminada correctamente.');
        if (this.hotelSeleccionado) {
          this.cargarReservasPorHotel(this.hotelSeleccionado);
        } else {
          this.cargarTodasLasReservas();
        }
      },
      error: (error: any) => {
        if (error.status === 403) {
          alert('No tiene permisos para eliminar esta reserva.');
        } else if (error.status === 404) {
          alert('No se encontró la reserva para eliminar.');
        } else {
          alert('No se pudo eliminar la reserva. Por favor, inténtelo más tarde.');
        }
      },
    });
  }

  eliminarReservasCanceladasYAntiguas(): void {
    Swal.fire({
      title: '¿Está seguro?',
      text: '¿Desea eliminar todas las reservas canceladas y antiguas? Esta acción no se puede deshacer.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Sí, eliminar',
    }).then((result) => {
      if (result.isConfirmed) {
        this.reservasService.eliminarReservasCanceladasYAntiguas().subscribe({
          next: (cantidad: number) => {
            Swal.fire({
              position: 'top-end',
              icon: 'success',
              title: `Se eliminaron ${cantidad} reservas canceladas y antiguas.`,
              showConfirmButton: false,
              timer: 2000,
            });

            if (this.hotelSeleccionado) {
              this.cargarReservasPorHotel(this.hotelSeleccionado);
            } else {
              this.cargarTodasLasReservas();
            }
          },
          error: (error: any) => {
            let errorMsg = 'No se pudieron eliminar las reservas. Por favor, inténtelo más tarde.';
            if (error.status === 403) {
              errorMsg = 'No tiene permisos para eliminar reservas.';
            }
            Swal.fire({
              position: 'top-end',
              icon: 'error',
              title: errorMsg,
              showConfirmButton: false,
              timer: 3000,
            });
          },
        });
      }
    });
  }
}