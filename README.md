# 🏨 GO RESER - Sistema Integral de Gestión Hotelera

[![Backend Docs](https://img.shields.io/badge/Documentación-Backend-007396?style=for-the-badge&logo=spring&logoColor=white)](Backend/README.md)
[![Frontend Docs](https://img.shields.io/badge/Documentación-Frontend-DD0031?style=for-the-badge&logo=angular&logoColor=white)](Frontend/README.md)
[![Swagger UI](https://img.shields.io/badge/Swagger-API_Docs-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)](http://localhost:8080/swagger-ui/index.html)
[![Database](https://img.shields.io/badge/H2_Database-In--Memory-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)](#-roles-permisos-y-credenciales-de-prueba-demo)

**GO RESER** es una plataforma web full-stack de alto rendimiento diseñada para la gestión completa de reservas, administración de hoteles, habitaciones y control de usuarios según sus roles (Cliente, Admin de Hotel y SuperAdmin).

---

## 🚀 Inicio Rápido

### Requisitos Previos
- **Java JDK**: 17 o superior
- **Node.js**: 18.x / 20.x / 22.x
- **PostgreSQL**: 15 o superior

### 1. Iniciar Backend
```bash
cd Backend
./mvnw spring-boot:run
```
> [👉 Ver guía detallada de configuración y estructura del Backend](Backend/README.md)

### 2. Iniciar Frontend
```bash
cd Frontend
npm install
npm run dev
```
> [👉 Ver guía detallada de componentes y arquitectura del Frontend](Frontend/README.md)

---

## 🛠️ Stack Tecnológico

| Capa | Tecnología | Descripción |
| :--- | :--- | :--- |
| **Backend** | Spring Boot 3, Java 17/21 | API RESTful con Spring Security y JWT |
| **Base de Datos** | PostgreSQL, Spring Data JPA | Persistencia relacional optimizada |
| **Frontend** | Angular (Standalone), TypeScript | Arquitectura modular basada en características (`features`) |
| **Estilos** | CSS3, FontAwesome, SweetAlert2 | UI responsiva y moderna |

---

## 📁 Estructura del Proyecto

```text
GO_RESER/
├── 📂 Backend/     # API REST en Spring Boot, Controladores, Entidades y Servicios
└── 📂 Frontend/    # Aplicación Angular (Core, Shared, Features)
```

- 📖 Para ver el detalle técnico del Backend: [Ir a Backend/README.md](Backend/README.md)
- 📖 Para ver el detalle técnico del Frontend: [Ir a Frontend/README.md](Frontend/README.md)

---

## 🔒 Roles, Permisos y Credenciales de Prueba (Demo)

El sistema viene con cuentas precargadas en base de datos para facilitar la evaluación inmediata de cada rol:

| Rol | Correo (Usuario) | Contraseña | Alcance y Permisos |
| :--- | :--- | :--- | :--- |
| **👤 ROLE_USER (Cliente)** | `cliente@gmail.com` | `123456` | Explorar catálogo, reservar habitaciones y dejar reseñas. |
| **🏨 ROLE_ADMIN (Hotel)** | `admin@admin.com` | `admin123` | Administrar habitaciones, tarifas, fotos y reservas del hotel. |
| **👑 ROLE_SUPERADMIN** | `superadmin@admin.com` | `superadmin123` | Control total del sistema, hoteles, usuarios y reportes. |

> 📑 **Documentación y pruebas de endpoints (Swagger):** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)  
> 🗄️ **Base de datos interactiva (Consola H2):** Puedes explorar los datos y tablas en vivo en [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL: `jdbc:h2:mem:go_reser_db`, Usuario: `sa`, sin contraseña).  
> 📖 Para más detalles de configuración y endpoints, consulta el [README del Backend](Backend/README.md).


