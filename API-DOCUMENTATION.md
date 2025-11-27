# Documentación de la API - Sistema de Encuestas

## Acceso a la Base de Datos

### Información de Conexión PostgreSQL

- **Host**: localhost
- **Puerto**: 5433
- **Base de datos**: monorepo
- **Usuario**: postgres
- **Contraseña**: postgres

### Conectar desde herramientas externas

Puedes conectarte usando cualquier cliente de PostgreSQL:

**psql (línea de comandos):**
```bash
PGPASSWORD=postgres psql -h localhost -p 5433 -U postgres -d monorepo
```

**DBeaver, pgAdmin, DataGrip, etc:**
- Host: localhost
- Port: 5433
- Database: monorepo
- Username: postgres
- Password: postgres

### Credenciales de la Aplicación

**Usuarios del Sistema (Spring Security):**
- Admin: username=admin, password=admin123
- Gerente: username=gerente, password=gerente123

**Usuarios Autorizados (Analistas de Encuestas):**
- Carlos Rodriguez (TechStore): email=carlos.rodriguez@techstore.com.co, password=carlos123
- Maria Gonzalez (ElectrMax): email=maria.gonzalez@electronicosmax.com.co, password=maria123
- Andres Martinez (DigZone): email=andres.martinez@digitalzone.com.co, password=andres123

## Acceso a la Documentación OpenAPI/Swagger

La documentación interactiva de la API está disponible a través de Swagger UI una vez que el backend esté en ejecución.

### URLs de Acceso

- **Swagger UI (Interfaz Interactiva)**: http://localhost:8080/swagger-ui/index.html
- **Documentación OpenAPI JSON**: http://localhost:8080/v3/api-docs
- **Swagger UI alternativa**: http://localhost:8080/swagger-ui.html (redirige a index.html)

## Cómo Usar Swagger UI

1. Inicia los contenedores de Docker:
   ```bash
   docker compose up -d
   ```

2. Abre tu navegador y visita: http://localhost:8080/swagger-ui/index.html

3. Verás todos los endpoints organizados por categorías:
   - **Autenticación**: Registro y login de usuarios
   - **Empresas**: Gestión de empresas
   - **Productos**: Gestión de productos
   - **Encuestas**: Gestión de encuestas
   - **Preguntas**: Gestión de preguntas
   - **Respuestas**: Gestión de respuestas

### Autenticación en Swagger

Para probar endpoints protegidos en Swagger:

1. Primero ejecuta el endpoint `POST /api/auth/login` con credenciales válidas
2. Copia el token JWT de la respuesta
3. Haz clic en el botón "Authorize" (🔒) en la parte superior derecha
4. Ingresa el token en el formato: `Bearer <tu-token-jwt>`
5. Haz clic en "Authorize" y luego "Close"
6. Ahora puedes probar todos los endpoints protegidos

## Importar en Postman

### Método 1: Importar desde URL (Recomendado)

1. Abre Postman
2. Haz clic en "Import" en la esquina superior izquierda
3. Selecciona la pestaña "Link"
4. Ingresa la URL: `http://localhost:8080/v3/api-docs`
5. Haz clic en "Continue" y luego "Import"

### Método 2: Importar desde Archivo

1. Descarga la especificación OpenAPI:
   ```bash
   curl http://localhost:8080/v3/api-docs > openapi.json
   ```

2. En Postman:
   - Haz clic en "Import"
   - Selecciona "Upload Files"
   - Arrastra el archivo `openapi.json` o selecciónalo manualmente
   - Haz clic en "Import"

### Configurar Autenticación en Postman

Después de importar la colección:

1. Ve a la colección importada
2. Haz clic en los tres puntos (...) y selecciona "Edit"
3. Ve a la pestaña "Authorization"
4. Selecciona "Bearer Token" como tipo
5. En el campo "Token", usa una variable: `{{jwt_token}}`
6. Guarda la colección

Para obtener el token:

1. Ejecuta la petición `POST /api/auth/login`
2. Copia el token de la respuesta
3. Ve a la pestaña "Variables" de la colección
4. Crea una variable llamada `jwt_token`
5. Pega el token como valor
6. Guarda

## Endpoints Principales

### Autenticación

- `POST /api/auth/register` - Registrar nuevo usuario
- `POST /api/auth/login` - Iniciar sesión y obtener token JWT

### Empresas

- `GET /api/empresas` - Listar todas las empresas
- `POST /api/empresas` - Crear nueva empresa
- `GET /api/empresas/{id}` - Obtener empresa por ID
- `PUT /api/empresas/{id}` - Actualizar empresa
- `DELETE /api/empresas/{id}` - Eliminar empresa

### Productos

- `GET /api/productos` - Listar todos los productos
- `POST /api/productos` - Crear nuevo producto
- `GET /api/productos/{id}` - Obtener producto por ID
- `PUT /api/productos/{id}` - Actualizar producto
- `DELETE /api/productos/{id}` - Eliminar producto

### Encuestas

- `GET /api/encuestas` - Listar todas las encuestas
- `POST /api/encuestas` - Crear nueva encuesta
- `GET /api/encuestas/{id}` - Obtener encuesta por ID
- `PUT /api/encuestas/{id}` - Actualizar encuesta
- `DELETE /api/encuestas/{id}` - Eliminar encuesta

### Preguntas

- `GET /api/preguntas` - Listar todas las preguntas
- `POST /api/preguntas` - Crear nueva pregunta
- `GET /api/preguntas/{id}` - Obtener pregunta por ID
- `PUT /api/preguntas/{id}` - Actualizar pregunta
- `DELETE /api/preguntas/{id}` - Eliminar pregunta

## Seguridad

Todos los endpoints excepto `/api/auth/register` y `/api/auth/login` requieren autenticación JWT.

**Formato del header de autenticación:**
```
Authorization: Bearer <tu-token-jwt>
```

**Tiempo de expiración del token:** 1 hora (3600000 ms)

## Regenerar Documentación

La documentación se genera automáticamente al iniciar la aplicación. Si haces cambios en los controladores, simplemente reinicia el backend:

```bash
docker compose restart backend
```

## Notas

- La documentación OpenAPI sigue la especificación v3.0
- Swagger UI está configurado con:
  - Operaciones ordenadas por método HTTP
  - Tags ordenados alfabéticamente
  - Función "Try it out" habilitada
- El formato de los tokens JWT es estándar (Header.Payload.Signature)

## Solución de Problemas

Si Swagger UI no carga:

1. Verifica que el backend esté corriendo: `docker ps`
2. Verifica los logs: `docker logs encuestas-backend-1`
3. Asegúrate de estar accediendo a: http://localhost:8080/swagger-ui/index.html

Si no puedes acceder a endpoints protegidos:

1. Verifica que el token JWT sea válido
2. Asegúrate de incluir el prefijo "Bearer " antes del token
3. Verifica que el token no haya expirado (1 hora de validez)
