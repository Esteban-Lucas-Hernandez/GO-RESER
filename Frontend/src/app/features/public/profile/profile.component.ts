import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { PerfilService } from './profile.service';
import { UsuarioDTO } from '../../../shared/models/user.model';
import { ActualizarPerfilDTO } from '../../../shared/models/update-profile.dto';
import { AuthService } from '../../../core/auth/auth.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
})
export class PerfilComponent implements OnInit {
  perfil: UsuarioDTO | null = null;
  loading = false;
  error: string | null = null;
  userRole: string | null = null;
  tokenInfo: any = null;
  editMode = false;
  editingField: string | null = null;
  showPasswordChange = false;
  updateData: ActualizarPerfilDTO = {
    nombreCompleto: '',
    telefono: '',
    documento: '',
    email: '',
    contrasena: '',
    fotoUrl: '',
  };

  constructor(
    private perfilService: PerfilService, 
    private authService: AuthService, 
    private cdr: ChangeDetectorRef,
    private router: Router
  ) { }

  ngOnInit(): void {
    // Obtener el rol del usuario para mostrarlo en la UI
    this.userRole = this.authService.getUserRole();

    // Obtener información del token para depuración
    const token = this.authService.getToken();
    if (token) {
      try {
        this.tokenInfo = this.authService.decodeToken(token);

        // Crear un mensaje de bienvenida más amigable
        const userName = this.tokenInfo.username || 'Usuario';
        const userRoles = this.tokenInfo.roles ? this.tokenInfo.roles.join(', ') : 'No definido';
        const welcomeMessage = `¡Hola, ${userName}`;

        Swal.fire({
          position: 'top-end',
          icon: 'info',
          title: 'Bienvenido a tu perfil',
          text: welcomeMessage,
          showConfirmButton: false,
          timer: 2500, // Aumentado ligeramente para dar tiempo a leer
        });
      } catch (e) {
        // Usar el servicio de alertas para consistencia
        Swal.fire({
          position: 'top-end',
          icon: 'error',
          title: 'Error al verificar tu sesión',
          showConfirmButton: false,
          timer: 3000,
        });
      }
    }

    this.loadProfile();
  }

  loadProfile(): void {
    this.loading = true;
    this.error = null;

    this.perfilService.getProfile().subscribe({
      next: (data: UsuarioDTO) => {
        this.perfil = data;
        this.loading = false;
        // Inicializar los datos de actualización con los valores actuales
        if (this.perfil) {
          this.updateData.nombreCompleto = this.perfil.nombreCompleto || '';
          this.updateData.telefono = this.perfil.telefono || '';
          this.updateData.documento = this.perfil.documento || '';
          this.updateData.email = this.perfil.email || '';
          this.updateData.fotoUrl = this.perfil.fotoUrl || '';
        }
      },
      error: (err: any) => {
        Swal.fire({
          position: 'top-end',
          icon: 'error',
          title: 'Error al cargar el perfil de usuario',
          showConfirmButton: false,
          timer: 3000,
        });

        // Manejo específico del error 403
        if (err.status === 403) {
          let errorMsg = 'No tiene permisos para acceder a esta información.\n';
          errorMsg += 'Posibles causas:\n';
          errorMsg += '- Su rol de usuario no tiene acceso al perfil de usuario\n';
          errorMsg += `- Su rol actual es: ${this.userRole || 'No identificado'}\n`;

          if (this.tokenInfo && this.tokenInfo.roles) {
            errorMsg += `- Roles en el token: ${this.tokenInfo.roles.join(', ')}\n`;
          }

          errorMsg += '\nPor favor, contacte al administrador del sistema.';
          this.error = errorMsg;
        } else {
          this.error =
            'No se pudo cargar la información del perfil de usuario. Por favor, inténtelo más tarde.';
        }

        this.loading = false;
      },
    });
  }

