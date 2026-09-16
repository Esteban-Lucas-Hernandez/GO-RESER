import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CategoriaService, CategoriaHabitacionDTO } from '../category.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-listar-categoria',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './listar-categoria.component.html',
  styleUrls: ['./listar-categoria.component.css'],
})
export class ListarCategoriaComponent implements OnInit {
  titulo = 'Gestión de Categorías';
  descripcion = 'Aquí puedes administrar las categorías del sistema.';
  categorias: CategoriaHabitacionDTO[] = [];
  loading = false;
  error: string | null = null;

  mostrarModalCrear = false;
  nuevaCategoria: CategoriaHabitacionDTO = { nombre: '', descripcion: '' };

  mostrarModalEditar = false;
  categoriaEditada: CategoriaHabitacionDTO = { nombre: '', descripcion: '' };
  categoriaEnEdicion: CategoriaHabitacionDTO | null = null;

  mostrandoModalConfirmacion = false;
  mensajeModal = '';
  categoriaAEliminar: { id: number; nombre: string } | null = null;

  constructor(private router: Router, private categoriaService: CategoriaService) {}

  ngOnInit(): void {
    this.loadCategorias();
  }

  loadCategorias(): void {
    this.loading = true;
    this.error = null;

    this.categoriaService.getCategorias().subscribe({
      next: (data: CategoriaHabitacionDTO[]) => {
        this.categorias = data;
        this.loading = false;
      },
      error: (err: any) => {
        this.loading = false;
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

  abrirModalCreacion(): void {
    this.nuevaCategoria = { nombre: '', descripcion: '' };
    this.mostrarModalCrear = true;
  }

  cerrarModalCrear(): void {
    this.mostrarModalCrear = false;
    this.error = null;
  }

  guardarNuevaCategoria(): void {
    if (!this.nuevaCategoria.nombre || !this.nuevaCategoria.descripcion) {
      this.error = 'Por favor, complete todos los campos.';
      return;
    }

    this.loading = true;
    this.error = null;

    this.categoriaService.createCategoria(this.nuevaCategoria).subscribe({
      next: (categoria: CategoriaHabitacionDTO) => {
        this.categorias.push(categoria);
        this.cerrarModalCrear();
        this.loading = false;
        Swal.fire({
          position: 'top-end',
          icon: 'success',
          title: 'Categoría creada con éxito',
          showConfirmButton: false,
          timer: 2000,
        });
      },
      error: (err: any) => {
        this.loading = false;
        Swal.fire({
          position: 'top-end',
          icon: 'error',
          title: 'No se pudo crear la categoría',
          showConfirmButton: false,
          timer: 3000,
        });
      },
    });
  }

  abrirModalEdicion(categoria: CategoriaHabitacionDTO): void {
    this.categoriaEnEdicion = categoria;
    this.categoriaEditada = { ...categoria };
    this.mostrarModalEditar = true;
  }

  cerrarModalEditar(): void {
    this.mostrarModalEditar = false;
    this.categoriaEnEdicion = null;
    this.error = null;
  }

  guardarCategoriaEditada(): void {
    if (!this.categoriaEditada.nombre || !this.categoriaEditada.descripcion) {
      this.error = 'Por favor, complete todos los campos.';
      return;
    }

    if (!this.categoriaEnEdicion) {
      this.error = 'No se ha seleccionado ninguna categoría para editar.';
      return;
    }

    this.loading = true;
    this.error = null;

    this.categoriaService
      .updateCategoria(this.categoriaEnEdicion.id!, this.categoriaEditada)
      .subscribe({
        next: (categoriaActualizada: CategoriaHabitacionDTO) => {
          const index = this.categorias.findIndex((cat) => cat.id === this.categoriaEnEdicion!.id);
          if (index !== -1) {
            this.categorias[index] = categoriaActualizada;
          }

          this.cerrarModalEditar();
          this.loading = false;
        },
        error: (err: any) => {
          console.error('Error al actualizar categoría:', err);
          if (err.status === 401) {
            this.error = 'No autorizado. Por favor, inicie sesión nuevamente.';
          } else if (err.status === 403) {
            this.error = 'Acceso denegado. No tiene permisos para realizar esta acción.';
          } else {
            this.error = 'No se pudo actualizar la categoría. Por favor, inténtelo más tarde.';
          }
          this.loading = false;
        },
      });
  }

  eliminarCategoria(id: number, nombre: string): void {
    this.categoriaAEliminar = { id, nombre };
    this.mensajeModal = '¿Está seguro que desea eliminar esta categoría?';
    this.mostrandoModalConfirmacion = true;
  }

  confirmarEliminacion(): void {
    if (this.categoriaAEliminar) {
      this.categoriaService.deleteCategoria(this.categoriaAEliminar.id).subscribe({
        next: () => {
          this.loadCategorias();
          Swal.fire({
            position: 'top-end',
            icon: 'success',
            title: `La categoría "${this.categoriaAEliminar!.nombre}" ha sido eliminada correctamente.`,
            showConfirmButton: false,
            timer: 2000,
          });
          this.cerrarModalConfirmacion();
        },
        error: (err: any) => {
          let errorMsg = 'No se pudo eliminar la categoría. Por favor, inténtelo más tarde.';
          if (err.status === 401) {
            errorMsg = 'No autorizado. Por favor, inicie sesión nuevamente.';
          } else if (err.status === 403) {
            errorMsg = 'Acceso denegado. No tiene permisos para realizar esta acción.';
          } else if (err.status === 0) {
            errorMsg = 'Error de conexión. Verifique su conexión a internet o inténtelo más tarde.';
          } else if (err.error && typeof err.error === 'string') {
            errorMsg = err.error;
          } else if (err.error && err.error.message) {
            errorMsg = err.error.message;
          }
          Swal.fire({
            position: 'top-end',
            icon: 'error',
            title: errorMsg,
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
    this.categoriaAEliminar = null;
    this.mensajeModal = '';
  }
}