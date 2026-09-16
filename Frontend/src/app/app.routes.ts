import { Routes } from '@angular/router';
import { PublicComponent } from './features/public/landing/landing.component';
import { LoginComponent } from './features/auth/login/login.component';
import { RegistroComponent } from './features/auth/register/register.component';
import { AuthGuard } from './core/guards/auth.guard';
import { SuperAdminGuard } from './core/guards/superadmin.guard';
import { AdminGuard } from './core/guards/admin.guard';
import { PanelComponent } from './features/admin/dashboard/dashboard.component';
import { HotelesComponent } from './features/admin/hotels/hotels.component';
import { ListarCategoriaComponent } from './features/admin/category/list/listar-categoria.component';
import { CrearCategoriaComponent } from './features/admin/category/create/crear-categoria.component';
import { EditarCategoriaComponent } from './features/admin/category/edit/editar-categoria.component';
import { ListarHabitacionComponent } from './features/admin/room/list/listar-habitacion.component';
import { CrearHabitacionComponent } from './features/admin/room/create/crear-habitacion.component';
import { EditarHabitacionComponent } from './features/admin/room/edit/editar-habitacion.component';
import { ListarHabitacionesComponent } from './features/admin/room/list-rooms/listar-habitaciones.component';
import { AdminComponent } from './features/admin/admin.component';
import { SuperAdminComponent } from './features/superadmin/users/users.component';
import { SuperAdminContainerComponent } from './features/superadmin/superadmin.component';
import { SuperAdminPerfilComponent } from './features/superadmin/profile/profile.component';
import { SuperAdminHotelesComponent } from './features/superadmin/hotels/hotels.component';
import { SuperAdminHabitacionesComponent } from './features/superadmin/rooms/rooms.component';
import { SuperAdminReservasComponent } from './features/superadmin/bookings/bookings.component';
import { HabitacionesComponent } from './features/public/hotels/rooms/rooms.component';
import { DetalleHabitacionComponent } from './features/public/hotels/room-detail/room-detail.component';
import { MisReservasComponent } from './features/public/bookings/my-bookings/my-bookings.component';
import { PerfilComponent } from './features/admin/profile/profile.component';
import { PerfilComponent as PublicPerfilComponent } from './features/public/profile/profile.component';
import { ReservasComponent } from './features/admin/bookings/bookings.component';
import { ResenasComponent } from './features/admin/reviews/reviews.component';
import { GoogleCallbackComponent } from './features/auth/google-callback/google-callback.component';

export const routes: Routes = [
  { path: '', redirectTo: 'public', pathMatch: 'full' },
  { path: 'public', component: PublicComponent },
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: RegistroComponent },
  { path: 'auth/google/callback', component: GoogleCallbackComponent },
  { path: 'habitaciones/:id', component: HabitacionesComponent },
  { path: 'detalle-habitacion/:hotelId/:id', component: DetalleHabitacionComponent },
  { path: 'mis-reservas', component: MisReservasComponent, canActivate: [AuthGuard] },
  { path: 'perfil', component: PublicPerfilComponent, canActivate: [AuthGuard] },
  {
    path: 'admin',
    component: AdminComponent,
    canActivate: [AdminGuard],
    children: [
      { path: 'panel', component: PanelComponent },
      { path: 'dashboard', redirectTo: 'panel', pathMatch: 'full' },
      { path: 'hoteles', component: HotelesComponent },
      { path: 'categoria/listar', component: ListarCategoriaComponent },
      { path: 'categoria/crear', component: CrearCategoriaComponent },
      { path: 'categoria/editar/:id', component: EditarCategoriaComponent },
      { path: 'habitacion/listar/:hotelId', component: ListarHabitacionComponent },
      { path: 'habitacion/crear/:hotelId', component: CrearHabitacionComponent },
      { path: 'habitacion/editar/:hotelId/:id', component: EditarHabitacionComponent },
      {
        path: 'habitacion/imagenes/:hotelId/:habitacionId',
        component: ListarHabitacionesComponent,
      },
      { path: 'reservas', component: ReservasComponent },
      { path: 'resenas', component: ResenasComponent },
      { path: 'perfil', component: PerfilComponent },
      { path: '', redirectTo: 'panel', pathMatch: 'full' },
    ],
  },
  {
    path: 'superadmin',
    component: SuperAdminContainerComponent,
    canActivate: [SuperAdminGuard],
    children: [
      { path: 'usuarios', component: SuperAdminComponent },
      { path: 'hoteles', component: SuperAdminHotelesComponent },
      { path: 'habitaciones', component: SuperAdminHabitacionesComponent },
      { path: 'reservas', component: SuperAdminReservasComponent },
      { path: 'perfil', component: SuperAdminPerfilComponent },
      { path: '', redirectTo: 'usuarios', pathMatch: 'full' },
    ],
  },
  { path: '**', redirectTo: 'public' },
];
