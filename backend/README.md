# Backend - Sistema de Gestión de Encuestas

## Descripción

API REST desarrollada con Spring Boot para la gestión integral de encuestas de satisfacción. Permite administrar productos, encuestas, preguntas, respuestas y análisis de resultados con autenticación JWT.

## Tecnologías Utilizadas

- **Java 17**: Lenguaje de programación principal
- **Spring Boot 3.1.5**: Framework para desarrollo de aplicaciones Java
- **Spring Security**: Autenticación y autorización con JWT
- **Spring Data JPA**: Capa de persistencia con Hibernate
- **PostgreSQL**: Base de datos relacional
- **Maven**: Gestión de dependencias y construcción del proyecto
- **Docker**: Contenedorización de la aplicación
- **Swagger/OpenAPI**: Documentación de la API

## Arquitectura del Proyecto

### Estructura de Paquetes

```
com.example.app/
├── config/              # Configuración de la aplicación
│   └── DataInitializer.java
├── controller/          # Controladores REST
│   ├── AuthController.java
│   ├── ClienteRespondeController.java
│   ├── EmpresaController.java
│   ├── EncuestaController.java
│   ├── OpcionRespuestaController.java
│   ├── PreguntaController.java
│   ├── ProductoController.java
│   ├── RespuestaEncuestaController.java
│   ├── UserController.java
│   └── UsuarioAutorizadoController.java
├── dto/                 # Data Transfer Objects
├── exception/           # Manejo de excepciones
├── model/              # Entidades JPA
├── repository/         # Repositorios JPA
├── security/           # Configuración de seguridad
│   ├── JwtAuthenticationFilter.java
│   ├── JwtTokenProvider.java
│   └── SecurityConfig.java
└── service/            # Lógica de negocio
    └── impl/           # Implementaciones de servicios
```

### Modelo de Datos

El sistema maneja las siguientes entidades principales:

- **User**: Usuarios administrativos del sistema
- **Empresa**: Empresas que realizan encuestas
- **Producto**: Productos o servicios a evaluar
- **Encuesta**: Encuestas asociadas a productos
- **Pregunta**: Preguntas dentro de las encuestas
- **OpcionRespuesta**: Opciones para preguntas de selección
- **ClienteResponde**: Clientes que responden encuestas
- **RespuestaEncuesta**: Respuestas registradas por clientes

## Seguridad

### Autenticación JWT

El sistema implementa autenticación basada en tokens JWT:

- **Login**: POST `/api/auth/login`
- **Registro**: POST `/api/auth/register`
- **Token**: Se incluye en el header `Authorization: Bearer <token>`
- **Expiración**: Configurable mediante variable de entorno (por defecto 1 hora)

### Endpoints Públicos

Los siguientes endpoints son accesibles sin autenticación:

- `/api/auth/login` - Autenticación de usuarios
- `/api/auth/register` - Registro de nuevos usuarios
- `/api/productos` - Listado de productos (para encuestas públicas)
- `/api/encuestas/*/completa` - Encuestas completas
- `/api/encuestas/producto/*` - Encuestas por producto
- `/api/clientes` - Registro de clientes
- `/api/respuestas` - Envío de respuestas de encuestas

### Endpoints Protegidos

Todos los demás endpoints requieren autenticación JWT y están destinados a usuarios administrativos.

## API Endpoints

### Autenticación

```
POST /api/auth/login
POST /api/auth/register
```

### Gestión de Usuarios

```
GET    /api/usuarios
GET    /api/usuarios/{id}
POST   /api/usuarios
PUT    /api/usuarios/{id}
DELETE /api/usuarios/{id}
```

### Gestión de Empresas

```
GET    /api/empresas
GET    /api/empresas/{id}
POST   /api/empresas
PUT    /api/empresas/{id}
DELETE /api/empresas/{id}
```

### Gestión de Productos

```
GET    /api/productos
GET    /api/productos/{id}
POST   /api/productos
PUT    /api/productos/{id}
DELETE /api/productos/{id}
```

### Gestión de Encuestas

```
GET    /api/encuestas
GET    /api/encuestas/{id}
GET    /api/encuestas/{id}/completa
GET    /api/encuestas/producto/{productoId}
POST   /api/encuestas
PUT    /api/encuestas/{id}
DELETE /api/encuestas/{id}
```

### Gestión de Preguntas

```
GET    /api/preguntas
GET    /api/preguntas/{id}
GET    /api/preguntas/encuesta/{encuestaId}
POST   /api/preguntas
PUT    /api/preguntas/{id}
DELETE /api/preguntas/{id}
```

### Gestión de Respuestas

```
POST   /api/respuestas
GET    /api/respuestas/encuesta/{encuestaId}
GET    /api/respuestas/cliente/{clienteId}
```

## Configuración

### Variables de Entorno

Crear un archivo `.env` en la raíz del proyecto basado en `.env.example`:

```properties
# Base de Datos
DB_URL=jdbc:postgresql://db:5432/monorepo
DB_USERNAME=postgres
DB_PASSWORD=your_secure_password

# JWT
JWT_SECRET=your_jwt_secret_key_at_least_32_characters
JWT_EXPIRATION=3600000
```

### Application Properties

El archivo `application.properties` está configurado para leer variables de entorno:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

app.jwt.secret=${JWT_SECRET}
app.jwt.expiration=${JWT_EXPIRATION}
```

## Instalación y Ejecución

### Requisitos Previos

- JDK 17 o superior
- Maven 3.6+
- PostgreSQL 15+
- Docker y Docker Compose (opcional)

### Ejecución con Docker

```bash
# Desde la raíz del proyecto
docker-compose up -d
```

El backend estará disponible en `http://localhost:8080`

### Ejecución Local

```bash
cd backend

# Instalar dependencias
mvn clean install

# Ejecutar aplicación
mvn spring-boot:run
```

### Construcción del JAR

```bash
mvn clean package
java -jar target/app-0.0.1-SNAPSHOT.jar
```

## Testing

```bash
# Ejecutar tests unitarios
mvn test

# Ejecutar tests con cobertura
mvn test jacoco:report
```

## Documentación de la API

Una vez iniciada la aplicación, acceder a:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

## Inicialización de Datos

El sistema incluye `DataInitializer.java` que carga datos de prueba:

- Usuario administrador: `admin` / `admin123`
- Productos de ejemplo
- Encuestas de demostración
- Preguntas predefinidas

## Mejores Prácticas Implementadas

- **Arquitectura en capas**: Separación clara entre controladores, servicios y repositorios
- **DTOs**: Uso de objetos de transferencia para desacoplar la API del modelo de datos
- **Manejo de excepciones**: GlobalExceptionHandler centralizado
- **Validación**: Bean Validation en DTOs
- **Logging**: SLF4J con configuración adecuada
- **CORS**: Configuración flexible para desarrollo y producción
- **Seguridad**: Protección de endpoints, sanitización de errores
- **Documentación**: OpenAPI/Swagger integrado

## Problemas Conocidos y Soluciones

### Error de conexión a base de datos

Verificar que PostgreSQL esté ejecutándose y las credenciales sean correctas.

### Token JWT inválido

Asegurarse de que `JWT_SECRET` tenga al menos 32 caracteres.

### CORS

La configuración actual permite todos los orígenes en desarrollo. Para producción, especificar orígenes permitidos.

## Contribución

Este proyecto es de carácter académico. Para contribuir:

1. Fork del repositorio
2. Crear rama feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit de cambios (`git commit -am 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Crear Pull Request

## Licencia

Proyecto académico desarrollado con fines educativos.

## Contacto

Para consultas o soporte, contactar al equipo de desarrollo.
