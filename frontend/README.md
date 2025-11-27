# Frontend - Sistema de Gestión de Encuestas

## Descripción

Aplicación web desarrollada con React y Vite para la gestión de encuestas de satisfacción. Proporciona una interfaz intuitiva para administradores y una experiencia amigable para clientes que responden encuestas.

## Tecnologías Utilizadas

- **React 18**: Biblioteca de JavaScript para interfaces de usuario
- **Vite**: Build tool y servidor de desarrollo rápido
- **React Router DOM**: Navegación y enrutamiento
- **Tailwind CSS**: Framework de CSS utilitario
- **Axios**: Cliente HTTP para consumo de API REST
- **Docker**: Contenedorización con Nginx
- **Jest**: Framework de testing

## Características Principales

### Para Administradores

- Autenticación segura con JWT
- Dashboard con estadísticas en tiempo real
- CRUD completo de:
  - Productos
  - Encuestas
  - Preguntas y opciones de respuesta
  - Usuarios
  - Empresas
- Visualización de respuestas de clientes
- Análisis de resultados con gráficos

### Para Clientes

- Interfaz pública para responder encuestas
- Selección de producto/servicio
- Preguntas con calificación por estrellas
- Preguntas abiertas con área de texto
- Confirmación de envío

## Estructura del Proyecto

```
frontend/
├── public/              # Archivos estáticos
├── src/
│   ├── assets/          # Imágenes y recursos
│   ├── components/      # Componentes reutilizables
│   │   ├── Header.jsx
│   │   ├── NavBar.jsx
│   │   └── StarRating.jsx
│   ├── pages/           # Páginas principales
│   │   ├── Home.jsx
│   │   ├── Login.jsx
│   │   ├── Survey.jsx
│   │   └── dashboard/   # Páginas del dashboard
│   │       ├── Dashboard.jsx
│   │       ├── Empresas.jsx
│   │       ├── Encuestas.jsx
│   │       ├── Preguntas.jsx
│   │       ├── Productos.jsx
│   │       ├── Respuestas.jsx
│   │       └── Usuarios.jsx
│   ├── utils/           # Utilidades
│   │   ├── api.js       # Helper para llamadas API
│   │   └── security.js  # Funciones de seguridad
│   ├── App.jsx          # Componente principal
│   ├── main.jsx         # Punto de entrada
│   └── index.css        # Estilos globales
├── Dockerfile           # Configuración Docker
├── package.json         # Dependencias NPM
├── vite.config.js       # Configuración Vite
└── tailwind.config.js   # Configuración Tailwind
```

## Rutas de la Aplicación

### Rutas Públicas

```
/                    - Página de inicio
/login              - Inicio de sesión
/encuesta           - Encuesta pública (selección de producto)
/encuesta/:id       - Encuesta específica
/gracias            - Página de agradecimiento
```

### Rutas Protegidas (requieren autenticación)

```
/dashboard          - Panel de control con estadísticas
/productos          - Gestión de productos
/encuestas          - Gestión de encuestas
/preguntas          - Gestión de preguntas
/respuestas         - Visualización de respuestas
/usuarios           - Gestión de usuarios
/empresas           - Gestión de empresas
```

## Componentes Principales

### NavBar

Barra de navegación con:
- Logo y nombre del sistema
- Enlaces a secciones del dashboard
- Botón de cierre de sesión
- Indicador de usuario activo

### StarRating

Componente de calificación por estrellas:
- 5 estrellas interactivas
- Hover effect
- Estado seleccionado persistente
- Accesibilidad mejorada

### Header

Encabezado de página con:
- Título dinámico
- Descripción
- Diseño responsivo

## Gestión de Estado

### Autenticación

- Token JWT almacenado en `localStorage`
- Verificación automática en rutas protegidas
- Redirección a login si token inválido
- Logout con limpieza de sesión

### API Client (utils/api.js)

Helper centralizado para llamadas HTTP:

```javascript
// GET request con autenticación
const data = await apiFetch.get('/api/productos');

// POST request
await apiFetch.post('/api/encuestas', formData);

// PUT request
await apiFetch.put(`/api/productos/${id}`, updatedData);

// DELETE request
await apiFetch.delete(`/api/productos/${id}`);
```

Características:
- Inyección automática de token JWT
- Manejo de errores 401 (redirect a login)
- Configuración centralizada de headers

## Estilos y Diseño

### Tailwind CSS

