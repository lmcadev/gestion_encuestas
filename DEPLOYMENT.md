# Despliegue en VPS con Portainer y CloudPanel

## Arquitectura

```
Internet
    ↓
CloudPanel Nginx (encuesta.lmcadev.com:443 - SSL)
    ↓
    ├── / → localhost:3000 (Frontend Container)
    └── /api/* → localhost:8081 (Backend Container)
    ↓
Docker Containers (Portainer)
    - frontend:80 → expuesto como 127.0.0.1:3000
    - backend:8080 → expuesto como 127.0.0.1:8081
    - postgres:5432 (interno)
```

## Configuración Paso a Paso

### 1. Configurar GitHub Container Registry

Las imágenes se almacenan en GitHub Container Registry (ghcr.io) automáticamente.

#### Hacer las imágenes públicas:
1. Ve a https://github.com/lmcadev/gestion_encuestas/packages
2. Para cada paquete (backend y frontend):
   - Click en "Package settings"
   - Scroll hasta "Danger Zone"
   - Click "Change visibility" → "Public"

### 2. Hacer públicas las imágenes en GitHub Container Registry

Las imágenes deben ser públicas para que Portainer pueda hacer pull sin autenticación.

1. Ve a https://github.com/lmcadev?tab=packages
2. Para cada paquete (backend y frontend):
   - Click en el paquete
   - Click en "Package settings" (lado derecho)
   - Scroll hasta "Danger Zone"
   - Click "Change visibility" → "Public"
   - Confirma el cambio

### 3. Crear Sitio en CloudPanel

1. Accede a CloudPanel
2. Sites → Add Site → Reverse Proxy
3. Configuración:
   - **Domain**: `encuesta.lmcadev.com`
   - **Reverse Proxy URL**: `http://localhost:3000`
   - **SSL**: Habilitar Let's Encrypt
4. Click "Create"

### 4. Configurar Stack en Portainer

1. Accede a Portainer en tu VPS
2. Selecciona tu environment local
3. **Stacks** → **Add stack**
4. Configuración:
   - **Name**: `gestion-encuestas`
   - **Build method**: **Repository** (Git Repository)
   - **Repository URL**: `https://github.com/lmcadev/gestion_encuestas`
   - **Repository reference**: `refs/heads/main`
   - **Compose path**: `docker-compose.prod.yml`
   - **Authentication**: None (el repo es público)
   - **Automatic updates**: 
     - ✅ Enable **Automatic updates** (opcional, si quieres updates automáticos periódicos)
     - **Fetch interval**: 5 minutes

5. **Environment variables** (sección abajo):
   ```
   POSTGRES_DB=encuestas_prod
   POSTGRES_USER=encuestas_user
   POSTGRES_PASSWORD=TU_PASSWORD_SEGURO_AQUI
   JWT_SECRET=TU_JWT_SECRET_DE_32_CARACTERES_MINIMO
   JWT_EXPIRATION=3600000
   ```

6. Click **"Deploy the stack"**

### 5. Verificar despliegue

```bash
# En Portainer UI:
# Stacks → gestion-encuestas → Ver contenedores activos

# O por SSH:
ssh root@tu-vps-ip
docker ps | grep gestion-encuestas
```

### 6. Configurar DNS

En tu proveedor de dominio (lmcadev.com):

```
Type: A
Name: encuesta
Value: [IP_DE_TU_VPS]
TTL: 3600
```

### 7. Configurar Nginx en CloudPanel

**IMPORTANTE**: Necesitas configurar el proxy tanto para el frontend como para el backend.

1. En CloudPanel → Sites → encuesta.lmcadev.com
2. Click en **"Vhost"** o **"Nginx Settings"**
3. Edita la configuración del bloque `server` con SSL (443):

