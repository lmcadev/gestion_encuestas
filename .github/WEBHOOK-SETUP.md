# Configuración de Webhook de Portainer

## URL del Webhook
```
http://72.61.1.242:9000/api/stacks/webhooks/3b6c79f2-9ad7-461f-b9cc-abf3df38c83f
```

## Pasos para Configurar

### 1. Abrir el puerto 9000 en el firewall del VPS

```bash
ssh root@72.61.1.242
sudo ufw allow 9000/tcp
sudo ufw status
```

### 2. Añadir el Secret en GitHub

1. Ve a: https://github.com/lmcadev/gestion_encuestas/settings/secrets/actions
2. Click en **"New repository secret"**
3. Crear el secret:
   - **Name:** `PORTAINER_WEBHOOK_URL`
   - **Secret:** `http://72.61.1.242:9000/api/stacks/webhooks/3b6c79f2-9ad7-461f-b9cc-abf3df38c83f`
4. Click **"Add secret"**

### 3. Verificar que el workflow esté actualizado

El archivo `.github/workflows/deploy.yml` ya está configurado para usar el webhook.

### 4. Probar el despliegue automático

Haz un cambio pequeño y push a main:

```bash
git add .
git commit -m "test: Probar webhook de Portainer"
git push origin main
```

GitHub Actions debería:
1. Construir las imágenes
2. Subirlas a ghcr.io
3. Llamar al webhook de Portainer
4. Portainer automáticamente hará pull y redespliegue

### 5. Verificar el resultado

- Ve a **GitHub Actions** en tu repo para ver el progreso
- En **Portainer → Stacks → gestion-encuestas** deberías ver la actualización
- Revisa los logs en Portainer si hay algún problema

## Notas de Seguridad

⚠️ **IMPORTANTE**: El puerto 9000 estará expuesto públicamente. Considera:

1. **Usar HTTPS (puerto 9443)** en lugar de HTTP (puerto 9000):
   - Cambiar la URL a: `https://72.61.1.242:9443/api/stacks/webhooks/3b6c79f2-9ad7-461f-b9cc-abf3df38c83f`
   - Abrir puerto: `sudo ufw allow 9443/tcp`

2. **Restringir acceso por IP** (solo permitir IPs de GitHub Actions):
   - Ver IPs: https://api.github.com/meta (buscar "actions")
   
3. **Usar Nginx como proxy** para Portainer con autenticación adicional

## Verificación

Prueba el webhook manualmente:

```bash
curl -X POST http://72.61.1.242:9000/api/stacks/webhooks/3b6c79f2-9ad7-461f-b9cc-abf3df38c83f
```

Si responde correctamente, el webhook está funcionando.
