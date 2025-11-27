# Despliegue en VPS con Portainer y CloudPanel

## Arquitectura

```
Internet
    ↓
CloudPanel Nginx (encuesta.lmcadev.com:443 - SSL)
    ↓
Reverse Proxy → localhost:3000
    ↓
Docker Containers (Portainer)
    - frontend:80 → expuesto como :3000
    - backend:8080 (interno)
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

### 2. Configurar Secrets en GitHub

En tu repositorio: Settings → Secrets and variables → Actions

Agregar:
- `PORTAINER_WEBHOOK_URL`: URL del webhook de Portainer (ver paso 5)

### 3. Crear Sitio en CloudPanel

1. Accede a CloudPanel
2. Sites → Add Site → Reverse Proxy
3. Configuración:
   - **Domain**: `encuesta.lmcadev.com`
   - **Reverse Proxy URL**: `http://localhost:3000`
   - **SSL**: Habilitar Let's Encrypt
4. Click "Create"

### 4. Configurar Stack en Portainer

1. Accede a Portainer
2. Selecciona tu environment local
3. Stacks → Add stack
4. Configuración:
   - **Name**: `gestion-encuestas`
   - **Build method**: Git Repository
   - **Repository URL**: `https://github.com/lmcadev/gestion_encuestas`
   - **Repository reference**: `refs/heads/main`
   - **Compose path**: `docker-compose.prod.yml`
   - **Authentication**: None (si el repo es público)

5. **Environment variables**:
   ```
   POSTGRES_DB=encuestas_prod
   POSTGRES_USER=encuestas_user
   POSTGRES_PASSWORD=TU_PASSWORD_SEGURO_AQUI
   JWT_SECRET=TU_JWT_SECRET_DE_32_CARACTERES_MINIMO
   JWT_EXPIRATION=3600000
   ```

6. Click "Deploy the stack"

### 5. Configurar Webhook de Portainer

1. En Portainer, ve a **Stacks** en el menú lateral
2. Busca tu stack `gestion-encuestas` en la lista
3. Click en el nombre del stack para entrar a los detalles
4. En la parte superior, busca el botón/ícono de **webhook** (puede ser un ícono de cadena/link o dice "Webhook")
   - Si no lo ves, haz click en los tres puntos (⋮) o menú de opciones del stack
5. Click en **"Create a service webhook"** o **"Add webhook"**
6. Selecciona el servicio a actualizar (puedes crear uno para `frontend` y otro para `backend`, o uno general para el stack)
7. Copia la URL generada (algo como: `https://tu-ip:9443/api/stacks/webhooks/xxx-xxx-xxx`)
8. Agrégala como secret `PORTAINER_WEBHOOK_URL` en GitHub (Settings → Secrets and variables → Actions → New repository secret)

**Nota**: Si no encuentras la opción de webhook en tu versión de Portainer, es posible que necesites actualizar el stack manualmente o configurar un despliegue por pull automático desde Git.

### 6. Configurar DNS

En tu proveedor de dominio (lmcadev.com):

```
Type: A
Name: encuesta
Value: [IP_DE_TU_VPS]
TTL: 3600
```

### 7. Verificar Configuración de Nginx (CloudPanel)

CloudPanel crea automáticamente la configuración, pero verifica:

```nginx
server {
    listen 80;
    listen [::]:80;
    server_name encuesta.lmcadev.com;
    return 301 https://$host$request_uri;
}

server {
    listen 443 ssl http2;
    listen [::]:443 ssl http2;
    server_name encuesta.lmcadev.com;

    ssl_certificate /path/to/cert;
    ssl_certificate_key /path/to/key;

    location / {
        proxy_pass http://localhost:3000;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_cache_bypass $http_upgrade;
    }

    # Proxy para API backend
    location /api/ {
        proxy_pass http://localhost:8080;
        proxy_http_version 1.1;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
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
   - Las sube a GitHub Container Registry
   - Ejecuta webhook de Portainer

3. **Portainer recibe webhook**
   - Pull de nuevas imágenes desde ghcr.io
   - Recrea contenedores con nuevas versiones
   - Zero-downtime deployment

4. **Aplicación actualizada**
   - Disponible en https://encuesta.lmcadev.com

## Comandos Útiles

### Ver logs en Portainer
```bash
# Desde Portainer UI:
# Stacks → gestion-encuestas → Logs
```

### Actualizar stack manualmente
```bash
# En Portainer:
# Stacks → gestion-encuestas → Update the stack → Pull and redeploy
```

### Verificar puertos en VPS
```bash
# SSH a tu VPS
ssh root@tu-vps-ip

# Ver contenedores
docker ps

# Ver logs
docker logs gestion-encuestas-frontend-1
docker logs gestion-encuestas-backend-1
docker logs gestion-encuestas-db-1

# Verificar puerto 3000
netstat -tlnp | grep 3000
```

## Troubleshooting

### Error: No se puede conectar al backend

Verificar que el frontend tenga acceso al backend a través de la red Docker:

```bash
docker exec -it gestion-encuestas-frontend-1 ping backend
```

### Error: SSL no funciona

1. Verificar que CloudPanel haya generado el certificado
2. Esperar propagación DNS (hasta 24h)
3. Forzar renovación en CloudPanel

### Error: Portainer no actualiza

1. Verificar webhook URL en secrets de GitHub
2. Ver logs de GitHub Actions
3. Actualizar manualmente: Portainer → Stack → Pull and redeploy

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

1. En Portainer → Stacks → gestion-encuestas
2. Edit stack
3. Cambiar tag de imágenes:
   ```yaml
   backend:
     image: ghcr.io/lmcadev/gestion_encuestas/backend:main-abc123
   frontend:
     image: ghcr.io/lmcadev/gestion_encuestas/frontend:main-abc123
   ```
4. Update the stack

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
