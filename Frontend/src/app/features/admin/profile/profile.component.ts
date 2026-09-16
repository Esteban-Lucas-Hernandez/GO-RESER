import Swal from 'sweetalert2';
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PerfilService } from './profile.service';
import { UsuarioDTO } from '../../../shared/models/user.model';
import { ActualizarPerfilDTO } from '../../../shared/models/update-profile.dto';
import { AuthService } from '../../../core/auth/auth.service';

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

  constructor(private perfilService: PerfilService, private authService: AuthService) {}

  ngOnInit(): void {
    this.userRole = this.authService.getUserRole();

    const token = this.authService.getToken();
    if (token) {
      try {
        this.tokenInfo = this.authService.decodeToken(token);
      } catch (e) {
        console.error('Error al decodificar token:', e);
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
          title: 'Error al cargar el perfil de administrador',
          showConfirmButton: false,
          timer: 3000,
        });

        if (err.status === 403) {
          let errorMsg = 'No tiene permisos para acceder a esta información.\n';
          errorMsg += 'Posibles causas:\n';
          errorMsg += '- Su rol de usuario no tiene acceso al perfil de administrador\n';
          errorMsg += `- Su rol actual es: ${this.userRole || 'No identificado'}\n`;

          if (this.tokenInfo && this.tokenInfo.roles) {
            errorMsg += `- Roles en el token: ${this.tokenInfo.roles.join(', ')}\n`;
          }

          errorMsg += '\nPor favor, contacte al administrador del sistema.';
          this.error = errorMsg;
        } else {
          this.error =
            'No se pudo cargar la información del perfil de administrador. Por favor, inténtelo más tarde.';
        }

        this.loading = false;
      },
    });
  }

  toggleEditMode(): void {
    this.editMode = !this.editMode;
    if (!this.editMode && this.perfil) {
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
      if (this.perfil) {
        switch (field) {
          case 'nombreCompleto':
            this.updateData.nombreCompleto = this.perfil.nombreCompleto || '';
            break;
          case 'telefono':
            this.updateData.telefono = this.perfil.telefono || '';
            break;
          case 'documento':
            this.updateData.documento = this.perfil.documento || '';
            break;
          case 'email':
            this.updateData.email = this.perfil.email || '';
            break;
          case 'fotoUrl':
            this.updateData.fotoUrl = this.perfil.fotoUrl || '';
            break;
        }
      }
    } else {
      this.editingField = field;
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
        icon: 'error',
        title: 'No hay cambios para actualizar.',
        showConfirmButton: false,
        timer: 3000,
      });
      return;
    }

    this.perfilService.updateProfile(dataToUpdate).subscribe({
      next: (updatedProfile: UsuarioDTO) => {
        this.perfil = updatedProfile;
        this.editingField = null;
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

    if (Object.keys(dataToUpdate).length === 0) {
      Swal.fire({
        position: 'top-end',
        icon: 'error',
        title: 'No hay cambios para actualizar.',
        showConfirmButton: false,
        timer: 3000,
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
}