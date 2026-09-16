# 🎨 GO RESER - Frontend (Angular Standalone)

[![Regresar al README Principal](https://img.shields.io/badge/⬅️_Volver-README_Global-gray?style=for-the-badge)](../README.md)
[![Angular](https://img.shields.io/badge/Angular-19-DD0031?style=for-the-badge&logo=angular&logoColor=white)](https://angular.dev/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.x-3178C6?style=for-the-badge&logo=typescript&logoColor=white)](https://www.typescriptlang.org/)

El **Frontend** de GO RESER está construido sobre **Angular** haciendo uso de la arquitectura basada en **Componentes Standalone** y organizado bajo la estructura de diseño orientada a características (`features`).

---

## 📂 Estructura Detallada del Frontend

```text
Frontend/src/app/
├── 🛡️ core/                  # Infraestructura central y singleton
│   ├── 🔐 auth/              # Servicio de Autenticación, Interceptor JWT, Utils
│   ├── 🚪 guards/            # AuthGuard, AdminGuard, SuperAdminGuard
│   └── 📄 models/            # DTOs y modelos globales (User, Profile)
│
├── 🧩 shared/                # Componentes reutilizables en toda la app
│   └── 🎨 components/        # Navbar unificado, Nav, Footer, DropdownMenu
│
└── 🌟 features/              # Módulos funcionales divididos por dominio
    ├── 🔑 auth/              # Login, Registro y Google Callback
    ├── 🌐 public/            # Landing Page, Listado de Hoteles, Reservas del cliente
    ├── 🛠️ admin/             # Dashboard de Hotelero, Habitaciones, Categorías
    └── 👑 superadmin/        # Control global de Usuarios, Hoteles y Reportes
```

---

## 🛠️ Comandos de Desarrollo

### Instalar Dependencias
```bash
npm install
```

### Iniciar Servidor de Desarrollo
```bash
npm run dev
```
Accede en tu navegador a `http://localhost:4200/`.

### Compilación de Producción
```bash
npm run build
```
Genera los archivos optimizados en la carpeta `dist/Front`.

---

## 🔒 Manejo de Rutas y Guards

- **`/public`**: Rutas de libre acceso para clientes y usuarios anónimos.
- **`/admin`**: Protegido por `AdminGuard` (Acceso exclusivo `ROLE_ADMIN`).
- **`/superadmin`**: Protegido por `SuperAdminGuard` (Acceso exclusivo `ROLE_SUPERADMIN`).

---

[⬅️ Regresar al README Principal](../README.md)
