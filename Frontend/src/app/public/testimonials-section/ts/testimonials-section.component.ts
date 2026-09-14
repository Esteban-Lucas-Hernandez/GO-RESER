import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-testimonials-section',
  standalone: true,
  imports: [CommonModule],
  templateUrl: '../html/testimonials-section.component.html',
  styleUrls: ['../css/testimonials-section.component.css']
})
export class TestimonialsSectionComponent implements OnInit {
  testimonials = [
    {
      name: 'Ana Martínez',
      role: 'Viajera Frecuente',
      image: 'https://randomuser.me/api/portraits/women/44.jpg',
      rating: 5,
      text: 'Excelente servicio! Encontré el hotel perfecto para mis vacaciones en la playa. El proceso de reserva fue súper fácil y el precio inmejorable.',
      location: 'Madrid, España'
    },
    {
      name: 'Carlos López',
      role: 'Empresario',
      image: 'https://randomuser.me/api/portraits/men/32.jpg',
      rating: 5,
      text: 'Uso Go Reser para todos mis viajes de negocios. La variedad de opciones y la calidad del servicio es impresionante. Altamente recomendado.',
      location: 'Barcelona, España'
    },
    {
      name: 'Laura García',
      role: 'Influencer de Viajes',
      image: 'https://randomuser.me/api/portraits/women/68.jpg',
      rating: 5,
      text: 'Descubrí lugares increíbles gracias a Go Reser. La interfaz es intuitiva y las opciones de filtros me ayudan a encontrar exactamente lo que busco.',
      location: 'Valencia, España'
    }
  ];

  currentTestimonial = 0;

  constructor() { }

  ngOnInit(): void {
    this.startAutoSlide();
  }

  startAutoSlide() {
    setInterval(() => {
      this.nextTestimonial();
    }, 5000);
  }

  nextTestimonial() {
    this.currentTestimonial = (this.currentTestimonial + 1) % this.testimonials.length;
  }

  prevTestimonial() {
    this.currentTestimonial = this.currentTestimonial === 0 
      ? this.testimonials.length - 1 
      : this.currentTestimonial - 1;
  }

  selectTestimonial(index: number) {
    this.currentTestimonial = index;
  }

  getStars(rating: number): number[] {
    return Array(rating).fill(0);
  }

}