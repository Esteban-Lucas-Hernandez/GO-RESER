import { Component, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService, RegistroData } from '../../auth.service'; // Importar el servicio y el modelo
import { AlertService } from '../../../services/alert.service';

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: '../html/registro.component.html',
  styleUrls: ['../css/registro.component.css'],
})
export class RegistroComponent {
  registroForm: FormGroup;
  @Output() registroSuccess = new EventEmitter<any>();
  @Output() loginRequested = new EventEmitter<void>();

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private alertService: AlertService
  ) {
    // Inyectar el servicio y el router
    this.registroForm = this.fb.group({
      nombreCompleto: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      telefono: [''],
      documento: [''],
      contrasena: ['', Validators.required],
      fotoUrl: [''], // Campo opcional para la URL de la foto
    });
  }

  onSubmit() {
    if (this.registroForm.valid) {
      const formData: RegistroData = this.registroForm.value;

      if (!formData.fotoUrl || formData.fotoUrl.trim() === '') {
        formData.fotoUrl =
          'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png';
      }

      this.authService.registrar(formData).subscribe({
        next: (response: any) => {
          this.registroSuccess.emit(response);
        },
        error: (error: any) => {
          this.alertService.showError(error, 'Error al registrar el usuario.');
        },
      });
    } else {
      Object.keys(this.registroForm.controls).forEach((key) => {
        const control = this.registroForm.get(key);
        control?.markAsTouched();
      });
    }
  }
  goToLogin() {
    if (this.loginRequested.observers.length > 0) {
      this.loginRequested.emit();
      return;
    }
    this.router.navigate(['/login']);
  }
}