import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import StarRating from '../components/StarRating';
import Header from '../components/Header';

export default function Survey() {
  const { encuestaId } = useParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [encuesta, setEncuesta] = useState(null);
  const [productos, setProductos] = useState([]);
  const [formData, setFormData] = useState({
    nombre: '',
    correo: '',
    telefono: '',
    productoId: ''
  });
  const [respuestas, setRespuestas] = useState({});
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);

  useEffect(() => {
    fetchEncuesta();
    fetchProductos();
  }, [encuestaId]);

  const fetchProductos = async () => {
    try {
      const response = await fetch('/api/productos');
      if (response.ok) {
        const data = await response.json();
        setProductos(data);
      }
    } catch (err) {
      console.error('Error al cargar productos:', err);
    }
  };

  const fetchEncuesta = async () => {
    try {
      let id = encuestaId;
      
      // Si hay ID en la URL, cargar esa encuesta específica
      if (id) {
        const response = await fetch(`/api/encuestas/${id}/completa`);
        if (!response.ok) throw new Error('Encuesta no encontrada');
        
        const data = await response.json();
        setEncuesta(data);
        
        // Si la encuesta tiene producto, pre-seleccionarlo
        if (data.productoId) {
          setFormData(prev => ({ ...prev, productoId: data.productoId }));
        }
        
        // Inicializar respuestas vacías
        const initialRespuestas = {};
        data.preguntas?.forEach(pregunta => {
          if (pregunta.tipoPregunta === 'ABIERTA') {
            initialRespuestas[pregunta.id] = '';
          } else {
            initialRespuestas[pregunta.id] = null;
          }
        });
        setRespuestas(initialRespuestas);
      }
      
      setLoading(false);
    } catch (err) {
      setError('No se pudo cargar la encuesta');
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });

    // Si cambia el producto, cargar la encuesta de ese producto
    if (name === 'productoId' && value) {
      cargarEncuestaPorProducto(value);
    }
  };

  const cargarEncuestaPorProducto = async (productoId) => {
    try {
      setLoading(true);
      const response = await fetch(`/api/encuestas/producto/${productoId}`);
      if (!response.ok) throw new Error('No se encontraron encuestas para este producto');
      
      const encuestas = await response.json();
      if (encuestas.length === 0) {
        setError('No hay encuestas disponibles para este producto');
        setLoading(false);
        return;
      }

      // Cargar la primera encuesta del producto
      const encuestaId = encuestas[0].id;
      const encuestaResponse = await fetch(`/api/encuestas/${encuestaId}/completa`);
      if (!encuestaResponse.ok) throw new Error('Error al cargar la encuesta');
      
      const data = await encuestaResponse.json();
      setEncuesta(data);
      
      // Reinicializar respuestas
      const initialRespuestas = {};
      data.preguntas?.forEach(pregunta => {
        if (pregunta.tipoPregunta === 'ABIERTA') {
          initialRespuestas[pregunta.id] = '';
        } else {
          initialRespuestas[pregunta.id] = null;
        }
      });
      setRespuestas(initialRespuestas);
      setError('');
      setLoading(false);
    } catch (err) {
      setError('No se pudo cargar la encuesta para este producto');
      setLoading(false);
    }
  };

  const handleStarChange = (preguntaId, valor) => {
    setRespuestas({
      ...respuestas,
      [preguntaId]: valor
    });
  };

  const handleTextChange = (preguntaId, texto) => {
    setRespuestas({
      ...respuestas,
      [preguntaId]: texto
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    // Validar campos obligatorios
    if (!formData.nombre || !formData.correo || !formData.telefono) {
      setError('Por favor completa todos los campos obligatorios');
      return;
    }

    // Validar que se haya seleccionado un producto
    if (!formData.productoId) {
      setError('Por favor selecciona un dispositivo/producto');
      return;
    }

    // Validar que haya una encuesta cargada
    if (!encuesta || !encuesta.id) {
      setError('Por favor selecciona un dispositivo para cargar la encuesta');
      return;
    }

    // Validar preguntas obligatorias
    const preguntasObligatorias = encuesta.preguntas?.filter(p => p.obligatoria);
    const faltanRespuestas = preguntasObligatorias?.some(p => !respuestas[p.id]);
    
    if (faltanRespuestas) {
      setError('Por favor responde todas las preguntas obligatorias');
      return;
    }

    try {
      // Crear cliente que responde
      const clienteResponse = await fetch('/api/clientes', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          nombre: formData.nombre,
          email: formData.correo,
          telefono: formData.telefono
        })
      });

      if (!clienteResponse.ok) throw new Error('Error al registrar cliente');
      const cliente = await clienteResponse.json();

      // Enviar respuestas
      const respuestasArray = Object.entries(respuestas)
        .filter(([_, valor]) => valor !== null && valor !== '')
        .map(([preguntaId, valor]) => {
          return {
            clienteId: cliente.id,
            encuestaId: encuesta.id,
            preguntaId: preguntaId,
            valor: String(valor)
          };
        });

      const promises = respuestasArray.map(respuesta =>
        fetch('/api/respuestas', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(respuesta)
        })
      );

      await Promise.all(promises);
      setSuccess(true);
      
      setTimeout(() => {
        navigate('/gracias');
      }, 2000);

    } catch (err) {
      setError('Error al enviar la encuesta. Por favor intenta nuevamente.');
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center" style={{background: 'radial-gradient(ellipse 400% 50% at 50% 0%, #F2F2F2 0%, #153885 15%, #153885 20%, #153885 100%)'}}>
        <div className="text-white text-xl">Cargando...</div>
      </div>
    );
  }

  return (
    <div className="min-h-screen py-12 px-4" style={{background: 'radial-gradient(ellipse 400% 50% at 50% 0%, #F2F2F2 0%, #153885 15%, #153885 20%, #153885 100%)'}}>
      <div className="max-w-5xl mx-auto">
        {/* Header */}
        <Header 
          title={encuesta ? encuesta.titulo : "Encuesta de Satisfacción"} 
          description={encuesta ? encuesta.descripcion : "Selecciona un dispositivo para comenzar"} 
        />

        {/* Form */}
        <div className="bg-white rounded-b-3xl shadow-2xl p-8">
          {error && (
            <div className="mb-6 bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
              {error}
            </div>
          )}

          {success && (
            <div className="mb-6 bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded">
              ¡Gracias por completar la encuesta! Redirigiendo...
            </div>
          )}

          <form onSubmit={handleSubmit} className="grid grid-cols-1 lg:grid-cols-2 gap-8">
            {/* Columna Izquierda */}
            <div className="space-y-6">
              {/* Información del Cliente */}
              <div>
                <label className="block text-gray-700 font-semibold mb-2">
                  Nombres *
                </label>
                <input
                  type="text"
                  name="nombre"
                  value={formData.nombre}
                  onChange={handleInputChange}
                  required
                  className="w-full px-4 py-3 border-2 border-gray-300 rounded-lg focus:border-blue-600 focus:outline-none transition"
                />
              </div>

              <div>
                <label className="block text-gray-700 font-semibold mb-2">
                  Correo *
                </label>
                <input
                  type="email"
                  name="correo"
                  value={formData.correo}
                  onChange={handleInputChange}
                  required
                  className="w-full px-4 py-3 border-2 border-gray-300 rounded-lg focus:border-blue-600 focus:outline-none transition"
                />
              </div>

              <div>
                <label className="block text-gray-700 font-semibold mb-2">
                  Teléfono *
                </label>
                <input
                  type="tel"
                  name="telefono"
                  value={formData.telefono}
                  onChange={handleInputChange}
                  required
                  className="w-full px-4 py-3 border-2 border-gray-300 rounded-lg focus:border-blue-600 focus:outline-none transition"
                />
              </div>

              <div>
                <label className="block text-gray-700 font-semibold mb-2">
                  Dispositivo / Producto *
                </label>
                <select
                  name="productoId"
                  value={formData.productoId}
                  onChange={handleInputChange}
                  required
                  className="w-full px-4 py-3 border-2 border-gray-300 rounded-lg focus:border-blue-600 focus:outline-none transition bg-white"
                >
                  <option value="">Selecciona un producto...</option>
                  {productos.map((producto) => (
                    <option key={producto.id} value={producto.id}>
                      {producto.nombre}
                    </option>
                  ))}
                </select>
              </div>

              {/* Mensaje si no hay encuesta cargada */}
              {!encuesta && (
                <div className="bg-blue-50 border-2 border-blue-200 rounded-lg p-6 text-center">
                  <p className="text-blue-800 font-medium text-lg">
                    👆 Selecciona un dispositivo para cargar las preguntas de la encuesta
                  </p>
                </div>
              )}

              {/* Preguntas con estrellas de la columna izquierda */}
              {encuesta && encuesta.preguntas
                ?.filter(p => p.tipoPregunta !== 'ABIERTA')
                .slice(0, Math.ceil(encuesta.preguntas.filter(p => p.tipoPregunta !== 'ABIERTA').length / 2))
                .map((pregunta) => (
                  <div key={pregunta.id} className="space-y-3">
                    <label className="block text-gray-700 font-medium">
                      {pregunta.texto} {pregunta.obligatoria && '*'}
                    </label>
                    {pregunta.tipoPregunta === 'OPCION_UNICA' && pregunta.opciones?.length === 5 ? (
                      <StarRating
                        value={respuestas[pregunta.id]}
                        onChange={handleStarChange}
                        name={pregunta.id}
                      />
                    ) : (
                      <div className="space-y-2">
                        {pregunta.opciones?.map((opcion) => (
                          <label key={opcion.id} className="flex items-center gap-3 cursor-pointer">
                            <input
                              type="radio"
                              name={pregunta.id}
                              value={opcion.id}
                              checked={respuestas[pregunta.id] === opcion.id}
                              onChange={(e) => handleStarChange(pregunta.id, opcion.id)}
                              className="w-5 h-5 text-blue-600"
                            />
                            <span className="text-gray-700">{opcion.texto}</span>
                          </label>
                        ))}
                      </div>
                    )}
                  </div>
                ))}
            </div>

            {/* Columna Derecha */}
            <div className="space-y-6">
              {/* Preguntas con estrellas de la columna derecha */}
              {encuesta && encuesta.preguntas
                ?.filter(p => p.tipoPregunta !== 'ABIERTA')
                .slice(Math.ceil(encuesta.preguntas.filter(p => p.tipoPregunta !== 'ABIERTA').length / 2))
                .map((pregunta) => (
                  <div key={pregunta.id} className="space-y-3">
                    <label className="block text-gray-700 font-medium">
                      {pregunta.texto} {pregunta.obligatoria && '*'}
                    </label>
                    {pregunta.tipoPregunta === 'OPCION_UNICA' && pregunta.opciones?.length === 5 ? (
                      <StarRating
                        value={respuestas[pregunta.id]}
                        onChange={handleStarChange}
                        name={pregunta.id}
                      />
                    ) : (
                      <div className="space-y-2">
                        {pregunta.opciones?.map((opcion) => (
                          <label key={opcion.id} className="flex items-center gap-3 cursor-pointer">
                            <input
                              type="radio"
                              name={pregunta.id}
                              value={opcion.id}
                              checked={respuestas[pregunta.id] === opcion.id}
                              onChange={(e) => handleStarChange(pregunta.id, opcion.id)}
                              className="w-5 h-5 text-blue-600"
                            />
                            <span className="text-gray-700">{opcion.texto}</span>
                          </label>
                        ))}
                      </div>
                    )}
                  </div>
                ))}

              {/* Preguntas abiertas */}
              {encuesta && encuesta.preguntas
                ?.filter(p => p.tipoPregunta === 'ABIERTA')
                .map((pregunta) => (
                  <div key={pregunta.id} className="space-y-3">
                    <label className="block text-gray-700 font-medium">
                      {pregunta.texto} {pregunta.obligatoria && '*'}
                    </label>
                    <textarea
                      value={respuestas[pregunta.id] || ''}
                      onChange={(e) => handleTextChange(pregunta.id, e.target.value)}
                      required={pregunta.obligatoria}
                      rows={6}
                      className="w-full px-4 py-3 border-2 border-gray-300 rounded-lg focus:border-blue-600 focus:outline-none transition resize-none"
                      placeholder="Escribe tu respuesta aquí..."
                    />
                  </div>
                ))}
            </div>

            {/* Botón Enviar - Span completo */}
            <div className="lg:col-span-2 flex justify-center mt-6">
              <button
                type="submit"
                disabled={success}
                className="bg-blue-600 hover:bg-blue-700 text-white font-bold py-4 px-16 rounded-full text-lg shadow-lg transition-all transform hover:scale-105 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                ENVIAR
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}
