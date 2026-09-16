import { Component, OnInit } from '@angular/core';
import { HotelAdminService, HotelDTO } from '../hotels/hotel.service';
import { ResenaService } from './review.service';
import { Resena } from './review.interface';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-resenas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reviews.component.html',
  styleUrls: ['./reviews.component.css'],
})
export class ResenasComponent implements OnInit {
  hoteles: HotelDTO[] = [];
  resenas: Resena[] = [];
  hotelSeleccionado: number | null = null;

  constructor(private hotelService: HotelAdminService, private resenaService: ResenaService) {}

  ngOnInit(): void {
    this.cargarHoteles();
    this.cargarTodasLasResenas();
  }

  cargarHoteles(): void {
    this.hotelService.getHoteles().subscribe({
      next: (data: HotelDTO[]) => {
        this.hoteles = data;
      },
      error: (error: any) => {
        console.error('Error al cargar hoteles:', error);
      },
    });
  }

  cargarTodasLasResenas(): void {
    this.resenaService.getResenas().subscribe({
      next: (data: Resena[]) => {
        this.resenas = data;
        this.hotelSeleccionado = null;
      },
      error: (error: any) => {
        console.error('Error al cargar reseñas:', error);
      },
    });
  }

  cargarResenasPorHotel(hotelId: number): void {
    this.resenaService.getResenasPorHotel(hotelId).subscribe({
      next: (data: Resena[]) => {
        this.resenas = data;
      },
      error: (error: any) => {
        console.error('Error al cargar reseñas por hotel:', error);
        this.resenas = [];
      },
    });
  }

  onHotelChange(event: any): void {
    const hotelId = parseInt(event.target.value, 10);
    this.hotelSeleccionado = hotelId;
    if (!isNaN(hotelId)) {
      this.cargarResenasPorHotel(hotelId);
    }
  }

  verTodasLasResenas(): void {
    this.cargarTodasLasResenas();
  }
}
