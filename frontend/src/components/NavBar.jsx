import React from 'react';
import { Link, useNavigate } from 'react-router-dom';

export default function NavBar() {
  const navigate = useNavigate();
  const token = localStorage.getItem('token');

  const logout = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };

  return (
    <nav className="bg-blue-600 text-white p-4 flex justify-between">
      <Link className="font-bold" to="/">Mi Aplicación</Link>
      <div>
        {token ? (
          <button onClick={logout} className="bg-blue-500 hover:bg-blue-700 px-3 py-1 rounded">
            Cerrar sesión
          </button>
        ) : null}
      </div>
    </nav>
  );
}