Utiliza clases utilitarias de Tailwind para:
- Layout responsivo con grid y flexbox
- Componentes con hover y focus states
- Animaciones suaves
- Dark mode support (configurado)

### Tema de Colores

```
Primary: #153885 (Azul corporativo)
Background: Gradiente radial con azul
Cards: Blanco con sombras
Success: Verde (#10B981)
Error: Rojo (#EF4444)
Warning: Amarillo (#F59E0B)
```

## Instalación y Ejecución

### Requisitos Previos

- Node.js 18+ y npm
- Backend ejecutándose en `http://localhost:8080`

### Instalación de Dependencias

```bash
cd frontend
npm install
```

### Ejecución en Desarrollo

```bash
npm run dev
```

La aplicación estará disponible en `http://localhost:5173`

### Construcción para Producción

```bash
npm run build
```

Los archivos optimizados se generan en `dist/`

### Ejecución con Docker

```bash
# Desde la raíz del proyecto
docker-compose up -d frontend
```

## Configuración de Proxy

Vite está configurado para proxy de API requests:

```javascript
// vite.config.js
export default defineConfig({
  server: {
    proxy: {
      '/api': {
        target: 'http://backend:8080',
        changeOrigin: true,
      }
    }
  }
})
```

## Testing

```bash
# Ejecutar tests
npm test

# Tests con cobertura
npm run test:coverage

# Tests en modo watch
npm run test:watch
```

## Funcionalidades por Módulo

### Dashboard

- Resumen de estadísticas
- Gráficos de respuestas por producto
- Indicadores de rendimiento
- Accesos rápidos a módulos

### Productos

- Listado con búsqueda y filtros
- Creación con formulario validado
- Edición in-place
- Eliminación con confirmación
- Indicador de encuestas asociadas

### Encuestas

- Gestión completa de encuestas
- Asignación a productos
- Vista previa de preguntas
- Estado activo/inactivo
- Copia de encuestas

### Preguntas

- Tipos: Opción única, Estrellas (1-5), Texto abierto
- Configuración de obligatoriedad
- Orden de preguntas
- Opciones de respuesta editables

### Respuestas

- Visualización por encuesta o cliente
- Filtros avanzados
- Exportación de datos
- Análisis estadístico

## Seguridad

### Implementaciones

- Autenticación JWT en todas las rutas protegidas
- Sanitización de inputs
- Validación de formularios
- Protection contra XSS
- HTTPS en producción (recomendado)

### Headers de Seguridad

```javascript
// Configurados en Nginx (producción)
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 1; mode=block
```

## Optimizaciones

### Performance

- Code splitting por rutas
- Lazy loading de componentes
- Minificación de assets
- Compresión Gzip/Brotli en Nginx
- Caché de recursos estáticos

### Build Optimization

```javascript
// vite.config.js
build: {
  rollupOptions: {
    output: {
      manualChunks: {
        'react-vendor': ['react', 'react-dom', 'react-router-dom']
      }
    }
  }
}
```

## Despliegue

### Docker Production

El `Dockerfile` utiliza multi-stage build:

1. **Stage 1**: Build con Node
2. **Stage 2**: Nginx con archivos estáticos

```dockerfile
FROM node:18-alpine as build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
```

## Problemas Comunes y Soluciones

### CORS Errors

Verificar configuración del backend y proxy de Vite.

### 401 Unauthorized

Token expirado o inválido. Verificar localStorage y hacer login nuevamente.

### Estilos no se aplican

Ejecutar `npm run build` después de cambios en `tailwind.config.js`.

### Hot reload no funciona

Reiniciar servidor de desarrollo: `npm run dev`

## Mejores Prácticas Implementadas

- **Componentes funcionales** con React Hooks
- **Estado local** con useState y useEffect
- **Routing** con React Router v6
- **Fetch API** centralizado
- **Error boundaries** para errores de renderizado
- **Lazy loading** para optimización
- **Responsive design** mobile-first
- **Accesibilidad** (ARIA labels, semantic HTML)

## Contribución

Este es un proyecto académico. Para contribuir:

1. Fork del repositorio
2. Crear rama feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit de cambios (`git commit -am 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Crear Pull Request

## Recursos Adicionales

- [Documentación de React](https://react.dev/)
- [Vite Docs](https://vitejs.dev/)
- [Tailwind CSS](https://tailwindcss.com/)
- [React Router](https://reactrouter.com/)

## Licencia

Proyecto académico desarrollado con fines educativos.

## Contacto

Para consultas o soporte, contactar al equipo de desarrollo.
