import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Header from '../components/Header';

export default function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    
    // Validaciones básicas
    if (!username || !password) {
      setError('Usuario y contraseña son requeridos');
      return;
    }
    
    try {
      console.log('Intentando login con:', username.trim());
      
      const response = await fetch('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ 
          username: username.trim(), 
          password 
        }),
      });
      
      console.log('Respuesta recibida:', response.status);
      
      if (!response.ok) {
        const errorText = await response.text();
        console.error('Error del servidor:', errorText);
        if (response.status === 401) {
          setError('Usuario o contraseña incorrectos');
        } else {
          setError('Error al iniciar sesión. Intente nuevamente.');
        }
        return;
      }
      
      const data = await response.json();
      console.log('Token recibido:', data.token ? 'Sí' : 'No');
      
      if (!data.token) {
        setError('Respuesta inválida del servidor');
        return;
      }
      
      // Guardar token y username
      localStorage.setItem('token', data.token);
      localStorage.setItem('username', username.trim());
      console.log('Token guardado, redirigiendo...');
      
      // Redirigir al dashboard
      window.location.href = '/dashboard';
    } catch (err) {
      console.error('Error en login:', err);
      setError('Error al conectar con el servidor: ' + err.message);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center p-4" 
         style={{background: 'radial-gradient(ellipse 400% 50% at 50% 0%, #F2F2F2 0%, #153885 15%, #153885 20%, #153885 100%)'}}>
      <div className="max-w-md w-full">
        <Header />
        
        <div className="bg-white rounded-b-3xl shadow-2xl p-8">
          <form onSubmit={handleSubmit} className="space-y-6">
            <div>
              <label htmlFor="username" className="block text-sm font-medium text-gray-700 mb-2">
                Nombres
              </label>
              <input
                id="username"
                type="text"
                className="w-full border border-gray-300 rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                required
              />
            </div>
            
            <div>
              <label htmlFor="password" className="block text-sm font-medium text-gray-700 mb-2">
                Correo
              </label>
              <input
                id="password"
                type="password"
                className="w-full border border-gray-300 rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
            </div>

            <div className="text-center">
              <a href="#" className="text-sm text-gray-600 hover:text-gray-800">
                ¿Olvido su contraseña?
              </a>
            </div>

            {error && (
              <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
                {error}
              </div>
            )}

            <button
              type="submit"
              className="w-full bg-blue-600 hover:bg-blue-700 text-white font-semibold py-3 rounded-full transition-colors"
            >
              ENTRAR
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}