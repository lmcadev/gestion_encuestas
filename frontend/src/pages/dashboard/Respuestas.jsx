import React, { useEffect, useState } from 'react';

export default function Respuestas() {
  const [respuestas, setRespuestas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [clientesExpandidos, setClientesExpandidos] = useState({});
  const [encuestasExpandidas, setEncuestasExpandidas] = useState({});
  const [filtros, setFiltros] = useState({
    encuesta: '',
    cliente: '',
    fechaDesde: '',
    fechaHasta: ''
  });

  useEffect(() => {
    fetchRespuestas();
  }, []);

  const fetchRespuestas = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await fetch('/api/respuestas', {
        headers: { Authorization: `Bearer ${token}` }
      });
      if (response.ok) {
        const data = await response.json();
        setRespuestas(data);
      }
      setLoading(false);
    } catch (error) {
      console.error('Error al cargar respuestas:', error);
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (!confirm('¿Estás seguro de eliminar esta respuesta?')) return;
    
    try {
      const token = localStorage.getItem('token');
      const response = await fetch(`/api/respuestas/${id}`, {
        method: 'DELETE',
        headers: { Authorization: `Bearer ${token}` }
      });
      if (response.ok) {
        fetchRespuestas();
      }
    } catch (error) {
      console.error('Error al eliminar respuesta:', error);
    }
  };

  const getRespuestaColor = (valor) => {
    const num = parseFloat(valor);
    if (isNaN(num)) return 'bg-gray-100 text-gray-800';
    if (num >= 4) return 'bg-green-100 text-green-800';
    if (num >= 3) return 'bg-yellow-100 text-yellow-800';
    return 'bg-red-100 text-red-800';
  };

  const respuestasFiltradas = respuestas.filter(respuesta => {
    if (filtros.cliente && !respuesta.clienteNombre?.toLowerCase().includes(filtros.cliente.toLowerCase())) {
      return false;
    }
    if (filtros.fechaDesde && respuesta.fecha && new Date(respuesta.fecha) < new Date(filtros.fechaDesde)) {
      return false;
    }
    if (filtros.fechaHasta && respuesta.fecha && new Date(respuesta.fecha) > new Date(filtros.fechaHasta)) {
      return false;
    }
    return true;
  });

  // Agrupar respuestas por cliente > encuesta > pregunta
  const respuestasAgrupadas = respuestasFiltradas.reduce((acc, respuesta) => {
    const clienteNombre = respuesta.clienteNombre || 'Cliente Desconocido';
    const encuestaId = respuesta.encuestaId;
    const encuestaTitulo = respuesta.encuestaTitulo || 'Sin encuesta';
    const preguntaId = respuesta.preguntaId;
    const preguntaTexto = respuesta.preguntaTexto || 'Sin pregunta';
    
    // Agrupar por cliente
    if (!acc[clienteNombre]) {
      acc[clienteNombre] = {
        nombre: clienteNombre,
        encuestas: {}
      };
    }
    
    // Agrupar por encuesta dentro del cliente
    if (!acc[clienteNombre].encuestas[encuestaId]) {
      acc[clienteNombre].encuestas[encuestaId] = {
        id: encuestaId,
        titulo: encuestaTitulo,
        preguntas: {}
      };
    }
    
    // Agrupar por pregunta dentro de la encuesta
    if (!acc[clienteNombre].encuestas[encuestaId].preguntas[preguntaId]) {
      acc[clienteNombre].encuestas[encuestaId].preguntas[preguntaId] = {
        id: preguntaId,
        texto: preguntaTexto,
        respuestas: []
      };
    }
    
    acc[clienteNombre].encuestas[encuestaId].preguntas[preguntaId].respuestas.push(respuesta);
    return acc;
  }, {});

  const clientes = Object.values(respuestasAgrupadas).map(cliente => ({
    nombre: cliente.nombre,
    encuestas: Object.values(cliente.encuestas).map(encuesta => ({
      id: encuesta.id,
      titulo: encuesta.titulo,
      preguntas: Object.values(encuesta.preguntas)
    }))
  }));

  const toggleCliente = (clienteNombre) => {
    setClientesExpandidos(prev => ({
      ...prev,
      [clienteNombre]: !prev[clienteNombre]
    }));
  };

  const toggleEncuesta = (clienteNombre, encuestaId) => {
    const key = `${clienteNombre}-${encuestaId}`;
    setEncuestasExpandidas(prev => ({
      ...prev,
      [key]: !prev[key]
    }));
  };

  if (loading) {
    return <div className="flex items-center justify-center h-full">Cargando...</div>;
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold text-gray-800">Respuestas</h1>
        <div className="text-sm text-gray-600">
          Total: {respuestasFiltradas.length} respuestas
        </div>
      </div>

      {/* Filtros */}
      <div className="bg-white rounded-xl shadow-md p-6 border border-gray-200">
        <h3 className="font-semibold text-gray-800 mb-4">Filtros</h3>
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <input
            type="text"
            placeholder="Buscar por cliente..."
            value={filtros.cliente}
            onChange={(e) => setFiltros({ ...filtros, cliente: e.target.value })}
            className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          />
          <input
            type="date"
            value={filtros.fechaDesde}
            onChange={(e) => setFiltros({ ...filtros, fechaDesde: e.target.value })}
            className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          />
          <input
            type="date"
            value={filtros.fechaHasta}
            onChange={(e) => setFiltros({ ...filtros, fechaHasta: e.target.value })}
            className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          />
          <button
            onClick={() => setFiltros({ encuesta: '', cliente: '', fechaDesde: '', fechaHasta: '' })}
            className="px-4 py-2 bg-gray-200 hover:bg-gray-300 rounded-lg"
          >
            Limpiar Filtros
          </button>
        </div>
      </div>

      {/* Listado agrupado por clientes > encuestas > preguntas */}
      <div className="space-y-4">
        {clientes.map((cliente) => (
          <div key={cliente.nombre} className="bg-white rounded-xl shadow-md overflow-hidden border border-gray-200">
            {/* Encabezado del cliente */}
            <div 
              onClick={() => toggleCliente(cliente.nombre)}
              className="flex items-center justify-between p-4 bg-gradient-to-r from-purple-50 to-purple-100 cursor-pointer hover:from-purple-100 hover:to-purple-200 transition-colors"
            >
              <div className="flex items-center gap-3">
                <svg 
                  className={`w-6 h-6 text-purple-600 transform transition-transform ${clientesExpandidos[cliente.nombre] ? 'rotate-90' : ''}`}
                  fill="none" 
                  stroke="currentColor" 
                  viewBox="0 0 24 24"
                >
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
                </svg>
                <svg className="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                </svg>
                <h2 className="text-xl font-bold text-gray-800">{cliente.nombre}</h2>
              </div>
              <span className="px-3 py-1 bg-purple-600 text-white text-sm font-semibold rounded-full">
                {cliente.encuestas.length} {cliente.encuestas.length === 1 ? 'encuesta' : 'encuestas'}
              </span>
            </div>

            {/* Encuestas del cliente (colapsable) */}
            {clientesExpandidos[cliente.nombre] && (
              <div className="p-4 space-y-3 bg-gray-50">
                {cliente.encuestas.map((encuesta) => (
                  <div key={encuesta.id} className="bg-white rounded-lg shadow overflow-hidden border border-gray-200">
                    {/* Encabezado de la encuesta */}
                    <div 
                      onClick={() => toggleEncuesta(cliente.nombre, encuesta.id)}
                      className="flex items-center justify-between p-3 bg-gradient-to-r from-blue-50 to-blue-100 cursor-pointer hover:from-blue-100 hover:to-blue-200 transition-colors"
                    >
                      <div className="flex items-center gap-2">
                        <svg 
                          className={`w-5 h-5 text-blue-600 transform transition-transform ${encuestasExpandidas[`${cliente.nombre}-${encuesta.id}`] ? 'rotate-90' : ''}`}
                          fill="none" 
                          stroke="currentColor" 
                          viewBox="0 0 24 24"
                        >
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
                        </svg>
                        <h3 className="text-lg font-semibold text-gray-800">{encuesta.titulo}</h3>
                      </div>
                      <span className="px-2 py-1 bg-blue-600 text-white text-xs font-semibold rounded-full">
                        {encuesta.preguntas.length} {encuesta.preguntas.length === 1 ? 'pregunta' : 'preguntas'}
                      </span>
                    </div>

                    {/* Preguntas de la encuesta (colapsable) */}
                    {encuestasExpandidas[`${cliente.nombre}-${encuesta.id}`] && (
                      <div className="divide-y divide-gray-200">
                        {encuesta.preguntas.map((pregunta) => (
                          <div key={pregunta.id} className="p-4 bg-white">
                            <div className="flex items-start gap-3 mb-3">
                              <svg className="w-5 h-5 text-gray-500 mt-0.5 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8.228 9c.549-1.165 2.03-2 3.772-2 2.21 0 4 1.343 4 3 0 1.4-1.278 2.575-3.006 2.907-.542.104-.994.54-.994 1.093m0 3h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                              </svg>
                              <div className="flex-1">
                                <p className="font-medium text-gray-800 mb-2">{pregunta.texto}</p>
                                <div className="flex flex-wrap gap-2">
                                  {pregunta.respuestas.map((respuesta) => (
                                    <div key={respuesta.id} className="flex items-center gap-2">
                                      <span className={`px-3 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${getRespuestaColor(respuesta.valor)}`}>
                                        {respuesta.valor}
                                      </span>
                                      <span className="text-xs text-gray-500">
                                        {respuesta.fecha ? new Date(respuesta.fecha).toLocaleDateString('es-ES') : '-'}
                                      </span>
                                      <button
                                        onClick={() => handleDelete(respuesta.id)}
                                        className="text-red-600 hover:text-red-900 text-xs"
                                      >
                                        Eliminar
                                      </button>
                                    </div>
                                  ))}
                                </div>
                              </div>
                            </div>
                          </div>
                        ))}
                      </div>
                    )}
                  </div>
                ))}
              </div>
            )}
          </div>
        ))}
      </div>

      {clientes.length === 0 && (
        <div className="text-center py-12">
          <svg className="w-16 h-16 text-gray-400 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
          </svg>
          <p className="text-gray-500">No hay respuestas registradas</p>
        </div>
      )}
    </div>
  );
}
