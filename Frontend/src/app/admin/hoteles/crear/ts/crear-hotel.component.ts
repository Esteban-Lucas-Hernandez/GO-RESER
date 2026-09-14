import Swal from 'sweetalert2';
import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HotelAdminService, HotelDTO, Departamento, Ciudad } from '../../hotel-admin.service';

@Component({
  selector: 'app-crear-hotel',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: '../html/crear-hotel.component.html',
  styleUrls: ['../css/crear-hotel.component.css'],
})
export class CrearHotelComponent implements OnInit {
  @Output() cerrado = new EventEmitter<void>();
  @Output() hotelCreado = new EventEmitter<HotelDTO>();

  hotelForm!: FormGroup;
  departamentos: Departamento[] = [];
  ciudades: Ciudad[] = [];

  constructor(private fb: FormBuilder, private hotelService: HotelAdminService) {}

  ngOnInit(): void {
    this.hotelForm = this.fb.group({
      nombre: ['', Validators.required],
      direccion: ['', Validators.required],
      telefono: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      descripcion: ['', Validators.required],
      estrellas: ['', Validators.required],
      politicaCancelacion: ['', Validators.required],
      checkIn: ['', Validators.required],
      checkOut: ['', Validators.required],
      imagenUrl: [''],
      departamentoId: ['', Validators.required],
      ciudadId: ['', Validators.required],
    });

    this.cargarDepartamentos();
  }

  cargarDepartamentos(): void {
    this.hotelService.getDepartamentos().subscribe({
      next: (data: Departamento[]) => {
        this.departamentos = data;
      },
      error: (error: any) => {
        Swal.fire({
          position: 'top-end',
          icon: 'error',
          title: 'Error al cargar departamentos',
          showConfirmButton: false,
          timer: 3000,
        });
      },
    });
  }

  cargarCiudades(departamentoId: number): void {
    this.hotelService.getCiudadesPorDepartamento(departamentoId).subscribe({
      next: (data: Ciudad[]) => {
        this.ciudades = data;
      },
      error: (error: any) => {
        Swal.fire({
          position: 'top-end',
          icon: 'error',
          title: 'Error al cargar ciudades',
          showConfirmButton: false,
          timer: 3000,
        });
      },
    });
  }

  onDepartamentoChange(event: any): void {
    const departamentoId = event.target.value;
    if (departamentoId) {
      this.cargarCiudades(Number(departamentoId));
      this.hotelForm.patchValue({ ciudadId: '' });
    } else {
      this.ciudades = [];
      this.hotelForm.patchValue({ ciudadId: '' });
    }
  }

  onSubmit(): void {
    if (this.hotelForm.valid) {
      const ciudadId = this.hotelForm.get('ciudadId')?.value;

      const formValue = { ...this.hotelForm.value };
      delete formValue.departamentoId;
      delete formValue.ciudadId;

      const hotelData = {
        ...formValue,
        estrellas: Number(formValue.estrellas),
        ciudadId: Number(ciudadId),
      };

      this.hotelService.crearHotel(hotelData).subscribe({
        next: (hotel: HotelDTO) => {
          this.hotelCreado.emit(hotel);
          this.cerrar();
        },
        error: (error: any) => {
          Swal.fire({
            position: 'top-end',
            icon: 'error',
            title: 'Error al crear el hotel. Por favor, inténtalo de nuevo.',
            showConfirmButton: false,
            timer: 3000,
          });
        },
      });
    } else {
      Swal.fire({
        position: 'top-end',
        icon: 'error',
        title: 'Formulario inválido',
        showConfirmButton: false,
        timer: 3000,
      });
      Object.keys(this.hotelForm.controls).forEach((key) => {
        const control = this.hotelForm.get(key);
        control?.markAsTouched();
      });
    }
  }

  cerrar(): void {
    this.cerrado.emit();
  }
}