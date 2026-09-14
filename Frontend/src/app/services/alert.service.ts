import { Injectable } from '@angular/core';
import Swal from 'sweetalert2';

@Injectable({
  providedIn: 'root'
})
export class AlertService {

  constructor() { }

  showSuccess(title: string, timer = 2000) {
    Swal.fire({
      position: 'top-end',
      icon: 'success',
      title: title,
      showConfirmButton: false,
      timer: timer,
    });
  }

  showError(error: any, defaultMessage = 'Ocurrió un error. Por favor, inténtalo de nuevo.') {
    let errorMessage = defaultMessage;

    if (typeof error === 'string') {
      // Si el error es un string, lo mostramos directamente
      errorMessage = error;
    } else if (error && error.error && typeof error.error === 'string') {
      // Intentamos parsear si el backend devuelve un JSON con un campo de error simple
      try {
        const parsedError = JSON.parse(error.error);
        if (parsedError && parsedError.message) {
          errorMessage = parsedError.message;
        }
      } catch (e) {
        // Si no se puede parsear, usamos el string de error.error
        errorMessage = error.error;
      }
    } else if (error && error.error && error.error.message) {
      // Si el error es un objeto con { error: { message: '...' } }
      errorMessage = error.error.message;
    } else if (error && error.message) {
      // Si el error es un objeto con { message: '...' }
      errorMessage = error.message;
    }

    Swal.fire({
      position: 'top-end',
      icon: 'error',
      title: errorMessage,
      showConfirmButton: false,
      timer: 3000,
    });
  }
}