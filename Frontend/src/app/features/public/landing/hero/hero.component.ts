import {
  Component,
  OnInit,
  OnDestroy,
  Output,
  EventEmitter,
  ViewChild,
  ElementRef,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HotelService, Hotel } from '../../hotel.service';

@Component({
  selector: 'app-hero',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './hero.component.html',
  styleUrls: ['./hero.component.css'],
})
export class HeroComponent implements OnInit, OnDestroy {
  @ViewChild('searchContainer', { static: false }) searchContainer!: ElementRef;
  @ViewChild('searchInput', { static: false }) searchInput!: ElementRef;

  // Search functionality
  searchTerm: string = '';
  allHoteles: Hotel[] = [];
  filteredHoteles: Hotel[] = [];
  showDropdown: boolean = false;

  // Dropdown positioning
  dropdownPositionTop: number = 0;
  dropdownPositionLeft: number = 0;
  dropdownWidth: number = 0;

  @Output() hotelesFilterChange = new EventEmitter<Hotel[]>();

  // Temporizador
  private hideDropdownTimeout: any;

  // Type effect properties
  private text = 'GO RESER';
  private typeEffectElement: HTMLElement | null = null;
  private index = 0;
  private isDeleting = false;
  private typeEffectTimeout: any;

  constructor(private hotelService: HotelService, private router: Router) {}

  ngOnInit() {
    this.loadHoteles();

    // Listen for window resize events to reposition dropdown
    if (typeof window !== 'undefined') {
      window.addEventListener('resize', this.onWindowResize.bind(this));
    }

    this.typeEffectElement = document.getElementById('goReser');
    if (this.typeEffectElement) {
      this.typeEffect();
    }
  }

  typeEffect(): void {
    if (!this.typeEffectElement) {
      return;
    }

    const fullText = this.text;
    let timeout = this.isDeleting ? 150 : 200;

    if (this.isDeleting) {
      // Deleting
      this.typeEffectElement.innerHTML = fullText.substring(0, this.index);
      this.index--;
      if (this.index < 0) {
        this.index = 0;
        this.isDeleting = false;
        timeout = 1500; // Pause before typing again
      }
    } else {
      // Typing
      this.typeEffectElement.innerHTML = fullText.substring(0, this.index + 1);
      this.index++;
      if (this.index > fullText.length) {
        this.index = fullText.length;
        this.isDeleting = true;
        timeout = 1500; // Pause before deleting
      }
    }

    this.typeEffectTimeout = setTimeout(() => this.typeEffect(), timeout);
  }

  ngOnDestroy() {
    // Limpiar temporizador
    if (this.hideDropdownTimeout) {
      clearTimeout(this.hideDropdownTimeout);
    }

    // Remove event listener
    if (typeof window !== 'undefined') {
      window.removeEventListener('resize', this.onWindowResize.bind(this));
    }

    if (this.typeEffectTimeout) {
      clearTimeout(this.typeEffectTimeout);
    }
  }

  // Load all hotels for filtering
  loadHoteles(): void {
    this.hotelService.getHoteles().subscribe({
      next: (data: any) => {
        this.allHoteles = data;
        this.filteredHoteles = data;
        this.hotelesFilterChange.emit(this.filteredHoteles);
      },
      error: (err: any) => {
        console.error('Error al cargar hoteles:', err);
      },
    });
  }

  // Filter hotels based on search term (name, city, or department)
  onSearchChange(): void {
    if (!this.searchTerm.trim()) {
      this.filteredHoteles = this.allHoteles;
    } else {
      const term = this.searchTerm.toLowerCase();
      this.filteredHoteles = this.allHoteles.filter((hotel) => this.matchesSearchTerm(hotel, term));
    }

    // Show dropdown only if there are results
    this.showDropdown = this.filteredHoteles.length > 0 && this.searchTerm.trim() !== '';

    // Position dropdown
    if (this.showDropdown) {
      setTimeout(() => {
        this.positionDropdown();
      }, 0);
    }

    this.hotelesFilterChange.emit(this.filteredHoteles);
  }

  // Check if a hotel matches the search term
  matchesSearchTerm(hotel: Hotel, term: string): boolean {
    // Check hotel name
    if (hotel.nombre.toLowerCase().includes(term)) {
      return true;
    }

    // Check city name
    if (hotel.ciudad && hotel.ciudad.nombre.toLowerCase().includes(term)) {
      return true;
    }

    // Check department name
    if (
      hotel.ciudad &&
      hotel.ciudad.departamento &&
      hotel.ciudad.departamento.nombre.toLowerCase().includes(term)
    ) {
      return true;
    }

    return false;
  }

  // Position the dropdown based on the search input
  positionDropdown(): void {
    if (this.searchInput && this.searchInput.nativeElement) {
      const inputRect = this.searchInput.nativeElement.getBoundingClientRect();
      this.dropdownPositionTop = inputRect.bottom + window.scrollY;
      this.dropdownPositionLeft = inputRect.left + window.scrollX;
      this.dropdownWidth = inputRect.width;
    }
  }

  // Handle window resize
  onWindowResize(): void {
    if (this.showDropdown) {
      this.positionDropdown();
    }
  }

  // Hide dropdown with delay to allow clicking on items
  hideDropdownWithDelay(): void {
    this.hideDropdownTimeout = setTimeout(() => {
      this.showDropdown = false;
    }, 200);
  }

  // Select a hotel from the dropdown
  selectHotel(hotel: Hotel): void {
    // Navigate to hotel details or rooms page
    this.router.navigate(['/habitaciones', hotel.id]);

    // Hide dropdown
    this.showDropdown = false;
    this.searchTerm = ''; // Clear search term
  }

  // Get city name for hotel
  getCiudadNombre(hotel: Hotel): string {
    return hotel.ciudad?.nombre || 'No especificado';
  }

  // Get department name for hotel
  getDepartamentoNombre(hotel: Hotel): string {
    return hotel.ciudad?.departamento?.nombre || 'No especificado';
  }

  // Navigate to hotels section
  goToHoteles(): void {
    // Buscar el elemento app-hoteles-carrusel y hacer scroll suave
    const hotelesCarrusel = document.querySelector('app-hoteles-carrusel');
    if (hotelesCarrusel) {
      hotelesCarrusel.scrollIntoView({ 
        behavior: 'smooth',
        block: 'start'
      });
    } else {
      // Fallback: si no se encuentra, navegar a la ruta
      this.router.navigate(['/hoteles']);
    }
  }
}