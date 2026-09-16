import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HotelService, Hotel, Resena } from '../../hotel.service';

interface Estadistica {
  valor: number | string;
  etiqueta: string;
  valorMostrado?: string;
}

@Component({
  selector: 'app-informacion-extra',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './extra-info.component.html',
  styleUrls: ['./extra-info.component.css'],
})
export class InformacionExtraComponent implements OnInit, OnDestroy {
  // Información de servicios destacados
  servicios = [
    {
      icon: '🏨',
      titulo: 'Amplia Selección de Hoteles',
      descripcion:
        'Encuentra los mejores hoteles en las ubicaciones más deseadas con las comodidades que necesitas.',
    },
    {
      icon: '💰',
      titulo: 'Precios Competitivos',
      descripcion:
        'Ofrecemos las mejores tarifas garantizadas con descuentos exclusivos para nuestros clientes.',
    },
    {
      icon: '🔒',
      titulo: 'Reservas Seguras',
      descripcion:
        'Tu seguridad es nuestra prioridad. Reservas protegidas con encriptación de última generación.',
    },
    {
      icon: '⭐',
      titulo: 'Experiencias Únicas',
      descripcion:
        'Vive experiencias inolvidables con nuestras recomendaciones personalizadas y servicios premium.',
    },
  ];

  // Información estadística
  estadisticas: Estadistica[] = [
    { valor: 0, etiqueta: 'Hoteles', valorMostrado: '0' },
    { valor: 0, etiqueta: 'Reseñas', valorMostrado: '0' },
    { valor: '24/7', etiqueta: 'Soporte Disponible', valorMostrado: '24/7' },
    { valor: 99, etiqueta: 'Recomendación', valorMostrado: '0' },
  ];

  private observer: IntersectionObserver | undefined;

  constructor(private hotelService: HotelService) {}

  ngOnInit() {
    // Cargar el número real de hoteles y reseñas
    this.cargarNumeroHoteles();
    this.cargarNumeroResenas();

    // Crear un observador para detectar cuando la sección entra en la vista
    this.observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            this.iniciarContadores();
            // Dejar de observar después de iniciar la animación
            if (this.observer) {
              this.observer.disconnect();
            }
          }
        });
      },
      { threshold: 0.5 } // Iniciar cuando el 50% de la sección sea visible
    );

    // Empezar a observar el elemento de estadísticas
    setTimeout(() => {
      const estadisticasElement = document.querySelector('.estadisticas-section');
      if (estadisticasElement && this.observer) {
        this.observer.observe(estadisticasElement);
      }
    }, 1000);
  }

  cargarNumeroHoteles() {
    this.hotelService.getHoteles().subscribe({
      next: (hoteles: Hotel[]) => {
        // Actualizar el valor de hoteles en las estadísticas
        this.estadisticas[0].valor = hoteles.length;
      },
      error: (error: any) => {
        console.error('Error al cargar hoteles:', error);
        // En caso de error, mantener el valor por defecto
      },
    });
  }

  cargarNumeroResenas() {
    this.hotelService.getTodasResenas().subscribe({
      next: (resenas: Resena[]) => {
        // Actualizar el valor de reseñas en las estadísticas
        this.estadisticas[1].valor = resenas.length;
      },
      error: (error: any) => {
        console.error('Error al cargar reseñas:', error);
        // En caso de error, mantener el valor por defecto
      },
    });
  }

  iniciarContadores() {
    this.estadisticas.forEach((estadistica, index) => {
      // Para valores numéricos, hacer la animación
      if (typeof estadistica.valor === 'number') {
        this.animarContador(index, estadistica.valor);
      }
    });
  }

  animarContador(index: number, valorFinal: number) {
    let valorActual = 0;
    const duracion = 2000; // Duración de la animación en ms
    const incremento = valorFinal / (duracion / 16); // Aproximadamente 60fps

    const interval = setInterval(() => {
      valorActual += incremento;

      if (valorActual >= valorFinal) {
        valorActual = valorFinal;
        clearInterval(interval);
      }

      // Formatear el valor mostrado
      if (index === 1) {
        // Para reseñas, mostrar el valor real sin formato K+
        this.estadisticas[index].valorMostrado = Math.round(valorActual).toString();
      } else {
        this.estadisticas[index].valorMostrado = Math.round(valorActual).toString();
      }

      // Añadir el signo + si es necesario
      if (
        (index === 0 || index === 1 || index === 3) &&
        this.estadisticas[index].valorMostrado !== '0'
      ) {
        if (index === 1) {
          // Para reseñas, no añadir K+
          this.estadisticas[index].valorMostrado += '+';
        } else {
          this.estadisticas[index].valorMostrado += '+';
        }
      }
    }, 16); // Aproximadamente 60fps
  }

  ngOnDestroy() {
    // Limpiar el observador cuando el componente se destruye
    if (this.observer) {
      this.observer.disconnect();
    }
  }

  scrollToHoteles(): void {
    // Encontrar el elemento del carrusel de hoteles y hacer scroll hacia él
    const hotelesElement = document.querySelector('app-hoteles-carrusel');
    if (hotelesElement) {
      hotelesElement.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
  }
}
