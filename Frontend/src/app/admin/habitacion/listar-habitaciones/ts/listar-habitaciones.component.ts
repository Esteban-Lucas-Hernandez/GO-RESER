import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { ImagenHabitacionService } from '../../imagen-habitacion.service';
import { ImagenHabitacionDTO } from '../../habitacion.interface';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-listar-habitaciones',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: '../html/listar-habitaciones.component.html',
  styleUrls: ['../css/listar-habitaciones.component.css'],
})
export class ListarHabitacionesComponent implements OnInit {
  titulo = 'Imágenes de Habitación';
  imagenes: ImagenHabitacionDTO[] = [];
  nuevaImagen: ImagenHabitacionDTO = {
    urlImagen: '',
  };
  loading = false;
  error: string | null = null;
  hotelId: number | null = null;
  habitacionId: number | null = null;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private imagenHabitacionService: ImagenHabitacionService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.hotelId = +params['hotelId'];
      this.habitacionId = +params['habitacionId'];
      if (this.hotelId && this.habitacionId) {
        this.loadImagenes(this.hotelId, this.habitacionId);
      }
    });
  }

  loadImagenes(hotelId: number, habitacionId: number): void {
    this.loading = true;
    this.error = null;

    this.imagenHabitacionService.getImagenesByHabitacionId(hotelId, habitacionId).subscribe({
      next: (data: ImagenHabitacionDTO[]) => {
        this.imagenes = data;
        this.loading = false;
      },
      error: (err: any) => {
        this.loading = false;
        Swal.fire({
          position: 'top-end',
          icon: 'error',
          title: 'No se pudieron cargar las imágenes',
          showConfirmButton: false,
          timer: 3000,
        });
      },
    });
  }

  onSubmit(): void {
    if (this.hotelId && this.habitacionId && this.nuevaImagen.urlImagen) {
      this.loading = true;
      this.error = null;

      const imagenParaCrear: ImagenHabitacionDTO = {
        ...this.nuevaImagen,
        idHabitacion: this.habitacionId,
      };

      this.imagenHabitacionService
        .createImagen(this.hotelId, this.habitacionId, imagenParaCrear)
        .subscribe({
          next: (data: ImagenHabitacionDTO) => {
            // Añadir la nueva imagen a la lista
            this.imagenes.push(data);
            // Limpiar el formulario
            this.nuevaImagen.urlImagen = '';
            this.loading = false;
            Swal.fire({
              position: 'top-end',
              icon: 'success',
              title: 'Imagen creada con éxito',
              showConfirmButton: false,
              timer: 2000,
            });
          },
          error: (err: any) => {
            this.loading = false;
            Swal.fire({
              position: 'top-end',
              icon: 'error',
              title: 'No se pudo crear la imagen',
              showConfirmButton: false,
              timer: 3000,
            });
          },
        });
    }
  }

  eliminarImagen(imagenId: number): void {
    if (this.hotelId && this.habitacionId) {
      Swal.fire({
        title: '¿Está seguro?',
        text: 'No podrá revertir esta acción',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí, eliminar',
      }).then((result) => {
        if (result.isConfirmed) {
          this.imagenHabitacionService
            .deleteImagen(this.hotelId!, this.habitacionId!, imagenId)
            .subscribe({
              next: () => {
                // Recargar la lista después de eliminar
                this.loadImagenes(this.hotelId!, this.habitacionId!);
                Swal.fire({
                  position: 'top-end',
                  icon: 'success',
                  title: 'Imagen eliminada con éxito',
                  showConfirmButton: false,
                  timer: 2000,
                });
              },
              error: (err: any) => {
                Swal.fire({
                  position: 'top-end',
                  icon: 'error',
                  title: 'No se pudo eliminar la imagen',
                  showConfirmButton: false,
                  timer: 3000,
                });
              },
            });
        }
      });
    }
  }

  volverAHabitacion(): void {
    if (this.hotelId) {
      this.router.navigate(['/admin/habitacion/listar', this.hotelId]);
    }
  }
}