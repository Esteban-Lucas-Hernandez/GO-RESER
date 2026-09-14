import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-about-section',
  standalone: true,
  imports: [CommonModule],
  templateUrl: '../html/about-section.component.html',
  styleUrls: ['../css/about-section.component.css']
})
export class AboutSectionComponent implements OnInit {
  values = [
    {
      icon: '💡',
      title: 'Innovación',
      description: 'Constantemente evolucionamos para ofrecer la mejor experiencia de reserva.'
    },
    {
      icon: '🤝',
      title: 'Confianza',
      description: 'Transparencia y seguridad en cada transacción.'
    },
    {
      icon: '🌍',
      title: 'Sostenibilidad',
      description: 'Comprometidos con el turismo responsable y sostenible.'
    },
    {
      icon: '⭐',
      title: 'Excelencia',
      description: 'Servicio de calidad superior en cada interacción.'
    }
  ];

  constructor() { }

  ngOnInit(): void {
  }

}