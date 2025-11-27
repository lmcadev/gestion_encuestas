# Sistema de Gestión de Encuestas

Sistema completo de gestión de encuestas de satisfacción desarrollado con Spring Boot y React. Permite a empresas crear y administrar encuestas para productos, mientras que los clientes pueden responderlas de forma pública y sencilla.

## Descripción del Proyecto

Este proyecto académico implementa un sistema de encuestas que combina:

- **Backend robusto**: API REST con Spring Boot, autenticación JWT y persistencia en PostgreSQL
- **Frontend moderno**: Interfaz React con Vite, Tailwind CSS y diseño responsivo
- **Arquitectura contenerizada**: Despliegue completo con Docker Compose
- **Seguridad**: Autenticación JWT, protección de endpoints y sanitización de datos

## Características Principales

### Módulo de Administración

- Gestión completa de productos y servicios
- Creación y configuración de encuestas personalizadas
- Diseño de preguntas con múltiples tipos:
  - Calificación por estrellas (1-5)
  - Opción única (radio buttons)
  - Respuesta abierta (texto libre)
- Dashboard con estadísticas en tiempo real
- Análisis de respuestas de clientes
- Gestión de usuarios administrativos
- Control de empresas

### Módulo Público

- Interfaz pública para responder encuestas
- Selección dinámica de producto
- Calificación intuitiva con sistema de estrellas
- Validación de campos obligatorios
- Confirmación de envío exitoso

## Tecnologías Utilizadas

### Backend

- Java 17
- Spring Boot 3.1.5
- Spring Security con JWT
- Spring Data JPA / Hibernate
- PostgreSQL 15
- Maven
- OpenAPI/Swagger
- Docker

### Frontend

- React 18
- Vite
- React Router DOM
- Tailwind CSS
- Axios
- Jest (Testing)
- Nginx (Producción)

## Estructura del Proyecto

```
gestion_encuestas/
├── backend/                    # API REST Spring Boot
│   ├── src/
│   │   ├── main/java/com/example/app/
│   │   │   ├── config/        # Configuración general
│   │   │   ├── controller/    # Controladores REST
│   │   │   ├── dto/           # Data Transfer Objects
│   │   │   ├── exception/     # Manejo de excepciones
│   │   │   ├── model/         # Entidades JPA
│   │   │   ├── repository/    # Repositorios
│   │   │   ├── security/      # JWT y seguridad
│   │   │   └── service/       # Lógica de negocio
│   │   └── resources/
│   │       └── application.properties
│   ├── pom.xml
│   ├── Dockerfile
│   └── README.md
├── frontend/                   # Aplicación React
│   ├── src/
│   │   ├── components/        # Componentes reutilizables
│   │   ├── pages/            # Páginas
│   │   │   ├── dashboard/    # Vistas administrativas
│   │   │   ├── Home.jsx
│   │   │   ├── Login.jsx
│   │   │   └── Survey.jsx
│   │   ├── utils/            # Utilidades
│   │   ├── App.jsx
│   │   └── main.jsx
│   ├── package.json
│   ├── Dockerfile
│   └── README.md
├── docker-compose.yml         # Orquestación de servicios
├── .env.example              # Template de variables de entorno
├── .gitignore
└── README.md
```

## Arquitectura del Sistema

### Modelo de Datos

El sistema se basa en las siguientes entidades principales:

- **User**: Usuarios administrativos con autenticación
- **Empresa**: Empresas que crean encuestas
- **Producto**: Productos o servicios a evaluar
- **Encuesta**: Encuestas asociadas a productos
- **Pregunta**: Preguntas con diferentes tipos
- **OpcionRespuesta**: Opciones para preguntas de selección
- **ClienteResponde**: Clientes que responden encuestas
- **RespuestaEncuesta**: Respuestas registradas

### Flujo de Datos

1. Administrador crea productos y encuestas asociadas
2. Se definen preguntas con sus tipos y opciones
3. Cliente accede a la encuesta pública
4. Selecciona producto y responde preguntas
5. Sistema registra cliente y respuestas
6. Administrador visualiza resultados y estadísticas

## Instalación y Configuración

### Requisitos Previos

- Docker y Docker Compose
- Git
- (Opcional) Java 17 y Node.js 18+ para desarrollo local

### Configuración de Variables de Entorno

1. Copiar el archivo de ejemplo:

```bash
cp .env.example .env
```

2. Editar `.env` con valores seguros:

```env
# Base de Datos
DB_URL=jdbc:postgresql://db:5432/monorepo
DB_USERNAME=postgres
DB_PASSWORD=tu_password_seguro

# JWT
JWT_SECRET=tu_secreto_jwt_de_al_menos_32_caracteres
JWT_EXPIRATION=3600000

# PostgreSQL Docker
POSTGRES_DB=monorepo
POSTGRES_USER=postgres
POSTGRES_PASSWORD=tu_password_seguro
```

