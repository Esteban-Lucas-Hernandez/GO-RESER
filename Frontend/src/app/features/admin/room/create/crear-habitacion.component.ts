import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { HabitacionService } from '../room.service';
import { CategoriaService } from '../../category/category.service';
import { CrearHabitacionDTO, CategoriaHabitacionDTO } from '../habitacion.interface';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-crear-habitacion',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './crear-habitacion.component.html',
  styleUrls: ['./crear-habitacion.component.css'],
})
export class CrearHabitacionComponent implements OnInit {
  habitacion: CrearHabitacionDTO = {
    numero: '',
    capacidad: 0,
    precio: 0,
    descripcion: '',
    estado: 'disponible',
    categoriaId: 0,
    imagenUrl: '',
    imagenesUrls: [],
  };

  categorias: CategoriaHabitacionDTO[] = [];
  loading = false;
  error: string | null = null;
  hotelId: number | null = null;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private habitacionService: HabitacionService,
    private categoriaService: CategoriaService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.hotelId = +params['hotelId'];
    });

    this.loadCategorias();
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

  onSubmit(): void {
    if (!this.hotelId) {
      this.error = 'ID de hotel no válido.';
      return;
    }

    if (
      !this.habitacion.numero ||
      !this.habitacion.capacidad ||
      !this.habitacion.precio ||
      !this.habitacion.categoriaId
    ) {
      this.error = 'Por favor, complete todos los campos obligatorios.';
      return;
    }

    this.loading = true;
    this.error = null;

    this.habitacionService.createHabitacion(this.hotelId, this.habitacion).subscribe({
      next: () => {
        this.loading = false;
        Swal.fire({
          position: 'top-end',
          icon: 'success',
          title: 'Habitación creada con éxito',
          showConfirmButton: false,
          timer: 2000,
        });
        this.router.navigate(['/admin/habitacion/listar', this.hotelId]);
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

  cancelar(): void {
    if (this.hotelId) {
      this.router.navigate(['/admin/habitacion/listar', this.hotelId]);
    }
  }
}