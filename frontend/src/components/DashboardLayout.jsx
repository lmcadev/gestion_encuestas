import React, { useState } from 'react';
import { Outlet, useNavigate } from 'react-router-dom';
import Sidebar from './Sidebar';
import logo from '../img/logo.svg';
import { clearSession, sanitizeInput } from '../utils/security';

export default function DashboardLayout() {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const navigate = useNavigate();
  const rawUsername = localStorage.getItem('username') || 'Usuario';
  const username = sanitizeInput(rawUsername);

  const handleLogout = () => {
    clearSession();
    navigate('/login');
  };

  return (
    <div className="min-h-screen" style={{background: 'radial-gradient(ellipse 400% 50% at 50% 0%, #F2F2F2 0%, #153885 15%, #153885 20%, #153885 100%)'}}>
      <div className="max-w-7xl mx-auto p-4">
        {/* Header */}
        <div className="rounded-t-3xl shadow-lg p-6 mb-0" style={{background: 'linear-gradient(to right, #2563EB 50%, #60A5FA 100%)'}}>
          <div className="flex items-center justify-between text-white">
            <div className="flex items-center gap-4">
              {/* Botón hamburguesa */}
              <button 
                onClick={() => setIsSidebarOpen(!isSidebarOpen)}
                className="p-2 hover:bg-blue-600 rounded transition-colors"
              >
                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
                </svg>
              </button>
              <img src={logo} alt="E-TECH STORE" className="h-12" />
            </div>
            <div className="flex items-center gap-4">
              <span className="text-lg font-semibold hidden sm:block">BIENVENIDO {username}</span>
              <button 
                onClick={handleLogout}
                className="p-2 hover:bg-blue-600 rounded transition-colors"
                title="Cerrar sesión"
              >
                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
                </svg>
              </button>
            </div>
          </div>
        </div>

        {/* Contenedor principal */}
        <div className="bg-white rounded-b-3xl shadow-2xl overflow-hidden">
          {/* Sidebar (solo visible cuando isSidebarOpen es true) */}
          <Sidebar isOpen={isSidebarOpen} setIsOpen={setIsSidebarOpen} />
          
          {/* Contenido principal */}
          <div className="p-6">
            <Outlet />
          </div>
        </div>
      </div>
    </div>
  );
}
