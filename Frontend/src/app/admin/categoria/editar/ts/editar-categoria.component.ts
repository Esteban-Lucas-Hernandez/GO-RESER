import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { CategoriaService, CategoriaHabitacionDTO } from '../../categoria.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-editar-categoria',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: '../html/editar-categoria.component.html',
  styleUrls: ['../css/editar-categoria.component.css'],
})
export class EditarCategoriaComponent implements OnInit {
  categoria: CategoriaHabitacionDTO = {
    id: undefined,
    nombre: '',
    descripcion: '',
  };
  loading = false;
  error: string | null = null;
  categoriaId: number | null = null;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private categoriaService: CategoriaService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.categoriaId = +params['id'];
      if (this.categoriaId) {
        this.loadCategoria(this.categoriaId);
      }
    });
  }

  loadCategoria(id: number): void {
    this.loading = true;
    this.error = null;

    this.categoriaService.getCategoriaById(id).subscribe({
      next: (data: CategoriaHabitacionDTO) => {
        this.categoria = data;
        this.loading = false;
      },
      error: (err: any) => {
        this.loading = false;
        Swal.fire({
          position: 'top-end',
          icon: 'error',
          title: 'No se pudo cargar la categoría',
          showConfirmButton: false,
          timer: 3000,
        });
      },
    });
  }

  onSubmit(): void {
    if (!this.categoria.nombre || !this.categoria.descripcion) {
      this.error = 'Por favor, complete todos los campos obligatorios.';
      return;
    }

    if (!this.categoria.id) {
      this.error = 'ID de categoría no válido.';
      return;
    }

    this.loading = true;
    this.error = null;

    this.categoriaService.updateCategoria(this.categoria.id, this.categoria).subscribe({
      next: () => {
        this.loading = false;
        Swal.fire({
          position: 'top-end',
          icon: 'success',
          title: 'Categoría actualizada con éxito',
          showConfirmButton: false,
          timer: 2000,
        });
        this.router.navigate(['/admin/categoria/listar']);
      },
      error: (err: any) => {
        this.loading = false;
        Swal.fire({
          position: 'top-end',
          icon: 'error',
          title: 'No se pudo actualizar la categoría',
          showConfirmButton: false,
          timer: 3000,
        });
      },
    });
  }

  cancelar(): void {
    this.router.navigate(['/admin/categoria/listar']);
  }
}