```nginx
server {
    listen 443 ssl http2;
    listen [::]:443 ssl http2;
    server_name encuesta.lmcadev.com;

    # Certificados SSL (CloudPanel los configura automáticamente)
    ssl_certificate /path/to/cert.pem;
    ssl_certificate_key /path/to/key.pem;

    # Frontend - Todas las rutas excepto /api
    location / {
        proxy_pass http://127.0.0.1:3000;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_cache_bypass $http_upgrade;
    }

    # Backend API - Todas las rutas /api/*
    location /api/ {
        proxy_pass http://127.0.0.1:8081;
        proxy_http_version 1.1;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # Timeouts para requests largos
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
    }

    # Swagger UI (opcional, solo para desarrollo)
    location /swagger-ui/ {
        proxy_pass http://127.0.0.1:8081;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

4. **Reload Nginx**:
```bash
# En tu VPS
sudo systemctl reload nginx
# O desde CloudPanel UI
```

## Flujo de Despliegue Continuo

1. **Desarrollador hace push a `main`**
   ```bash
   git add .
   git commit -m "Nueva funcionalidad"
   git push origin main
   ```

2. **GitHub Actions se ejecuta automáticamente**
   - Construye imágenes Docker de backend y frontend
   - Las sube a GitHub Container Registry con tag `:latest`
   - Las imágenes quedan disponibles públicamente

3. **Actualización en Portainer (manual o automática)**
   
   **Opción A - Manual:**
   - En Portainer → Stacks → gestion-encuestas
   - Click en **"Pull and redeploy"** o **"Update the stack"**
   - Portainer hace pull de `:latest` y recrea contenedores
   
   **Opción B - Automática:**
   - Si habilitaste "Automatic updates" en el stack
   - Portainer verifica cada 5 minutos si hay nuevas imágenes
   - Actualiza automáticamente cuando detecta cambios

4. **Aplicación actualizada**
   - Disponible en https://encuesta.lmcadev.com

## Comandos Útiles

### Ver logs en Portainer
```bash
# En Portainer UI:
# Stacks → gestion-encuestas → Logs (botón en la parte superior)

# O desde terminal:
docker logs <container-id>
```

### Actualizar stack manualmente
```bash
# En Portainer UI:
# Stacks → gestion-encuestas → "Pull and redeploy" o "Update the stack"
# Esto hace pull de las últimas imágenes :latest y recrea los contenedores
```

### Verificar puertos en VPS
```bash
# SSH a tu VPS
ssh root@tu-vps-ip

# Ver contenedores
docker ps

# Ver logs
docker logs <nombre-del-contenedor>

# Verificar puerto 3000
netstat -tlnp | grep 3000
```

## Troubleshooting

### Error: No se puede conectar al backend

Verificar conectividad entre contenedores:

```bash
# En Portainer UI, abrir consola del contenedor frontend
# O por terminal:
docker exec -it <frontend-container-id> ping backend
```

### Error: SSL no funciona

1. Verificar que CloudPanel haya generado el certificado
2. Esperar propagación DNS (hasta 24h)
3. Forzar renovación en CloudPanel

### Error: Portainer no actualiza imágenes

1. Verificar que las imágenes en GitHub Container Registry sean **públicas**
2. En Portainer, hacer click en "Pull and redeploy" manualmente
3. Verificar logs de Portainer para errores de autenticación
4. Si usas "Automatic updates", verificar que esté habilitado y el intervalo configurado

## Seguridad

### Variables de Entorno en Portainer

Nunca commitas archivos `.env` al repositorio. Todas las variables sensibles se configuran en Portainer.

### Firewall en VPS

```bash
# Solo permitir puertos necesarios
ufw allow 22/tcp    # SSH
ufw allow 80/tcp    # HTTP
ufw allow 443/tcp   # HTTPS
ufw enable
```

### Actualizar imágenes base

```bash
# Ejecutar periódicamente para seguridad
docker pull postgres:15
docker pull node:18-alpine
docker pull eclipse-temurin:17-jre
```

## Monitoring

### Verificar estado de la aplicación

```bash
# Healthcheck del backend
curl https://encuesta.lmcadev.com/api/productos

# Verificar frontend
curl -I https://encuesta.lmcadev.com

# Ver uso de recursos
docker stats
```

### Logs persistentes

CloudPanel y Portainer mantienen logs automáticamente. Para logs persistentes adicionales:

```yaml
# Agregar a docker-compose.prod.yml
logging:
  driver: "json-file"
  options:
    max-size: "10m"
    max-file: "3"
```

## Rollback

Si hay problemas con el despliegue:

1. **En Portainer UI:**
   - Stacks → gestion-encuestas → **Edit stack**
   - Cambiar las imágenes de `:latest` a un tag específico anterior:
     ```yaml
     backend:
       image: ghcr.io/lmcadev/gestion_encuestas/backend:main-abc123
     frontend:
       image: ghcr.io/lmcadev/gestion_encuestas/frontend:main-abc123
     ```
   - Click **"Update the stack"**

2. **Encontrar tags anteriores:**
   - Ve a https://github.com/lmcadev/gestion_encuestas/packages
   - Busca los tags disponibles (ejemplo: `main-a1b2c3d`)

## Costos

- **CloudPanel**: Gratis
- **Portainer**: Community Edition gratis
- **GitHub Actions**: 2000 minutos/mes gratis
- **GitHub Container Registry**: Gratis para imágenes públicas
- **Let's Encrypt SSL**: Gratis

## Próximos Pasos

1. Configurar backups automáticos de PostgreSQL
2. Implementar monitoreo con Uptime Kuma
3. Configurar alertas por email
4. Implementar staging environment
