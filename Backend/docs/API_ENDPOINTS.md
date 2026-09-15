# GoReser REST API Endpoints Specification

This document details the complete REST API catalog for **GoReser**, grouped by controller access tier and responsibility.

---

## 1. Authentication & Identity (`/auth`)

| Method | Endpoint | Description | Role / Access |
| :--- | :--- | :--- | :--- |
| `POST` | `/auth/login` | Authenticate user with credentials & return JWT | `PermitAll` |
| `POST` | `/auth/registro` | Register a new customer account | `PermitAll` |
| `POST` | `/auth/google` | Authenticate via Google ID Token | `PermitAll` |
| `GET` | `/auth/oauth2/success` | OAuth2 callback redirect handler | `PermitAll` |

---

## 2. Public Catalog (`/public`)

| Method | Endpoint | Description | Role / Access |
| :--- | :--- | :--- | :--- |
| `GET` | `/public/hoteles` | List all available hotels with city info & ratings | `PermitAll` |
| `GET` | `/public/hoteles/{id}` | Get hotel details & room types | `PermitAll` |
| `GET` | `/public/hoteles/{id}/habitaciones` | List rooms for a specific hotel | `PermitAll` |
| `GET` | `/public/habitaciones/{id}` | Get detailed room specifications | `PermitAll` |
| `GET` | `/public/resenas/hotel/{hotelId}` | Get all customer reviews for a hotel | `PermitAll` |

---

## 3. Customer Operations (`/user`)

| Method | Endpoint | Description | Role / Access |
| :--- | :--- | :--- | :--- |
| `GET` | `/user/perfil` | Retrieve current authenticated user profile | `ROLE_USER` |
| `PUT` | `/user/perfil` | Update personal profile details & avatar | `ROLE_USER` |
| `POST` | `/user/reservas` | Create a new hotel room booking | `ROLE_USER` |
| `GET` | `/user/reservas` | List all bookings made by the authenticated user | `ROLE_USER` |
| `GET` | `/user/reservas/{id}` | Get specific booking status & breakdown | `ROLE_USER` |
| `POST` | `/user/pagos` | Submit payment processing for a booking | `ROLE_USER` |
| `GET` | `/user/pagos/{id}` | Check payment transaction status & receipt | `ROLE_USER` |
| `POST` | `/user/resenas` | Submit a hotel review and rating (1-5 stars) | `ROLE_USER` |

---

## 4. Hotel Administration (`/admin`)

| Method | Endpoint | Description | Role / Access |
| :--- | :--- | :--- | :--- |
| `GET` | `/admin/panel/stats` | Retrieve hotel dashboard statistics & KPI | `ROLE_ADMIN` |
| `GET` | `/admin/hoteles` | List hotels managed by the administrator | `ROLE_ADMIN` |
| `POST` | `/admin/hoteles` | Register a new hotel establishment | `ROLE_ADMIN` |
| `PUT` | `/admin/hoteles/{id}` | Update hotel details & policies | `ROLE_ADMIN` |
| `DELETE` | `/admin/hoteles/{id}` | Deactivate a hotel | `ROLE_ADMIN` |
| `GET` | `/admin/habitaciones` | List inventory across admin's hotels | `ROLE_ADMIN` |
| `POST` | `/admin/habitaciones` | Add a new room to inventory | `ROLE_ADMIN` |
| `PUT` | `/admin/habitaciones/{id}` | Update room details, capacity, and rate | `ROLE_ADMIN` |
| `DELETE` | `/admin/habitaciones/{id}` | Remove a room from inventory | `ROLE_ADMIN` |
| `POST` | `/admin/habitaciones/{id}/imagenes` | Upload gallery images for room | `ROLE_ADMIN` |
| `DELETE` | `/admin/imagenes/{id}` | Remove image from gallery | `ROLE_ADMIN` |
| `GET` | `/admin/categorias` | List custom room categories | `ROLE_ADMIN` |
| `POST` | `/admin/categorias` | Create a new room category | `ROLE_ADMIN` |
| `GET` | `/admin/reservas` | Manage bookings for managed hotels | `ROLE_ADMIN` |
| `PUT` | `/admin/reservas/{id}/estado` | Update reservation status | `ROLE_ADMIN` |
| `GET` | `/admin/resenas` | Review moderation & feedback | `ROLE_ADMIN` |
| `GET` | `/admin/departamentos` | Reference list of states & cities | `ROLE_ADMIN` |

---

## 5. Super Administrator (`/superadmin`)

| Method | Endpoint | Description | Role / Access |
| :--- | :--- | :--- | :--- |
| `GET` | `/superadmin/usuarios` | Global listing of all system users & roles | `ROLE_SUPERADMIN` |
| `PUT` | `/superadmin/usuarios/{id}/rol` | Update user privileges / role promotion | `ROLE_SUPERADMIN` |
| `PUT` | `/superadmin/usuarios/{id}/estado` | Enable / disable user accounts | `ROLE_SUPERADMIN` |
| `GET` | `/superadmin/hoteles` | System-wide view of all registered hotels | `ROLE_SUPERADMIN` |
| `GET` | `/superadmin/habitaciones` | System-wide room inventory oversight | `ROLE_SUPERADMIN` |
| `GET` | `/superadmin/reservas` | Global audit log of all bookings & revenue | `ROLE_SUPERADMIN` |
| `GET` | `/superadmin/perfil` | SuperAdmin profile management | `ROLE_SUPERADMIN` |
