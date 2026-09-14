import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HotelesCarruselComponent } from '../../hoteles-carrusel/ts/hoteles-carrusel.component';
import { NavComponent } from '../../nav/ts/nav.component';
import { HeroComponent } from '../../hero/ts/hero.component';
import { InformacionExtraComponent } from '../../informacion-extra/ts/informacion-extra.component';
import { FooterComponent } from '../../footer/ts/footer.component';
import { FeaturesSectionComponent } from '../../features-section/ts/features-section.component';
import { HotelService } from '../../hotel.service';
import { AboutSectionComponent } from '../../about-section/ts/about-section.component';
import { TestimonialsSectionComponent } from '../../testimonials-section/ts/testimonials-section.component';
import { NewsletterSectionComponent } from '../../newsletter-section/ts/newsletter-section.component';

@Component({
  selector: 'app-public',
  standalone: true,
  imports: [
    CommonModule,
    HotelesCarruselComponent,
    NavComponent,
    HeroComponent,
    InformacionExtraComponent,
    FooterComponent,
    FeaturesSectionComponent,
    AboutSectionComponent,
    TestimonialsSectionComponent,
    NewsletterSectionComponent,
  ],
  providers: [HotelService],
  templateUrl: '../html/public.component.html',
  styleUrls: ['../css/public.component.css'],
})
export class PublicComponent {
  constructor() {}
}