import React, { useEffect, useState } from 'react';
import logo from '../img/logo.svg';

export default function Home() {
  const [stats, setStats] = useState({
    totalEncuestas: 0,
    satisfaccionPromedio: 0,
    tasaRecomendacion: 0,
    tasaCompletitud: 0
  });
  const [satisfaccionPorProducto, setSatisfaccionPorProducto] = useState([]);
  const [aspectosMejorCalificados, setAspectosMejorCalificados] = useState([]);
  const [comentariosRecientes, setComentariosRecientes] = useState([]);
  const [loading, setLoading] = useState(true);
  const username = localStorage.getItem('username') || 'Usuario';

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await fetch('/api/estadisticas', {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      
      if (!response.ok) {
        throw new Error('Error al cargar estadísticas');
      }
      
      const data = await response.json();
      
      console.log('Datos recibidos del backend:', data);
      
      setStats({
        totalEncuestas: data.kpis.totalEncuestas,
        satisfaccionPromedio: data.kpis.satisfaccionPromedio,
        tasaRecomendacion: data.kpis.tasaRecomendacion,
        tasaCompletitud: data.kpis.tasaCompletitud
      });

      setSatisfaccionPorProducto(data.satisfaccionPorProducto || []);
      setAspectosMejorCalificados(data.aspectosMejorCalificados || []);
      setComentariosRecientes(data.comentariosRecientes || []);

      setLoading(false);
    } catch (error) {
      console.error('Error al cargar datos del dashboard:', error);
      setLoading(false);
    }
  };

  const renderStars = (rating) => {
    return '★'.repeat(Math.round(rating));
  };

  if (loading) {
    return <div className="flex items-center justify-center min-h-screen">Cargando...</div>;
  }

  return (
    <div className="min-h-screen" style={{background: 'radial-gradient(ellipse 400% 50% at 50% 0%, #F2F2F2 0%, #153885 15%, #153885 20%, #153885 100%)'}}>
      <div className="max-w-7xl mx-auto p-4">
        {/* Header con botón de cerrar sesión */}
        <div className="rounded-t-3xl shadow-lg p-6 mb-0" style={{background: 'linear-gradient(to right, #2563EB 50%, #60A5FA 100%)'}}>
          <div className="flex items-center justify-between text-white">
            <img src={logo} alt="E-TECH STORE" className="h-12" />
            <div className="flex items-center gap-4">
              <span className="text-lg font-semibold">BIENVENIDO {username}</span>
              <button 
                onClick={() => {
                  localStorage.removeItem('token');
                  localStorage.removeItem('username');
                  window.location.href = '/login';
                }}
                className="p-2 hover:bg-blue-600 rounded"
                title="Cerrar sesión"
              >
                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
                </svg>
              </button>
            </div>
          </div>
        </div>

        {/* Contenido del dashboard */}
        <div className="bg-white rounded-b-3xl shadow-2xl p-8">
          {/* Menú lateral */}
          <div className="flex gap-8">
            <div className="flex-shrink-0">
              <button className="p-3 hover:bg-gray-100 rounded">
                <svg className="w-6 h-6" fill="currentColor" viewBox="0 0 24 24">
                  <path d="M4 6h16M4 12h16M4 18h16" stroke="currentColor" strokeWidth={2} strokeLinecap="round"/>
                </svg>
              </button>
            </div>

            {/* Contenido principal */}
            <div className="flex-1">
              {/* KPI Cards */}
              <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
                {/* Total encuestas */}
                <div className="border-2 border-gray-300 rounded-2xl p-6">
                  <h3 className="text-sm text-gray-600 mb-2">Total encuestas</h3>
                  <p className="text-3xl font-bold mb-2">{stats.totalEncuestas}</p>
                  <p className="text-xs text-gray-500">Respuestas recibidas</p>
                </div>

                {/* Satisfacción Promedio */}
                <div className="border-2 border-gray-300 rounded-2xl p-6">
                  <h3 className="text-sm text-gray-600 mb-2">Satisfacción Promedio</h3>
                  <p className="text-3xl font-bold mb-2">{stats.satisfaccionPromedio} / 5</p>
                  <div className="text-yellow-400 text-xl">{renderStars(stats.satisfaccionPromedio)}</div>
                </div>

                {/* Tasa de Recomendación */}
                <div className="border-2 border-gray-300 rounded-2xl p-6">
                  <h3 className="text-sm text-gray-600 mb-2">Tasa de Recomendación</h3>
                  <p className="text-3xl font-bold">{stats.tasaRecomendacion} %</p>
                </div>

                {/* Tasa de Completitud */}
                <div className="border-2 border-gray-300 rounded-2xl p-6">
                  <h3 className="text-sm text-gray-600 mb-2">Tasa de Completitud</h3>
                  <p className="text-3xl font-bold">{stats.tasaCompletitud} %</p>
                </div>
              </div>

              {/* Secciones inferiores */}
              <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                {/* Satisfacción Promedio por Producto */}
                <div className="border-2 border-gray-300 rounded-2xl p-6">
                  <h3 className="font-bold text-lg mb-4">Satisfacción Promedio</h3>
                  <p className="text-xs text-gray-500 mb-4">Promedio de puntuación</p>
                  <div className="space-y-3">
                    {satisfaccionPorProducto.map((item, idx) => (
                      <div key={idx} className="flex justify-between items-center text-sm">
                        <span>{item.producto}</span>
                        <span className="text-gray-500">{item.respuestas} respuestas | {item.promedio} / 5</span>
                      </div>
                    ))}
                  </div>
                </div>

                {/* Aspecto mejor calificado */}
                <div className="border-2 border-gray-300 rounded-2xl p-6">
                  <h3 className="font-bold text-lg mb-4">Aspecto mejor calificado</h3>
                  <p className="text-xs text-gray-500 mb-4">Promedio mejor puntuado</p>
                  <div className="space-y-3">
                    {aspectosMejorCalificados.map((item, idx) => (
                      <div key={idx} className="flex justify-between items-center text-sm">
                        <span className="truncate mr-2">{item.pregunta}</span>
                        <span className="text-gray-500 whitespace-nowrap">{item.respuestas} respuestas | {item.promedio} / 5</span>
                      </div>
                    ))}
                  </div>
                </div>

                {/* Comentarios recientes */}
                <div className="border-2 border-gray-300 rounded-2xl p-6">
                  <h3 className="font-bold text-lg mb-4">Comentarios recientes</h3>
                  <div className="space-y-4">
                    {comentariosRecientes.map((item, idx) => (
                      <div key={idx} className="text-sm">
                        <div className="flex justify-between text-xs text-gray-500 mb-1">
                          <span>{item.usuario}</span>
                          <span>{item.fecha}</span>
                        </div>
                        <p className="text-xs text-gray-700">{item.comentario}</p>
                      </div>
                    ))}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}