  toggleEditMode(): void {
    this.editMode = !this.editMode;
    // Si se entra en modo edición, inicializar los valores con los datos actuales del perfil
    if (this.editMode && this.perfil) {
      // Asegurarse de que los datos estén inicializados
      this.updateData.nombreCompleto = this.updateData.nombreCompleto || this.perfil.nombreCompleto || '';
      this.updateData.telefono = this.updateData.telefono || this.perfil.telefono || '';
      this.updateData.documento = this.updateData.documento || this.perfil.documento || '';
      this.updateData.email = this.updateData.email || this.perfil.email || '';
      this.updateData.fotoUrl = this.updateData.fotoUrl || this.perfil.fotoUrl || '';
    }
    // Si se cancela la edición, restaurar los valores originales
    else if (!this.editMode && this.perfil) {
      this.updateData.nombreCompleto = this.perfil.nombreCompleto || '';
      this.updateData.telefono = this.perfil.telefono || '';
      this.updateData.documento = this.perfil.documento || '';
      this.updateData.email = this.perfil.email || '';
      this.updateData.fotoUrl = this.perfil.fotoUrl || '';
      this.updateData.contrasena = '';
    }
  }

  toggleFieldEdit(field: string): void {
    if (this.editingField === field) {
      this.editingField = null;
      // Limpiar datos al cancelar
      this.updateData = {
        nombreCompleto: '',
        telefono: '',
        documento: '',
        email: '',
        contrasena: '',
        fotoUrl: '',
      };
    } else {
      this.editingField = field;
      // Inicializar el valor del campo que se va a editar
      if (this.perfil) {
        switch (field) {
          case 'nombreCompleto':
            this.updateData.nombreCompleto = this.perfil.nombreCompleto;
            break;
          case 'documento':
            this.updateData.documento = this.perfil.documento;
            break;
          case 'email':
            this.updateData.email = this.perfil.email;
            break;
          case 'telefono':
            this.updateData.telefono = this.perfil.telefono;
            break;
          case 'fotoUrl':
            this.updateData.fotoUrl = this.perfil.fotoUrl;
            break;
        }
      }
    }
  }

  saveField(field: string): void {
    const dataToUpdate: ActualizarPerfilDTO = {};

    switch (field) {
      case 'nombreCompleto':
        if (this.updateData.nombreCompleto) {
          dataToUpdate.nombreCompleto = this.updateData.nombreCompleto;
        }
        break;
      case 'telefono':
        if (this.updateData.telefono) {
          dataToUpdate.telefono = this.updateData.telefono;
        }
        break;
      case 'documento':
        if (this.updateData.documento) {
          dataToUpdate.documento = this.updateData.documento;
        }
        break;
      case 'email':
        if (this.updateData.email) {
          dataToUpdate.email = this.updateData.email;
        }
        break;
      case 'fotoUrl':
        if (this.updateData.fotoUrl) {
          dataToUpdate.fotoUrl = this.updateData.fotoUrl;
        }
        break;
    }

    if (Object.keys(dataToUpdate).length === 0) {
      Swal.fire({
        position: 'top-end',
        icon: 'info',
        title: 'No hay cambios para actualizar.',
        showConfirmButton: false,
        timer: 2000,
      });
      return;
    }

    this.perfilService.updateProfile(dataToUpdate).subscribe({
      next: (updatedProfile: UsuarioDTO) => {
        // Actualizar localmente el perfil con los datos devueltos por el backend
        this.perfil = updatedProfile;

        // Actualización optimista/manual para asegurar que la UI refleje el cambio inmediatamente
        // (útil si el backend devuelve el objeto sin actualizar o hay retraso)
        if (this.perfil) {
          this.perfil = { ...this.perfil, ...dataToUpdate };
        }

        this.editingField = null;
        this.cdr.detectChanges(); // Forzar detección de cambios
        Swal.fire({
          position: 'top-end',
          icon: 'success',
          title: 'Campo actualizado correctamente.',
          showConfirmButton: false,
          timer: 2000,
        });
      },
      error: (err: any) => {
        Swal.fire({
          position: 'top-end',
          icon: 'error',
          title: 'Error al actualizar el campo. Por favor, inténtelo más tarde.',
          showConfirmButton: false,
          timer: 3000,
        });
      },
    });
  }

