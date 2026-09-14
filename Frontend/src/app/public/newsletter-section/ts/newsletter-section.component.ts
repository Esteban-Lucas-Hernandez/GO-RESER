import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-newsletter-section',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: '../html/newsletter-section.component.html',
  styleUrls: ['../css/newsletter-section.component.css']
})
export class NewsletterSectionComponent implements OnInit {
  email: string = '';
  isSubscribed: boolean = false;
  isLoading: boolean = false;

  constructor() { }

  ngOnInit(): void {
  }

  subscribeToNewsletter() {
    if (!this.isValidEmail(this.email)) {
      alert('Por favor, ingresa un email válido');
      return;
    }

    this.isLoading = true;
    
    // Simular llamada a API
    setTimeout(() => {
      this.isLoading = false;
      this.isSubscribed = true;
      this.email = '';
      
      // Mostrar mensaje de éxito
      setTimeout(() => {
        this.isSubscribed = false;
      }, 3000);
    }, 1500);
  }

  isValidEmail(email: string): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }

}