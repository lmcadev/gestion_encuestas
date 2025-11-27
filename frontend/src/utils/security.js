/**
 * Utilidades de seguridad para el frontend
 */

// Sanitizar inputs para prevenir XSS
export const sanitizeInput = (input) => {
  if (typeof input !== 'string') return input;
  
  const div = document.createElement('div');
  div.textContent = input;
  return div.innerHTML;
};

// Validar formato de email
export const isValidEmail = (email) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
};

// Validar que no contenga scripts maliciosos
export const containsScript = (text) => {
  if (typeof text !== 'string') return false;
  
  const scriptPatterns = [
    /<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi,
    /javascript:/gi,
    /on\w+\s*=/gi, // onclick, onerror, etc.
    /<iframe/gi,
    /<object/gi,
    /<embed/gi
  ];
  
  return scriptPatterns.some(pattern => pattern.test(text));
};

// Validar token JWT antes de usarlo
export const isValidJWT = (token) => {
  if (!token || typeof token !== 'string') return false;
  
  const parts = token.split('.');
  if (parts.length !== 3) return false;
  
  try {
    // Verificar que las partes sean base64 válido
    atob(parts[0]);
    atob(parts[1]);
    return true;
  } catch (e) {
    return false;
  }
};

// Obtener token de forma segura
export const getToken = () => {
  try {
    const token = localStorage.getItem('token');
    if (token && isValidJWT(token)) {
      return token;
    }
    // Si el token no es válido, limpiarlo
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    return null;
  } catch (e) {
    console.error('Error al obtener token:', e);
    return null;
  }
};

// Guardar token de forma segura
export const setToken = (token, username) => {
  if (!token || !isValidJWT(token)) {
    throw new Error('Token inválido');
  }
  
  if (!username || typeof username !== 'string') {
    throw new Error('Username inválido');
  }
  
  localStorage.setItem('token', token);
  localStorage.setItem('username', sanitizeInput(username));
};

// Limpiar sesión
export const clearSession = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('username');
};

// Validar respuesta del servidor
export const validateResponse = async (response) => {
  if (!response.ok) {
    const errorData = await response.json().catch(() => ({ error: 'Error desconocido' }));
    throw new Error(errorData.error || `Error HTTP: ${response.status}`);
  }
  return response;
};

// Rate limiting simple en cliente (prevenir spam)
const requestTimestamps = new Map();

export const checkRateLimit = (key, maxRequests = 5, windowMs = 60000) => {
  const now = Date.now();
  const timestamps = requestTimestamps.get(key) || [];
  
  // Filtrar timestamps fuera de la ventana
  const validTimestamps = timestamps.filter(ts => now - ts < windowMs);
  
  if (validTimestamps.length >= maxRequests) {
    return false; // Rate limit excedido
  }
  
  validTimestamps.push(now);
  requestTimestamps.set(key, validTimestamps);
  return true;
};

// Escapar HTML para prevenir XSS al renderizar contenido dinámico
export const escapeHtml = (text) => {
  const map = {
    '&': '&amp;',
    '<': '&lt;',
    '>': '&gt;',
    '"': '&quot;',
    "'": '&#039;'
  };
  return text.replace(/[&<>"']/g, m => map[m]);
};
