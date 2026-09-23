import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { BaseChartDirective } from 'ng2-charts';
import { ChartConfiguration, ChartData } from 'chart.js';
import { AuthService } from '../../../../core/auth/auth.service';
import { environment } from '../../../../../environments/environment';

interface Reserva {
  total: number;
  fechaReserva: string;
  nombreHotel: string;
  estado: string; // Agregamos el campo estado
}

interface IngresosPorDia {
  total: number;
  hoteles: Record<string, number>;
}

@Component({
  selector: 'app-ingresos',
  standalone: true,
  imports: [CommonModule, BaseChartDirective],
  templateUrl: './revenue.component.html',
  styleUrls: ['./revenue.component.css'],
})
export class IngresosComponent implements OnInit {
  API_URL = `${environment.apiUrl}/admin/reservas`;

  totalIngresos: number = 0;
  totalReservas: number = 0;
  hotelesActivosCount: number = 0;

  // Datos para el gráfico
  public lineChartLabels: string[] = [];
  public lineChartData: ChartData<'line'> = {
    labels: [],
    datasets: [
      {
        label: 'Ingresos por día',
        data: [],
        fill: true,
        tension: 0.4,
        backgroundColor: 'rgba(0, 128, 255, 0.25)',
        borderColor: '#0080ff', // Azul #0080ff para el borde
        pointBackgroundColor: 'rgba(0, 128, 255, 1)',
        pointBorderColor: '#fff',
        pointHoverBackgroundColor: '#fff',
        pointHoverBorderColor: 'rgba(0, 128, 255, 1)',
      },
    ],
  };

  public lineChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      tooltip: {
        callbacks: {
          title: (tooltipItems) => {
            return `Fecha: ${tooltipItems[0].label}`;
          },
          label: (context) => {
            const label = context.dataset.label || '';
            const value = context.parsed.y;
            if (value != null) {
              return `${label}: $${value.toLocaleString()}`;
            }
            return label;
          },
          afterLabel: (context) => {
            const fecha = context.label;
            const datosDia = this.ingresosDetallados[fecha];
            if (datosDia) {
              let tooltipText = `\nDetalle por hotel:`;
              for (const [hotel, ingreso] of Object.entries(datosDia.hoteles)) {
                tooltipText += `\n${hotel}: $${ingreso.toLocaleString()}`;
              }
              return tooltipText;
            }
            return '';
          },
        },
      },
    },
  };

  // Datos detallados por día y hotel
  ingresosDetallados: Record<string, IngresosPorDia> = {};

  constructor(
    private http: HttpClient,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarDatos();
  }

  cargarDatos() {
    const token = this.authService.getToken();
    console.log('📈 [INGRESOS COMPONENT] cargarDatos() ejecutado. Token presente:', !!token);
    if (!token) {
      console.error('❌ [INGRESOS COMPONENT] No se encontró el token de autenticación');
      return;
    }

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });

    console.log('📈 [INGRESOS COMPONENT] Petición GET a:', this.API_URL);
    this.http.get<Reserva[]>(this.API_URL, { headers }).subscribe({
      next: (reservas: Reserva[]) => {
        console.log('✅ [INGRESOS COMPONENT] Reservas recibidas:', reservas?.length);
        // Filtrar solo las reservas confirmadas
        const reservasConfirmadas = (reservas || []).filter(
          (reserva) => reserva.estado && reserva.estado.toUpperCase() === 'CONFIRMADA'
        );

        // KPI
        this.totalIngresos = reservasConfirmadas.reduce((sum, r) => sum + r.total, 0);
        this.totalReservas = reservasConfirmadas.length;
        console.log('📈 [INGRESOS COMPONENT] Total ingresos calculados:', this.totalIngresos, 'Total reservas:', this.totalReservas);

        // Agrupar ingresos por fecha
        const ingresosPorFecha: Record<string, number> = {};
        this.ingresosDetallados = {};
        const uniqueHoteles = new Set<string>();

        reservasConfirmadas.forEach((reserva) => {
          const fecha = reserva.fechaReserva ? reserva.fechaReserva.split('T')[0] : 'Sin fecha';

          // Acumular total por fecha
          ingresosPorFecha[fecha] = (ingresosPorFecha[fecha] || 0) + reserva.total;

          // Acumular detalles por fecha y hotel
          if (!this.ingresosDetallados[fecha]) {
            this.ingresosDetallados[fecha] = {
              total: 0,
              hoteles: {},
            };
          }

          this.ingresosDetallados[fecha].total += reserva.total;

          if (reserva.nombreHotel) {
            uniqueHoteles.add(reserva.nombreHotel);
            if (!this.ingresosDetallados[fecha].hoteles[reserva.nombreHotel]) {
              this.ingresosDetallados[fecha].hoteles[reserva.nombreHotel] = 0;
            }
            this.ingresosDetallados[fecha].hoteles[reserva.nombreHotel] += reserva.total;
          }
        });

        this.hotelesActivosCount = uniqueHoteles.size;
        this.lineChartLabels = Object.keys(ingresosPorFecha);
        this.lineChartData = {
          labels: this.lineChartLabels,
          datasets: [
            {
              ...this.lineChartData.datasets[0],
              data: Object.values(ingresosPorFecha),
            },
          ],
        };
        console.log('✅ [INGRESOS COMPONENT] Gráfico de ingresos actualizado exitosamente.');
      },
      error: (error: any) => {
        console.error('❌ [INGRESOS COMPONENT] Error al cargar datos de reservas:', error);
        if (error.status === 401) {
          console.error('⛔ [INGRESOS COMPONENT] No autorizado: Verifique que el token sea válido');
        } else if (error.status === 403) {
          console.error('⛔ [INGRESOS COMPONENT] Acceso prohibido: El usuario no tiene permisos suficientes');
        }
      },
    });
  }

  // Método para redirigir a la página de reservas
  verTodasLasReservas() {
    this.router.navigate(['/admin/reservas']);
  }
}