  handleImageError(event: any): void {
    // Establecer una imagen por defecto si falla la carga de la imagen de perfil
    event.target.src =
      'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png';
  }

  togglePasswordChange(): void {
    this.showPasswordChange = !this.showPasswordChange;
    if (!this.showPasswordChange) {
      this.updateData.contrasena = '';
    }
  }

  updatePassword(): void {
    if (!this.updateData.contrasena) {
      Swal.fire({
        position: 'top-end',
        icon: 'error',
        title: 'Por favor, ingrese la nueva contraseña.',
        showConfirmButton: false,
        timer: 3000,
      });
      return;
    }

    if (this.updateData.contrasena.trim() === '') {
      Swal.fire({
        position: 'top-end',
        icon: 'error',
        title: 'Por favor, ingrese una contraseña válida.',
        showConfirmButton: false,
        timer: 3000,
      });
      return;
    }

    const dataToUpdate: ActualizarPerfilDTO = {
      contrasena: this.updateData.contrasena,
    };

    this.perfilService.updateProfile(dataToUpdate).subscribe({
      next: (updatedProfile: UsuarioDTO) => {
        this.perfil = updatedProfile;
        this.showPasswordChange = false;
        this.updateData.contrasena = '';
        Swal.fire({
          position: 'top-end',
          icon: 'success',
          title: 'Contraseña actualizada correctamente.',
          showConfirmButton: false,
          timer: 2000,
        });
      },
      error: (err: any) => {
        Swal.fire({
          position: 'top-end',
          icon: 'error',
          title: 'Error al actualizar la contraseña. Por favor, inténtelo más tarde.',
          showConfirmButton: false,
          timer: 3000,
        });
      },
    });
  }

  updateProfile(): void {
    // Filtrar solo los campos que tienen valores para actualizar
    const dataToUpdate: ActualizarPerfilDTO = {};

    if (this.updateData.nombreCompleto) {
      dataToUpdate.nombreCompleto = this.updateData.nombreCompleto;
    }

    if (this.updateData.telefono) {
      dataToUpdate.telefono = this.updateData.telefono;
    }

    if (this.updateData.documento) {
      dataToUpdate.documento = this.updateData.documento;
    }

    if (this.updateData.email) {
      dataToUpdate.email = this.updateData.email;
    }

    if (this.updateData.fotoUrl) {
      dataToUpdate.fotoUrl = this.updateData.fotoUrl;
    }

    if (this.updateData.contrasena) {
      dataToUpdate.contrasena = this.updateData.contrasena;
    }

    // Si no hay datos para actualizar, mostrar un mensaje
    if (Object.keys(dataToUpdate).length === 0) {
      Swal.fire({
        position: 'top-end',
        icon: 'info',
        title: 'No hay cambios para actualizar.',
        showConfirmButton: false,
        timer: 2000,
      });
      return;
    }

    this.perfilService.updateProfile(dataToUpdate).subscribe({
      next: (updatedProfile: UsuarioDTO) => {
        this.perfil = updatedProfile;
        this.editMode = false;
        Swal.fire({
          position: 'top-end',
          icon: 'success',
          title: 'Perfil actualizado correctamente.',
          showConfirmButton: false,
          timer: 2000,
        });
      },
      error: (err: any) => {
        Swal.fire({
          position: 'top-end',
          icon: 'error',
          title: 'Error al actualizar el perfil. Por favor, inténtelo más tarde.',
          showConfirmButton: false,
          timer: 3000,
        });
      },
    });
  }

  goHome(): void {
    this.router.navigate(['/']);
  }
}