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

En tu repositorio: Settings → Secrets and variables → Actions → New repository secret

Agregar los siguientes secrets:

1. **VPS_HOST**: IP de tu VPS (ejemplo: `123.45.67.89`)
2. **VPS_USER**: Usuario SSH (normalmente `root`)
3. **VPS_SSH_KEY**: Clave privada SSH para acceder al VPS (ver instrucciones abajo)

#### Generar y configurar SSH Key:

**En tu máquina local:**
```bash
# Generar clave SSH (si no tienes una)
ssh-keygen -t ed25519 -C "github-actions" -f ~/.ssh/github_actions_key

# Copiar clave pública al VPS
ssh-copy-id -i ~/.ssh/github_actions_key.pub root@TU_VPS_IP

# Mostrar clave privada para copiarla a GitHub
cat ~/.ssh/github_actions_key
```

**En GitHub:**
- Copia TODO el contenido de la clave privada (desde `-----BEGIN` hasta `-----END`)
- Pégalo en el secret `VPS_SSH_KEY`

### 3. Crear Sitio en CloudPanel

1. Accede a CloudPanel
2. Sites → Add Site → Reverse Proxy
3. Configuración:
   - **Domain**: `encuesta.lmcadev.com`
   - **Reverse Proxy URL**: `http://localhost:3000`
   - **SSL**: Habilitar Let's Encrypt
4. Click "Create"

### 4. Crear docker-compose.prod.yml en el VPS

**Conéctate a tu VPS:**
```bash
ssh root@TU_VPS_IP

# Crear directorio para el proyecto
mkdir -p /opt/gestion-encuestas
cd /opt/gestion-encuestas

# Descargar docker-compose.prod.yml
curl -o docker-compose.prod.yml https://raw.githubusercontent.com/lmcadev/gestion_encuestas/main/docker-compose.prod.yml

# Crear archivo .env con variables de entorno
nano .env
```

**Contenido del archivo .env:**
```env
POSTGRES_DB=encuestas_prod
POSTGRES_USER=encuestas_user
POSTGRES_PASSWORD=TU_PASSWORD_SEGURO_AQUI
JWT_SECRET=TU_JWT_SECRET_DE_32_CARACTERES_MINIMO
JWT_EXPIRATION=3600000
```

**Iniciar los contenedores:**
```bash
docker compose -f docker-compose.prod.yml up -d
```

### 5. Verificar que los contenedores estén corriendo

```bash
# Ver contenedores activos
docker ps

# Ver logs
docker compose -f docker-compose.prod.yml logs -f
```

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
   - Se conecta por SSH al VPS
   - Hace pull de las nuevas imágenes
   - Recrea los contenedores

3. **Contenedores actualizados en VPS**
   - Zero-downtime deployment
   - Disponible en https://encuesta.lmcadev.com

## Comandos Útiles

### Ver logs en Portainer
```bash
# SSH al VPS
ssh root@tu-vps-ip

# Ver logs en tiempo real
cd /opt/gestion-encuestas
docker compose -f docker-compose.prod.yml logs -f

# Ver logs de un servicio específico
docker compose -f docker-compose.prod.yml logs -f frontend
docker compose -f docker-compose.prod.yml logs -f backend
docker compose -f docker-compose.prod.yml logs -f db
```

### Actualizar stack manualmente
```bash
# Conéctate por SSH al VPS
ssh root@tu-vps-ip

# Ve al directorio del proyecto
cd /opt/gestion-encuestas

# Pull de nuevas imágenes y recrear contenedores
docker compose -f docker-compose.prod.yml pull
docker compose -f docker-compose.prod.yml up -d --force-recreate
```

### Verificar puertos en VPS
```bash
# SSH a tu VPS
ssh root@tu-vps-ip

# Ver contenedores
docker ps

# Ver logs de contenedores específicos
cd /opt/gestion-encuestas
docker compose -f docker-compose.prod.yml logs frontend
docker compose -f docker-compose.prod.yml logs backend
docker compose -f docker-compose.prod.yml logs db

# Verificar puerto 3000
netstat -tlnp | grep 3000
```

## Troubleshooting

### Error: No se puede conectar al backend

Verificar que el frontend tenga acceso al backend a través de la red Docker:

```bash
# SSH al VPS
cd /opt/gestion-encuestas
docker compose -f docker-compose.prod.yml exec frontend ping backend
```

### Error: SSL no funciona

1. Verificar que CloudPanel haya generado el certificado
2. Esperar propagación DNS (hasta 24h)
3. Forzar renovación en CloudPanel

### Error: GitHub Actions falla en deploy

1. Verificar que los secrets estén correctamente configurados en GitHub
2. Probar conexión SSH manualmente desde tu máquina:
   ```bash
   ssh -i ~/.ssh/github_actions_key root@TU_VPS_IP
   ```
3. Ver logs de GitHub Actions en la pestaña "Actions" del repositorio

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

```bash
# SSH al VPS
ssh root@tu-vps-ip
cd /opt/gestion-encuestas

# Editar docker-compose.prod.yml y cambiar tags de imágenes
nano docker-compose.prod.yml

# Cambiar de:
# image: ghcr.io/lmcadev/gestion_encuestas/backend:latest
# A una versión específica:
# image: ghcr.io/lmcadev/gestion_encuestas/backend:main-abc123

# Recrear contenedores
docker compose -f docker-compose.prod.yml up -d --force-recreate
```

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
