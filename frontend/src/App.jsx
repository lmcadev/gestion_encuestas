import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/Login.jsx';
import Survey from './pages/Survey.jsx';
import ThankYou from './pages/ThankYou.jsx';
import DashboardLayout from './components/DashboardLayout.jsx';
import Dashboard from './pages/dashboard/Dashboard.jsx';
import Usuarios from './pages/dashboard/Usuarios.jsx';
import Productos from './pages/dashboard/Productos.jsx';
import Encuestas from './pages/dashboard/Encuestas.jsx';
import Preguntas from './pages/dashboard/Preguntas.jsx';
import Respuestas from './pages/dashboard/Respuestas.jsx';
import Clientes from './pages/dashboard/Clientes.jsx';

// Componente que protege rutas verificando la presencia de un token JWT.
function ProtectedRoute({ children }) {
  const token = localStorage.getItem('token');
  if (!token) {
    return <Navigate to="/login" replace />;
  }
  return children;
}

export default function App() {
  return (
    <div className="min-h-screen">
      <Routes>
        {/* Rutas públicas */}
        <Route path="/encuesta/:encuestaId" element={<Survey />} />
        <Route path="/encuesta" element={<Survey />} />
        <Route path="/gracias" element={<ThankYou />} />
        <Route path="/login" element={<Login />} />
        
        {/* Rutas protegidas del dashboard */}
        <Route
          path="/dashboard"
          element={
            <ProtectedRoute>
              <DashboardLayout />
            </ProtectedRoute>
          }
        >
          <Route index element={<Dashboard />} />
          <Route path="usuarios" element={<Usuarios />} />
          <Route path="productos" element={<Productos />} />
          <Route path="encuestas" element={<Encuestas />} />
          <Route path="preguntas" element={<Preguntas />} />
          <Route path="respuestas" element={<Respuestas />} />
          <Route path="clientes" element={<Clientes />} />
        </Route>

        {/* Redirección por defecto */}
        <Route path="/" element={<Navigate to="/dashboard" replace />} />
      </Routes>
    </div>
  );
}