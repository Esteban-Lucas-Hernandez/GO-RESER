# ⚙️ GO RESER - Backend (API REST)

[![Regresar al README Principal](https://img.shields.io/badge/⬅️_Volver-README_Global-gray?style=for-the-badge)](../README.md)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Java 21](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Database](https://img.shields.io/badge/H2_Database-In--Memory_(PostgreSQL_Mode)-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)](#-acceso-a-la-base-de-datos)
[![Swagger](https://img.shields.io/badge/Swagger_UI-OpenAPI_3.0-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)](#-documentación-interactiva-y-pruebas-con-swagger-openapi)


El **Backend** de GO RESER es una API RESTful empresarial desarrollada con **Spring Boot 3** y **Java 21**, protegida con **Spring Security** y autenticación basada en **JWT (JSON Web Tokens)**. Incluye precarga automática de datos para pruebas inmediatas en modo desarrollo.

---

## 🚀 Inicio Rápido del Backend

1. **Clonar o situarse en la carpeta Backend:**
   ```bash
   cd Backend
   ```

2. **Ejecutar el servidor:**
   * En Linux / macOS:
     ```bash
     ./mvnw spring-boot:run
     ```
   * En Windows:
     ```cmd
     mvnw.cmd spring-boot:run
     ```

La API quedará escuchando en: `http://localhost:8080`

---

## 🗄️ Acceso a la Base de Datos (Consola Web)

El proyecto incluye una base de datos en memoria **H2** preconfigurada con compatibilidad PostgreSQL y datos iniciales completos (`data.sql` y `DataInitializer`), lo que permite probar todo el sistema sin necesidad de instalar o configurar servidores locales adicionales.

Para acceder a la consola visual interactiva desde tu navegador:

* **Ruta / URL de la Consola:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
* **Driver Class:** `org.h2.Driver`
* **JDBC URL:** `jdbc:h2:mem:go_reser_db`
* **User Name:** `sa`
* **Password:** *(dejar en blanco / vacío)*

> [!TIP]
> Al presionar **Connect**, podrás consultar directamente todas las tablas del modelo relacional (`users`, `hotel`, `habitacion`, `reserva`, `departamentos`, `ciudades`, etc.) y ejecutar consultas SQL en tiempo real.

### 🐘 ¿Prefieres usar PostgreSQL local en lugar de H2?
En [src/main/resources/application.properties](src/main/resources/application.properties) encontrarás un bloque de configuración listo y comentado para PostgreSQL:
1. Comenta el bloque de H2 y la línea de `data.sql`.
2. Descomenta el bloque de PostgreSQL y coloca tu usuario y contraseña:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/go_reser_db
   spring.datasource.driver-class-name=org.postgresql.Driver
   spring.datasource.username=postgres
   spring.datasource.password=tu_contraseña
   spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
   spring.jpa.hibernate.ddl-auto=update
   ```
3. Crea la base de datos en tu servidor: `CREATE DATABASE go_reser_db;`

---

## 🔐 Integraciones Externas (Google OAuth2 y Envío de Correos)

El backend cuenta con soporte nativo para **Inicio de Sesión con Google** y **Servicio de Correos vía Gmail SMTP**. Se configuran mediante variables de entorno en el sistema o creando un archivo `.env` en la raíz de `Backend/` (puedes guiarte con [.env.example](.env.example)):

### 1. 🔑 Inicio de Sesión con Google (OAuth 2.0)
Permite autenticarse directamente desde el botón *"Iniciar sesión con Google"* en el frontend de Angular.
* **¿Qué necesitas?** Un `Client ID` y `Client Secret` generados en [Google Cloud Console](https://console.cloud.google.com/).
* **Pasos rápidos:**
  1. En Google Cloud Console ve a **APIs & Services > Credentials**.
  2. Crea credenciales de tipo **OAuth 2.0 Client ID** seleccionando **Web application**.
  3. En **URIs de redireccionamiento autorizados** agrega:  
     `http://localhost:8080/login/oauth2/code/google`
  4. Copia el **Client ID** y el **Client Secret**.

### 2. 📧 Envío de Correos (Gmail SMTP)
El servicio [EmailServiceImpl.java](src/main/java/com/example/Back/services/impl/EmailServiceImpl.java) utiliza el servidor SMTP de Gmail (`smtp.gmail.com:587`).
* **¿Qué necesitas?** Tu correo de Gmail y una **Contraseña de Aplicación de 16 caracteres** (Google no permite contraseñas normales por seguridad).
* **Pasos para obtenerla:**
  1. Ingresa a tu cuenta de Google en [myaccount.google.com/security](https://myaccount.google.com/security).
  2. Activa la **Verificación en 2 pasos** (si no la tienes).
  3. Busca **"Contraseñas de aplicaciones"** y crea una (ej: `GoReser`).
  4. Google te generará una clave de 16 letras (ej: `abcd efgh ijkl mnop`).

### 📝 Variables a configurar en tu entorno o `.env`:
```properties
# Google OAuth2
GOOGLE_CLIENT_ID=tu_cliente_id.apps.googleusercontent.com
GOOGLE_CLIENT_SECRET=tu_client_secret
GOOGLE_REDIRECT_URI=http://localhost:8080/login/oauth2/code/google

# Gmail SMTP
SPRING_MAIL_USERNAME=tu_correo@gmail.com
SPRING_MAIL_PASSWORD=tu_contraseña_de_aplicacion_16_letras
```
*(Nota: Si no configuras estas credenciales, el sistema igual arranca y funciona con autenticación tradicional por email/password).*

---

## 🔑 Credenciales de Prueba (Cuentas Demo)

Para facilitar la evaluación y pruebas del sistema, la aplicación inicializa automáticamente 3 usuarios con diferentes niveles de privilegios:

| Rol del Sistema | Nombre de Usuario | Correo Electrónico (Login) | Contraseña | Permisos y Alcance |
| :--- | :--- | :--- | :--- | :--- |
| **👤 Cliente (`ROLE_USER`)** | Lucas Hernández | `cliente@gmail.com` | `123456` | Explorar hoteles públicos, buscar disponibilidad, crear reservas y publicar reseñas. |
| **🏨 Admin Hotel (`ROLE_ADMIN`)** | Administrador Principal | `admin@admin.com` | `admin123` | Gestión de su hotel, administración de habitaciones, tarifas, imágenes y solicitudes de reserva. |
| **👑 SuperAdmin (`ROLE_SUPERADMIN`)** | Super Administrador | `superadmin@admin.com` | `superadmin123` | Panel de control global, alta de hoteles, asignación de administradores, gestión de usuarios y reportes. |

---

## 📑 Documentación Interactiva y Pruebas con Swagger (OpenAPI)

La API cuenta con **Springdoc OpenAPI / Swagger UI** integrado. Permite explorar visualmente todos los endpoints, ver modelos de datos (DTOs) y ejecutar peticiones directamente desde el navegador:

* 🌐 **Swagger UI (Consola Interactiva):** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
* 📄 **OpenAPI Spec (JSON):** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

### 💡 ¿Cómo probar endpoints protegidos en Swagger?
1. En la sección **Auth**, ejecuta el endpoint `POST /auth/login` con cualquiera de las [Credenciales Demo](#-credenciales-de-prueba-cuentas-demo) (por ejemplo `admin@admin.com` y `admin123`).
2. Copia el `token` devuelto en la respuesta JSON.
3. Haz clic en el botón verde **Authorize 🔓** ubicado arriba a la derecha en Swagger.
4. En el campo **Value**, pega el token JWT y pulsa **Authorize**.
5. Ahora podrás presionar **"Try it out"** y ejecutar cualquier endpoint con los permisos correspondientes.

---

## 📌 Principales Endpoints API

| Módulo | Método | Endpoint | Descripción | Rol / Acceso |
| :--- | :--- | :--- | :--- | :--- |
| **Auth** | `POST` | `/auth/login` | Autenticación y generación de JWT | Público (`PermitAll`) |
| **Auth** | `POST` | `/auth/registro` | Registro de nuevos clientes | Público (`PermitAll`) |
| **Auth** | `POST` | `/auth/google` | Inicio de sesión con Google OAuth2 | Público (`PermitAll`) |
| **Catálogo** | `GET` | `/public/hoteles` | Lista pública y búsqueda de hoteles | Público (`PermitAll`) |
| **Reservas** | `POST` | `/user/reservas/habitacion/{id}` | Creación de reserva de habitación | `ROLE_USER` |
| **Hoteles** | `POST` | `/admin/hoteles` | Creación y administración de hoteles | `ROLE_ADMIN` / `SUPERADMIN` |

> 📖 **Documentación Técnica Completa:**  
> - Para revisar todas las rutas y contratos REST: [Catálogo Completo de Endpoints (docs/API_ENDPOINTS.md)](docs/API_ENDPOINTS.md)  
> - Para comprender el diseño en capas y patrones: [Arquitectura del Sistema (docs/ARCHITECTURE.md)](docs/ARCHITECTURE.md)

---

## 📂 Estructura del Código Fuente

```text
Backend/src/main/java/com/example/Back/
├── ⚙️ Config/          # Configuración de Seguridad, CORS, Beans y DataInitializer
├── 🎮 Controllers/     # Controladores REST (/auth, /public, /user, /admin, etc.)
├── 🔒 Security/        # Filtros JWT, AuthenticationManager y UserDetailsService
├── 📦 dto/             # Data Transfer Objects para requests y responses
├── 🔀 enums/           # Enumeradores de estado (Roles, EstadoReserva, EstadoHabitacion)
├── ⚠️ exceptions/      # Manejo global de excepciones (@ControllerAdvice)
├── 🔄 mapper/          # Mapeadores entre entidades y DTOs
├── 🗄️ models/          # Entidades JPA relacionales
├── 📁 repo/            # Interfaces de persistencia Spring Data JPA
├── 🧠 services/        # Servicios con lógica de negocio y transacciones
├── 🔍 specification/   # Filtros y especificaciones de búsqueda dinámica JPA
└── 🛠️ validation/      # Validadores personalizados
```

---

[⬅️ Regresar al README Principal](../README.md)