import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HotelesCarruselComponent } from '../hotels/hotels-carousel/hotels-carousel.component';
import { NavComponent } from '../../../shared/components/nav/nav.component';
import { HeroComponent } from './hero/hero.component';
import { InformacionExtraComponent } from './extra-info/extra-info.component';
import { FooterComponent } from '../../../shared/components/footer/footer.component';
import { FeaturesSectionComponent } from './features-section/features-section.component';
import { HotelService } from '../hotel.service';
import { AboutSectionComponent } from './about-section/about-section.component';
import { TestimonialsSectionComponent } from './testimonials-section/testimonials-section.component';
import { NewsletterSectionComponent } from './newsletter-section/newsletter-section.component';

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
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.css'],
})
export class PublicComponent {
  constructor() {}
}