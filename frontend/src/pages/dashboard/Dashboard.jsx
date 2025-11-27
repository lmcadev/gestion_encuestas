import React, { useEffect, useState } from 'react';
import apiClient from '../../utils/apiClient';

export default function Dashboard() {
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
      
      console.log('Respuesta completa del backend:', JSON.stringify(data, null, 2));
      console.log('Satisfacción por producto:', data.satisfaccionPorProducto);
      
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
    return <div className="flex items-center justify-center h-full">Cargando...</div>;
  }

  return (
    <div className="space-y-6">
      {/* KPIs Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-4">
        <div className="bg-white rounded-2xl p-5 border-2 border-gray-200 shadow-sm">
          <div className="text-xs text-gray-600 mb-2">Total encuestas</div>
          <div className="text-3xl font-bold text-gray-800 mb-1">{stats.totalEncuestas}</div>
          <div className="text-xs text-gray-400">#% vs mes anterior</div>
        </div>

        <div className="bg-white rounded-2xl p-5 border-2 border-gray-200 shadow-sm">
          <div className="text-xs text-gray-600 mb-2">Satisfacción Promedio</div>
          <div className="text-3xl font-bold text-gray-800 mb-1">{stats.satisfaccionPromedio.toFixed(1)} / 5</div>
          <div className="text-xl text-yellow-400">{renderStars(stats.satisfaccionPromedio)}</div>
        </div>

        <div className="bg-white rounded-2xl p-5 border-2 border-gray-200 shadow-sm">
          <div className="text-xs text-gray-600 mb-2">Tasa de Recomendación</div>
          <div className="text-3xl font-bold text-gray-800">{stats.tasaRecomendacion.toFixed(1)} %</div>
        </div>

        <div className="bg-white rounded-2xl p-5 border-2 border-gray-200 shadow-sm">
          <div className="text-xs text-gray-600 mb-2">Tasa de Completitud</div>
          <div className="text-3xl font-bold text-gray-800">{stats.tasaCompletitud.toFixed(1)} %</div>
        </div>
      </div>

      {/* Grid de 3 columnas */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-4">
        {/* Satisfacción por Producto */}
        <div className="bg-white rounded-2xl border-2 border-gray-200 shadow-sm p-5">
          <h2 className="text-sm font-bold text-gray-800 mb-0">Satisfacción Promedio</h2>
          <p className="text-xs text-gray-500 mb-5">Promedio de puntuación</p>
          <div className="space-y-3">
            {satisfaccionPorProducto.slice(0, 5).map((producto, index) => (
              <div key={index} className="pb-3 border-b border-gray-200 last:border-0 flex items-center justify-between">
                <div className="font-medium text-gray-800 text-sm">{producto.producto}</div>
                <div className="text-xs text-gray-400">{producto.respuestas} respuestas | {producto.promedio ? producto.promedio.toFixed(1) : '0.0'} / 5</div>
              </div>
            ))}
          </div>
        </div>

        {/* Aspectos Mejor Calificados */}
        <div className="bg-white rounded-2xl border-2 border-gray-200 shadow-sm p-5">
          <h2 className="text-sm font-bold text-gray-800 mb-0">Aspecto mejor calificado</h2>
          <p className="text-xs text-gray-500 mb-5">Promedio mejor puntuado</p>
          <div className="space-y-3">
            {aspectosMejorCalificados.slice(0, 5).map((aspecto, index) => (
              <div key={index} className="pb-3 border-b border-gray-200 last:border-0 flex items-center justify-between">
                <div className="font-medium text-gray-800 text-sm">Pregunta {index + 1}</div>
                <div className="text-xs text-gray-400">{aspecto.respuestas || 0} respuestas | {aspecto.promedio.toFixed(1)} / 5</div>
              </div>
            ))}
          </div>
        </div>

        {/* Comentarios Recientes */}
        <div className="bg-white rounded-2xl border-2 border-gray-200 shadow-sm p-5">
          <h2 className="text-sm font-bold text-gray-800 mb-0">Comentarios recientes</h2>
          <p className="text-xs text-gray-500 mb-5">&nbsp;</p>
          <div className="space-y-3">
            {comentariosRecientes.length > 0 ? comentariosRecientes.slice(0, 3).map((comentario, index) => (
              <div key={index} className="pb-3 border-b border-gray-200 last:border-0">
                <div className="flex items-center justify-between mb-1">
                  <div className="text-xs font-semibold text-gray-800">
                    {comentario.usuario || 'Usuario'}
                  </div>
                  <div className="text-xs text-gray-400">
                    {comentario.fecha || ''}
                  </div>
                </div>
                <div className="text-xs text-gray-600 leading-relaxed">
                  {comentario.comentario || 'Sin comentario'}
                </div>
              </div>
            )) : (
              <div className="text-xs text-gray-400 text-center py-4">No hay comentarios recientes</div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
