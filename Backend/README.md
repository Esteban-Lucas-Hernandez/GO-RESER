# ⚙️ GO RESER - Backend (API REST)

[![Regresar al README Principal](https://img.shields.io/badge/⬅️_Volver-README_Global-gray?style=for-the-badge)](../README.md)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15%2B-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)

El **Backend** de GO RESER es una API RESTful desarrollada con **Spring Boot 3** y **Java 17/21**, respaldada por una base de datos **PostgreSQL** y protegida por autenticación basada en **JWT (JSON Web Tokens)**.

---

## 📂 Estructura Detallada del Backend

```text
Backend/src/main/java/com/example/Back/
├── ⚙️ Config/          # Configuraciones generales (CORS, Swagger, Beans)
├── 🎮 Controllers/     # Endpoints REST expuestos para Auth, Hoteles, Reservas, etc.
├── 🔒 Security/        # Filtros JWT, Spring Security y detalles de autenticación
├── 📦 dto/             # Objetos de Transferencia de Datos (DTOs)
├── 🔀 enums/           # Enumeraciones (Roles, EstadoReserva, EstadoHabitacion)
├── ⚠️ exceptions/      # Control de excepciones globales (@ControllerAdvice)
├── 🔄 mapper/          # Mappers entre Entidades JPA y DTOs
├── 🗄️ models/          # Entidades persistentes de PostgreSQL
├── 📁 repo/            # Repositorios Spring Data JPA
├── 🧠 services/        # Capa de lógica de negocio y reglas del sistema
├── 🔍 specification/   # Criterios de filtrado y búsqueda dinámicos
└── 🛠️ validation/      # Anotaciones y validadores personalizados
```

---

## 🛠️ Configuración e Instalación

### 1. Base de Datos PostgreSQL
Crea la base de datos en tu servidor local PostgreSQL:
```sql
CREATE DATABASE go_reser_db;
```

### 2. Variables de Configuración (`src/main/resources/application.properties`)
Asegúrate de ajustar los parámetros de conexión:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/go_reser_db
spring.datasource.username=postgres
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update
jwt.secret=tu_clave_secreta_jwt
```

### 3. Ejecución del Servidor
```bash
./mvnw spring-boot:run
```
La API estará escuchando en `http://localhost:8080`.

---

## 📌 Principales Endpoints API

| Módulo | Método | Endpoint | Descripción |
| :--- | :--- | :--- | :--- |
| **Auth** | `POST` | `/auth/login` | Inicio de sesión y generación de JWT |
| **Auth** | `POST` | `/auth/registro` | Registro de nuevos clientes |
| **Hoteles** | `GET` | `/public/hoteles` | Lista pública de hoteles disponibles |
| **Reservas** | `POST` | `/user/reservas/habitacion/{id}` | Crear reserva de habitación |
| **Admin** | `POST` | `/admin/hoteles` | Creación y actualización de hoteles |

---

[⬅️ Regresar al README Principal](../README.md)