### Ejecución con Docker

```bash
# Clonar repositorio
git clone https://github.com/lmcadev/gestion_encuestas.git
cd gestion_encuestas

# Crear archivo .env con credenciales
cp .env.example .env
# Editar .env con tus valores

# Iniciar todos los servicios
docker-compose up -d --build

# Verificar estado de contenedores
docker-compose ps

# Ver logs
docker-compose logs -f
```

### Acceso a la Aplicación

- **Frontend**: http://localhost:5173
- **Backend API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **PostgreSQL**: localhost:5433

### Credenciales por Defecto

Usuario administrador inicial:
- **Usuario**: admin
- **Contraseña**: admin123

**Importante**: Cambiar estas credenciales en producción.

## Desarrollo Local

### Backend

```bash
cd backend

# Instalar dependencias
mvn clean install

# Ejecutar en modo desarrollo
mvn spring-boot:run

# Ejecutar tests
mvn test
```

### Frontend

```bash
cd frontend

# Instalar dependencias
npm install

# Ejecutar en modo desarrollo
npm run dev

# Ejecutar tests
npm test

# Build para producción
npm run build
```

## API Endpoints

### Autenticación

```
POST /api/auth/login          # Iniciar sesión
POST /api/auth/register       # Registrar usuario
```

### Endpoints Públicos (sin autenticación)

```
GET  /api/productos                      # Listar productos
GET  /api/encuestas/{id}/completa       # Encuesta completa
GET  /api/encuestas/producto/{id}       # Encuestas por producto
POST /api/clientes                       # Registrar cliente
POST /api/respuestas                     # Enviar respuesta
```

### Endpoints Protegidos (requieren JWT)

```
# Gestión de Productos
GET    /api/productos
POST   /api/productos
PUT    /api/productos/{id}
DELETE /api/productos/{id}

# Gestión de Encuestas
GET    /api/encuestas
POST   /api/encuestas
PUT    /api/encuestas/{id}
DELETE /api/encuestas/{id}

# Gestión de Preguntas
GET    /api/preguntas
POST   /api/preguntas
PUT    /api/preguntas/{id}
DELETE /api/preguntas/{id}

# Otros módulos
GET    /api/usuarios
GET    /api/empresas
GET    /api/respuestas/encuesta/{id}
```

Documentación completa en: http://localhost:8080/swagger-ui.html

## Seguridad

### Medidas Implementadas

- Autenticación JWT con tokens de expiración configurable
- Protección de endpoints administrativos
- Sanitización de entradas para prevenir XSS
- Validación de datos en backend y frontend
- CORS configurado para desarrollo y producción
- Passwords hasheados con BCrypt
- Variables de entorno para credenciales sensibles
- Headers de seguridad HTTP

### Buenas Prácticas

- No exponer stack traces en producción
- Tokens JWT con secreto fuerte (32+ caracteres)
- Cambiar credenciales por defecto
- Usar HTTPS en producción
- Mantener dependencias actualizadas

## Testing

### Backend Tests

```bash
cd backend
mvn test
mvn test jacoco:report  # Con cobertura
```

### Frontend Tests

```bash
cd frontend
npm test
npm run test:coverage
```

## Despliegue

### Con Docker Compose (Recomendado)

```bash
docker-compose up -d --build
```

### Despliegue Manual

1. **Base de Datos**: PostgreSQL configurado con credenciales
2. **Backend**: JAR ejecutable con variables de entorno
3. **Frontend**: Build estático servido por Nginx

## Estructura de la Base de Datos

Ver archivo `diagrama E-TECH_STORE.jpg` en la raíz del proyecto para el diagrama entidad-relación completo.

## Documentación Adicional

- [README Backend](./backend/README.md) - Documentación detallada del backend
- [README Frontend](./frontend/README.md) - Documentación detallada del frontend
- [Security Audit](./SECURITY_AUDIT.md) - Auditoría de seguridad

## Roadmap

- [ ] Implementación de refresh tokens
- [ ] Exportación de resultados a Excel/PDF
- [ ] Gráficos avanzados con Chart.js
- [ ] Notificaciones por email
- [ ] Sistema de permisos granular
- [ ] Internacionalización (i18n)
- [ ] PWA (Progressive Web App)

## Licencia

Proyecto académico desarrollado con fines educativos.

## Autor

Luis Miguel Castañeda Arciniegas

## Contacto

Para consultas o soporte:
- GitHub: [@lmcadev](https://github.com/lmcadev)
- Repositorio: [gestion_encuestas](https://github.com/lmcadev/gestion_encuestas)
